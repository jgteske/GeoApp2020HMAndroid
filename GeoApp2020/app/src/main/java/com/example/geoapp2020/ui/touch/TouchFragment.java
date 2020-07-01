package com.example.geoapp2020.ui.touch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.geoapp2020.R;

public class TouchFragment extends Fragment {

    private TouchViewModel touchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_touch, container, false);

        touchViewModel = new TouchViewModel(getContext());

        FrameLayout frameLayout = (FrameLayout) root.findViewById(R.id.touch_framelayout);
        frameLayout.addView(touchViewModel);

        return root;
    }
}