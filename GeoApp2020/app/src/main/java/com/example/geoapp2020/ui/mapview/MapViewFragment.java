package com.example.geoapp2020.ui.mapview;
// reference is this tutorial from osmand:  https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library
// edited, adapted and changed by jteske

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.geoapp2020.R;
import com.example.geoapp2020.ui.data.DBAccess;
import com.example.geoapp2020.ui.data.DataManager;
import com.example.geoapp2020.ui.data.Dataset;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MapViewFragment extends Fragment {


    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mMapView = null;

    private CopyrightOverlay copyrightOverlay;
    private MinimapOverlay minimapOverlay;
    private ScaleBarOverlay scaleBarOverlay;
    private MyLocationNewOverlay locationOverlay;

    private MyLocationProvider myLocationProvider;
    private Location currentLocation;

    private DBAccess dbAccess;
    ArrayList<OverlayItem> overlayItemArray = new ArrayList<OverlayItem>();
    ItemizedIconOverlay<OverlayItem> itemizedIconOverlay;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_mapview, container, false);

        //handle permissions first, before map is created. not depicted here
        final Context ctx = getContext();  // this provides the context of the application!!! IMPORTANT for other functions
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));


        mMapView = (MapView) root.findViewById(R.id.map);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);

        // controller for the starting point of the map
        IMapController mapController = mMapView.getController();
        mapController.setZoom(11);
        GeoPoint startPoint = new GeoPoint(48.135125, 11.581981);
        mapController.setCenter(startPoint);


        //Adding a marker to the map
        GeoPoint punktmarker = new GeoPoint(48.135125, 11.581981);
        Marker startMarker = new Marker(mMapView);
        startMarker.setPosition(punktmarker);
        startMarker.setTitle("Covid19");
        //startMarker.setIcon(getDrawable(ctx, R.drawable.ic_tip));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlays().add(startMarker);

        //Adding a Copyright Overlay
        copyrightOverlay = new CopyrightOverlay(ctx);
        mMapView.getOverlays().add(copyrightOverlay);

        // Adding a Map Scale Bar
        final DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        scaleBarOverlay = new ScaleBarOverlay(mMapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mMapView.getOverlays().add(this.scaleBarOverlay);

        // Minimap Overlay
        minimapOverlay = new MinimapOverlay(ctx, mMapView.getTileRequestCompleteHandler());
        minimapOverlay.setWidth(dm.widthPixels / 5);
        minimapOverlay.setHeight(dm.heightPixels / 5);
        //optionally, you can set the minimap to a different tile source
        minimapOverlay.setTileSource(TileSourceFactory.OpenTopo); // Adding a different Tile Source for the miniMap - selection here: https://osmdroid.github.io/osmdroid/javadocAll/org/osmdroid/tileprovider/tilesource/TileSourceFactory.html
        mMapView.getOverlays().add(this.minimapOverlay);


        /**
         * ////////////////////////////////////////////// Location Manager //////////////////////////////////////////////////////////////////
         */
        GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider(ctx);
        gpsMyLocationProvider.setLocationUpdateMinTime(2000); // Setting GPS Update intervall to ...ms
        this.locationOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, mMapView);
        this.locationOverlay.enableMyLocation();
        this.locationOverlay.disableFollowLocation();
        this.locationOverlay.isDrawAccuracyEnabled();
        this.locationOverlay.setDrawAccuracyEnabled(true);

        Bitmap personIcon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_menu_mylocation);
        this.locationOverlay.setPersonIcon(personIcon);

        mMapView.getOverlays().add(this.locationOverlay);

        // Center to my current position on Button Click
        // source: https://github.com/osmdroid/osmdroid/blob/19053d1435e8fa4e58b04960b23b0769e99adb66/OpenStreetMapViewer/src/main/java/org/osmdroid/samplefragments/events/SampleAnimatedZoomToLocation.java
        //
        // Location Provider
        myLocationProvider = new MyLocationProvider(ctx);
        myLocationProvider.startLocationProvider(new IMyLocationConsumer() {
            @Override
            public void onLocationChanged(Location location, IMyLocationProvider source) {
                //myLocationProvider.stopLocationProvider();
                currentLocation = location;

            }
        });

        ImageButton centerButton = (ImageButton) root.findViewById(R.id.button_location);
        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking if currentLocation is set, otherwise app will crash when location is not set and you press the LccationButton - jteske
                if (currentLocation == null){
                    Toast.makeText(getActivity(), R.string.dialog_no_location, Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), R.string.zoom_on_me, Toast.LENGTH_LONG).show();
                    GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mMapView.getController().animateTo(myPosition, 14.0, (long) 2000); // animateTo: 1.Location, 2.Zoomlevel, 3.animationspeed in ms (jteske)
                }
            }
        });
        ////////////////////////////////////////////// End of Location Manager //////////////////////////////////////////////////////////////////

        // Button to save your current Location in an Database
        Button button = (Button) root.findViewById(R.id.button_save_position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // giving the Activity Context, Latitude nad Longitude of the current location to put into an sqlite-database - jteske
                if (currentLocation != null){
                    DataManager dm = new DataManager(ctx, currentLocation.getLatitude(), currentLocation.getLongitude());
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    Resources res = getResources();
                    String text = res.getString(R.string.dialog_no_location);
                    text += "\n\n" + res.getString(R.string.dialog_try_again);
                    builder.setMessage(text);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            requestPermissionsIfNecessary(new String[] {
                                    // WRITE_EXTERNAL_STORAGE is required in order to show the map
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            });
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        requestPermissionsIfNecessary(new String[] {
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        });


        // Toggle button to show your currently saved positions on the map
        Switch switchButton = (Switch) root.findViewById(R.id.button_switch_locations);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // get location Data from database
                    dbAccess = new DBAccess(getContext(), "database_locations.sqlite");
                    final ArrayList<Dataset> locations = (ArrayList<Dataset>) dbAccess.readDataset();

                    // iterating through all locations and create a marker for each one of it
                    int arr_size = locations.size();

                    // getting multiple markers into one overlay-item http://android-er.blogspot.com/2012/05/create-multi-marker-openstreetmap-for.html
                    for (int i=0; i<arr_size; i++){

                        // Write current data into a dataset
                        Dataset data = locations.get(i);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                        String stringDate = formatter.format(data.RecordingDate);
                        // adding data into the overlayItemArray
                        overlayItemArray.add(new OverlayItem(
                                "1", data.LocationName, stringDate, new GeoPoint( data.LocationLatitude, data.LocationLongitude)));
                    }

                    // create Drawable icon for the itemizedIconOverlay - source http://spearhend.blogspot.com/2012/04/load-android-drawable-from-xml.html
                    // loads the oberlayItemArray with the personalized Icon
                    try {
                        itemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(
                                //getContext(), overlayItemArray, null); // simple for with the standard icon
                                overlayItemArray, Drawable.createFromXml(getResources(), getResources().getXml(R.drawable.ic_added_location)), null, ctx); // https://osmdroid.github.io/osmdroid/javadocs/osmdroid-android/debug/index.html?org/osmdroid/views/overlay/Marker.html
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                    mMapView.getOverlays().add(itemizedIconOverlay);

                } else {
                        mMapView.getOverlays().remove(itemizedIconOverlay);
                        mMapView.invalidate();
                }
            }
        });


        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mMapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up

        // reload the location manager after permission has been granted
        myLocationProvider.startLocationProvider(new IMyLocationConsumer() {
            @Override
            public void onLocationChanged(Location location, IMyLocationProvider source) {
                //myLocationProvider.stopLocationProvider();
                currentLocation = location;

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mMapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
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

    /**
     * Requests permissions, if not set
     *
     * @param permissions
     */
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
