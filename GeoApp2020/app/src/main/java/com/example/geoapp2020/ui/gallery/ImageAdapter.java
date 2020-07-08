package com.example.geoapp2020.ui.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.geoapp2020.R;

public class ImageAdapter  extends BaseAdapter {
    private Context context;
    private Integer[] bilderIDs = {
            R.raw.image_01, R.raw.image_02,
            R.raw.image_03, R.raw.image_04,
            R.raw.image_05, R.raw.image_06,
            R.raw.image_07, R.raw.image_08,
            R.raw.image_09, R.raw.image_10,
            R.raw.image_11, R.raw.image_12
    };

    public ImageAdapter(Context c) {
        context = c;
    }

    /**
     * Returns imageArray length
     *
     * @return
     */
    @Override
    public int getCount() { return bilderIDs.length; }

    /**
     * Returns Item/Image
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) { return bilderIDs[position]; }

    /**
     * Returns ItemId
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) { return 0; }

    /**
     * Creating an ImageView for every image, to load into the GridView
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;
        if(convertView == null){
            iv = new ImageView(context);
            iv.setLayoutParams(new GridView.LayoutParams(300, 300));
            iv.setPadding(5,5,5,5);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else {
            iv = (ImageView) convertView;
        }
        iv.setImageResource(bilderIDs[position]);
        return iv;
    }
}
