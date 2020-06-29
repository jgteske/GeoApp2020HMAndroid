package com.example.geoapp2020.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.geoapp2020.R;
import com.example.geoapp2020.ui.dialog.DialogAlertFragment;
import com.example.geoapp2020.ui.dialog.DialogTipFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

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


        // setting up buttons to link with navigation elements
        final Button button = (Button) root.findViewById(R.id.button_aufgaben);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_webview, null));
        Button button1 = (Button) root.findViewById(R.id.button_gallery);
        button1.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_gallery, null));
        Button button2 = (Button) root.findViewById(R.id.button_map);
        button2.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_map, null));


        // chaning buttonstyle on click base source: https://stackoverflow.com/questions/48479026/android-change-button-style-resource-on-click  -  modified by jteske
        // Loading first news on App startup
        final TextView newsText = (TextView) root.findViewById(R.id.news_text);
        newsText.setText(R.string.newsText_1);

        // News Buttons
        final Button button3 = (Button) root.findViewById(R.id.button_news_1);
        button3.setActivated(true); // activated by default
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Changes style with the drawables > button_selector.xml
                button3.setActivated(true);
                // sets other buttons to Activated = false
                root.findViewById(R.id.button_news_2).setActivated(false);
                root.findViewById(R.id.button_news_3).setActivated(false);

                newsText.setText(R.string.newsText_1);
                Toast.makeText(getContext(), "News 1 geladen", Toast.LENGTH_SHORT).show();
            }
        });
        final Button button4 = (Button) root.findViewById(R.id.button_news_2);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Changes style with the drawables > button_selector.xml
                button4.setActivated(true);
                // sets other buttons to Activated = false
                root.findViewById(R.id.button_news_1).setActivated(false);
                root.findViewById(R.id.button_news_3).setActivated(false);

                newsText.setText(R.string.newsText_2);
                Toast.makeText(getContext(), "News 2 geladen", Toast.LENGTH_SHORT).show();
            }
        });
        final Button button5 = (Button) root.findViewById(R.id.button_news_3);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Changes style with the drawables > button_selector.xml
                button5.setActivated(true);
                // sets other buttons to Activated = false
                root.findViewById(R.id.button_news_1).setActivated(false);
                root.findViewById(R.id.button_news_2).setActivated(false);

                newsText.setText(R.string.newsText_3);
                Toast.makeText(getContext(), "News 3 geladen", Toast.LENGTH_SHORT).show();
            }
        });


        // setting up buttons to alert tip
        Button button6 = (Button) root.findViewById(R.id.button_tip);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DialogTipFragment.newInstance(
                        R.string.dialog_tip);
                newFragment.show(getFragmentManager(), "dialog");
            }
        });

        // setting up buttons to alert tip
        Button button7 = (Button) root.findViewById(R.id.button_alert);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DialogAlertFragment.newInstance(
                        R.string.dialog_alert);
                newFragment.show(getFragmentManager(), "dialog");
            }
        });


        return root;
    }
}
