package com.example.geoapp2020.ui.data;
// source: 6ter_Lehrbrief_GeoApp
// adapted to fit the Fragment management from jteske 03.07.2020
// purpose is to create function to call on button-click, filling the database with content!!! - johann teske

import android.content.Context;
import android.widget.Toast;


import java.util.Date;


public class DataManager {

    private Dataset ds1;
    private DBAccess dbAccess;

    public static int COUNT = 0;

    public DataManager(Context context, double lat, double lng) {

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



}
