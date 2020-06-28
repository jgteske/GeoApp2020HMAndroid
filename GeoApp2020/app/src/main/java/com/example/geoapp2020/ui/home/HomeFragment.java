package com.example.geoapp2020.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.geoapp2020.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // setting home screen titleimage
        ImageView imageView = (ImageView) root.findViewById(R.id.title_image);
        imageView.setImageResource(R.drawable.title_image);

        // setting up buttons
        Button button = (Button) root.findViewById(R.id.button_aufgaben);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_webview, null));
        Button button1 = (Button) root.findViewById(R.id.button_gallery);
        button1.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_gallery, null));
        Button button2 = (Button) root.findViewById(R.id.button_map);
        button2.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_map, null));

        return root;
    }
}
