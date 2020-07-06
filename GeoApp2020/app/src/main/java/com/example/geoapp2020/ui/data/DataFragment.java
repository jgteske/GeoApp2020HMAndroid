package com.example.geoapp2020.ui.data;
/**
 * source: 6ter_Lehrbrief_GeoApp
 * adapted to fit the Fragment management from jteske 03.07.2020
 *
 * Creating a Fragment to show LocationData
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.geoapp2020.R;

import java.util.ArrayList;


public class DataFragment extends Fragment {

    private DBAccess dbAccess;
    DataListAdapter adapter;



    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_data, container, false);

        // show list of your Locations
        dbAccess = new DBAccess(getContext(), "database_locations.sqlite");
        final ArrayList<Dataset> locations = (ArrayList<Dataset>) dbAccess.readDataset();
        ListView listView = (ListView) root.findViewById(R.id.list_view_location);
        adapter = new DataListAdapter(getContext(), (ArrayList<Dataset>) locations);

        if (locations.size() > 0) {
            listView.setAdapter(adapter);
        }else {
            LinearLayout emptyListFiller = (LinearLayout) root.findViewById(R.id.no_location_list);
            emptyListFiller.setVisibility(emptyListFiller.VISIBLE);
        }
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                int itemclicked = position;
                final Dataset object = locations.get(position);
                Resources res = getContext().getResources();
                String text = object.LocationName + "\nGespeichert am: \n" + object.RecordingDate + "\n\n" + res.getString(R.string.dialog_delete);
                builder.setMessage(text);
                builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbAccess.deletDataset(object);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.dialog_button_cancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });


        return root;
    }

}
