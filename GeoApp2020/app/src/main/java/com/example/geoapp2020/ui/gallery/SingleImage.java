package com.example.geoapp2020.ui.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.geoapp2020.R;

public class SingleImage extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_single);
        if(getIntent() != null) {
            Bundle daten = getIntent().getExtras();
            if(daten != null) {
                int resid = daten.getInt("resId");
                ImageView bild = (ImageView) findViewById(R.id.imageView1);
                bild.setImageResource(resid);
            }
        }
    }
}
