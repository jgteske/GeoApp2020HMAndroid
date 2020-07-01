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
    private Bitmap pointBitmap;
    Paint iconPaint;

    private int touchX;
    private int touchY;

    public TouchViewModel(Context c) {
        super(c);

        // loads background image
        this.setBackgroundResource(R.raw.image_01);

        // loads point bitmap
        Resources resources = getResources();
        pointBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_position);

        // antialiasing
        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setFocusable(true);
    }

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
}
