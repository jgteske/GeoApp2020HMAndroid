package com.example.geoapp2020.ui.data;
// source: 6ter_Lehrbrief_GeoApp
// adapted to fit the Fragment management from jteske 03.07.2020

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.geoapp2020.R;

import java.util.ArrayList;
import java.util.List;


public class DataFragment extends Fragment {

    private DBAccess dbAccess;
    DataListAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_data, container, false);

        // show list of your Locations
        dbAccess = new DBAccess(getContext(), "database_locations.sqlite");
        List locations = dbAccess.readDataset();
        ListView listView = (ListView) root.findViewById(R.id.list_view_location);
        adapter = new DataListAdapter(getContext(), (ArrayList<Dataset>) locations);

        listView.setAdapter(adapter);

        return root;
    }

}
