package com.example.geoapp2020.ui.touch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.example.geoapp2020.R;


public class TouchViewModel extends View {

    private float xpos = -1;
    private float ypos = -1;
    private Bitmap pointBitmap;
    Paint iconPaint;

    private int touchX;
    private int touchY;

    public TouchViewModel(Context c) {
        super(c);

        // loads background image
        // moved to the fragment_touch.xml layout to crop the image to the Screen

        // loads point bitmap
        Resources resources = getResources();
        pointBitmap = drawableToBitmap(resources.getDrawable(R.drawable.ic_danger_center));

        // antialiasing
        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setFocusable(true);
    }

    /**
     * Creates the draggable image
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // starting position getWidth() just works in onDraw()
        if (xpos == -1 && ypos == -1) {
            xpos = getWidth() / 2;
            ypos = getHeight() / 2;
        }

        // draw point bitmap
        if (pointBitmap != null) {
            canvas.drawBitmap(pointBitmap, xpos - pointBitmap.getWidth() / 2,
                    ypos - pointBitmap.getHeight() / 2, iconPaint);
        }
    }


    /**
     * Creating an onTouchEvent to put the image on touched location
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                xpos = (int) event.getX();
                ypos = (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xpos = (int) event.getX();
                ypos = (int) event.getY();
                System.out.print("....."+xpos+"...."+ypos);
                invalidate();
                break;
        }
        return true;
    }


    /**
     * CREATE A BITMAP FROM DRAWABLE
     * source: André - https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}
