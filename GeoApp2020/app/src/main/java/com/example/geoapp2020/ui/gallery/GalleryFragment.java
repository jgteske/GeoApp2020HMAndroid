package com.example.geoapp2020.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.geoapp2020.R;

public class GalleryFragment extends Fragment implements OnItemClickListener {

    private GalleryViewModel galleryViewModel;
    ImageAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);


        GridView galerie = (GridView) root.findViewById(R.id.grid_view);
        adapter = new ImageAdapter(getContext());
        galerie.setAdapter(adapter);
        galerie.setOnItemClickListener(this);

        return root;
    }

    /**
     * Creates an on Click listener for every single Image in the gallery
     *
     * @param parent
     * @param v
     * @param pos
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
        int resid = (Integer) adapter.getItem(pos);
        Intent intent = new Intent(getContext(), SingleImage.class);
        intent.putExtra("resId", resid);
        startActivity(intent);
    }
}
