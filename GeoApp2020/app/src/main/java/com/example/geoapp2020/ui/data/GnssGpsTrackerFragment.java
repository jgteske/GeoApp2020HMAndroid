package com.example.geoapp2020.ui.data;
/**
 * source: 6ter_Lehrbrief_GeoApp
 * adapted to fit the Fragment management from jteske 04.07.2020
 *
 * A Tracker to track a route (<rte>) with GPS into a .gpx-File and save it on your device
 */


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.geoapp2020.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GnssGpsTrackerFragment extends Fragment implements LocationListener, View.OnClickListener {


    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;


    private TextView showLon;   // show longitude
    private TextView showLat; // shot latitude
    private TextView showAlt; // show altitude
    private TextView showSpeed; // show speed

    private LocationManager locationManager;

    private SimpleDateFormat gpxTimeFormat;

    private Button startButton;
    private Button stopButton;
    private Button saveButton;

    private boolean collectData;
    private List<Location> positions;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_tracker, container, false);
        Context context = getContext();

        // request permission if necessary
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        });


        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        // check if location is enabled
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            promptWarning(context);
        }

        // declare buttons
        // start Button
        startButton = (Button) root.findViewById(R.id.button1);
        startButton.setOnClickListener(this);
        // stop Button
        stopButton = (Button) root.findViewById(R.id.button2);
        stopButton.setOnClickListener(this);
        stopButton.setEnabled(false);
        // save Button
        saveButton = (Button) root.findViewById(R.id.button3);
        saveButton.setOnClickListener(this);
        saveButton.setEnabled(false);
        collectData = false;

        positions = new ArrayList<Location>();
        showLat = (TextView) root.findViewById(R.id.text_view_latitude);
        showLon = (TextView) root.findViewById(R.id.text_view_longitude);
        showAlt = (TextView) root.findViewById(R.id.text_view_altitude);
        showSpeed = (TextView) root.findViewById(R.id.text_view_speed);
        gpxTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


        return root;
    }

    /**
     * manages on click function for buttons
     */
    public void onClick(View v) {
        if (v == startButton) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                saveButton.setEnabled(false);
                collectData = true;
            }else{
                // request permission if necessary
                requestPermissionsIfNecessary(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                });
            }

        } else if (v == stopButton) {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            saveButton.setEnabled(true);
            collectData = false;
        } else if (v == saveButton) {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            saveButton.setEnabled(false);
            collectData = false;
            saveCollectedData(getContext());
        }
    }

    /**
     * dialog to save collected data
     */
    private void saveCollectedData(final Context context) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setOwnerActivity(getActivity());
            dialog.setContentView(R.layout.save);
            // get resource String and set it to the dialog Title
            Resources res = getResources();
            dialog.setTitle(res.getText(R.string.saveDialogTitle));
            final EditText filename = (EditText)
                    dialog.findViewById(R.id.editTextDateiname);
            filename.setText("dateiname.gpx");
            Button save = (Button) dialog.findViewById(R.id.button_save_Ok);
            save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Editable ed = filename.getText();
                    try {
                        writePositions(ed.toString());
                        positions.clear();
                        Toast.makeText(context, R.string.file_saved, Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Log.d("gpxTracker", ex.getMessage());
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception ex) {
            Log.d("gpxTracker", ex.getMessage());
        }
    }

    /**
     * write file onto the external or internal storage gpx-route (.gpx)
     * @throws Exception
     */
    private void writePositions(String filename) throws Exception {
        File sdCard = Environment.getExternalStorageDirectory();
        boolean sdCardExists = (sdCard.exists() && sdCard.canWrite());
        File file;
        if (sdCardExists) {
            file = new File(sdCard.getAbsolutePath() + File.separator + filename);
        } else {
            file = new File(Environment.DIRECTORY_DOWNLOADS + File.separator + filename);
            ;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        // write file head
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n");
        writer.write("<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"jteske\"\n");
        writer.write("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        writer.write("xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1/gpx.xsd\">");
        writer.newLine();
        writer.write("<metadata>\n");
        writer.write("\t<name>" + filename.replace(".gpx","") + "</name>\n");
        writer.write("\t<desc>This GPX-File has been created with an GPS-Tracker implemented in the GeoApp2020</desc>\n");
        writer.write("\t<author>\n");
        writer.write("\t\t<name>" + android.os.Build.MODEL + "</name>\n");
        writer.write("\t</author>\n");
        writer.write("</metadata>");
        writer.newLine();
        writer.write("<rte>\n");
        writer.write("\t<name>Track-GeoApp2020</name>");
        writer.newLine();
        // write positions
        for (Location loc : positions) {
            saveLocation(loc, writer);
        }
        // write file end
        writer.write("</rte>");
        writer.newLine();
        writer.write("</gpx>");
        writer.close();
    }

    /**
     * parses location into writer to generate a gpx track
     * @param loc
     * @param writer
     */
    private void saveLocation(Location loc, BufferedWriter writer) throws IOException {
        writer.write("\t<rtept lat=\"" + loc.getLatitude() + "\" lon=\"" + loc.getLongitude() + "\">");
        writer.newLine();
        writer.write("\t<ele>" + loc.getAltitude() + "</ele>");
        writer.newLine();
        String time = gpxTimeFormat.format(new Date(loc.getTime()));
        writer.write("\t<time>" + time + "</time>");
        writer.newLine();
        writer.write("\t</rtept>");
        writer.newLine();
    }

    /**
     *  prompt user to activate GPS
     */
    private void promptWarning(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Resources res = getResources();
        String text = res.getString(R.string.dialog_no_location);
        builder.setMessage(text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                getActivity().finish(); // stop activity
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, this);
    }

        public void onLocationChanged(Location loc) {
            double lon = loc.getLongitude();
            double lat = loc.getLatitude();
            showLat.setText(Location.convert(lat, Location.FORMAT_SECONDS));
            showLon.setText(Location.convert(lon, Location.FORMAT_SECONDS));
            if(loc.hasAltitude()) {
                showAlt.setText(String.valueOf(loc.getAltitude()));
            }
            if(collectData) {
                positions.add(loc);
            }
            if(loc.hasSpeed()) {
                showSpeed.setText(String.valueOf(loc.getSpeed()) + " m/s");

            }
        }

        public void onProviderDisabled(String provider) {

        }
        public void onProviderEnabled(String provider) {

        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    (Activity) getContext(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    (Activity) getContext(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


}

