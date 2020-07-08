package com.example.geoapp2020.ui.data;
/**
 * source: 6ter_Lehrbrief_GeoApp
 * adapted to fit the Fragment management from jteske 03.07.2020
 *
 * Creating a Dataset
 */

import java.util.Date;

public class Dataset {

    // Variables to set for the database
    public long id;
    public String LocationName;
    public double LocationLatitude;
    public double LocationLongitude;
    public Date RecordingDate;

    // Function to construct the database with params
    public Dataset(String LocationName, double LocationLatitude, double LocationLongitude, Date RecordingDate) {
        this.id = -1; // id will be applied when data is put into the database
        this.LocationName = LocationName;
        this.LocationLatitude = LocationLatitude;
        this.LocationLongitude = LocationLongitude;
        this.RecordingDate = RecordingDate;
    }

    // Constructor without params
    public Dataset(){

    }

}
