package com.example.geoapp2020.ui.data;
/**
 * source: 6ter_Lehrbrief_GeoApp
 * adapted to fit the Fragment management from jteske 03.07.2020
 *
 * purpose is to create function to call on button-click, filling the database with content!!! - johann teske
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.geoapp2020.R;

import java.util.ArrayList;
import java.util.Date;


public class DataManager {

    private int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private Dataset ds1;
    private DBAccess dbAccess;

    public static int COUNT = 0;

    public DataManager(final Context context, double lat, double lng) {
        /**
         * Checks if the database can be written onto external storage
         * Cancels the attempt to write the database in this case and pushes a Comment!
         */
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            Resources res = context.getResources();
            String text = res.getString(R.string.dialog_no_location);
            text += "\n\nUND/ODER\n\n" + res.getString(R.string.dialog_no_writingpermission) + "\n\n" + res.getString(R.string.dialog_try_again);
            builder.setMessage(text);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    requestPermissionsIfNecessary(context, new String[] {
                            // WRITE_EXTERNAL_STORAGE is required in order to show the map
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    });

                    dialog.dismiss();

                    // RETURN important to prevent from creating the database after clicking "Ok" - otherwise the database will be created without the table - jteske 05.07.2020
                    return;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        String sql = "CREATE TABLE table_1 " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "LocationName VARCHAR(20) NOT NULL, " +
                "LocationLatitude DOUBLE, " +
                "LocationLongitude DOUBLE, " +
                "RecordingDate DATE)";
        // creating the table, if it doesn't exist already
        dbAccess = new DBAccess(context, "database_locations.sqlite", sql);
        //Toast.makeText(context, "DB angelegt bzw. geöffnet" + dbAccess, Toast.LENGTH_SHORT).show();

        // loading data into one dataset
        Date date = new Date();


        String locName = "LocationName_"+COUNT;
        // getting LocationName, CurrentLatitude, CurrentLongitude and Date into the dataset
        Dataset dataset = new Dataset(locName, lat, lng, date);

        // adding the data into the database
        dbAccess.addDataset(dataset);

        COUNT++;

        Toast.makeText(context, "Hinzugefügt: " + locName, Toast.LENGTH_SHORT).show();

        // close Database connection
        dbAccess.close();

    }

    /**
     * Requests permissions, if not set
     *
     * @param permissions
     */
    private void requestPermissionsIfNecessary(Context context, String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


}
