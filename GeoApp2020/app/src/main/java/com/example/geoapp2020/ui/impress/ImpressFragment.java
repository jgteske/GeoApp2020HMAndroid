package com.example.geoapp2020.ui.impress;
/**
 * Impress for the GeoApp2020
 *
 * TODO
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.geoapp2020.R;


public class ImpressFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_impress, container, false);


        return root;
    }
}
