package com.example.geoapp2020.ui.data;
// based on this example: https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
// adapted to fit the Fragment management from jteske 03.07.2020

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.geoapp2020.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DataListAdapter extends ArrayAdapter<Dataset>{


        public DataListAdapter(Context context,  ArrayList<Dataset> items) {
            super(context, 0, items);
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Dataset object = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_location, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.loc_name);
        TextView tvLat = (TextView) convertView.findViewById(R.id.loc_lat);
        TextView tvLng = (TextView) convertView.findViewById(R.id.loc_lng);
        TextView tvDate = (TextView) convertView.findViewById(R.id.loc_date);

        // Populate the data into the template view using the data object
        tvName.setText(object.LocationName);
        // Converting non string Objects https://stackoverflow.com/questions/40310773/android-studio-textview-show-date
        String stringLat = Double.toString(object.LocationLatitude);
        tvLat.setText(stringLat);
        String stringLng = Double.toString(object.LocationLongitude);
        tvLng.setText(stringLng);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:MM:SS");
        String stringDate = formatter.format(object.RecordingDate);
        tvDate.setText(stringDate);

        // Return the completed view to render on screen
        return convertView;

    }
}
