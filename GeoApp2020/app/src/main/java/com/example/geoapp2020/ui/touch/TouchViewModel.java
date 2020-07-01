package com.example.geoapp2020.ui.touch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.geoapp2020.R;


public class TouchViewModel extends View {

    private float xpos = -1;
    private float ypos = -1;
    private Bitmap flowerBitmap;
    Paint iconPaint;

    private int touchX;
    private int touchY;

    public TouchViewModel(Context c) {
        super(c);

        // load background image
        this.setBackgroundResource(R.raw.image_01);

        // load flower bitmap
        Resources resources = getResources();
        flowerBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_position);

        // antialiasing
        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Startposition getWidth() funktioniert nicht in onCreate() oder
        // onStart()
        if (xpos == -1 && ypos == -1) {
            xpos = getWidth() / 2;
            ypos = getHeight() / 2;
        }

        // draw flower bitmap
        if (flowerBitmap != null) {
            canvas.drawBitmap(flowerBitmap, xpos - flowerBitmap.getWidth() / 2,
                    ypos - flowerBitmap.getHeight() / 2, iconPaint);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            ypos -= 5;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            xpos += 5;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            ypos += 5;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            xpos -= 5;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            xpos = getWidth() / 2;
            ypos = getHeight() / 2;
        }

        invalidate();

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int aktion = event.getAction();

        if(aktion == MotionEvent.ACTION_DOWN) {
            touchX = (int) event.getX();
            touchY = (int) event.getY();
        }

        if(aktion == MotionEvent.ACTION_UP) {
            int tx = (int) event.getX();
            int ty = (int) event.getY();

            if( (touchX - tx) > 20) {
                xpos -= 25;
            } else if ((touchX - tx) <= -20){
                xpos += 25;
            }

            if( (touchY - ty) > 20) {
                ypos -= 25;
            } else if( (touchY - ty) <= -20) {
                ypos += 25;
            }
        }

        invalidate();

        return true;
    }
}
