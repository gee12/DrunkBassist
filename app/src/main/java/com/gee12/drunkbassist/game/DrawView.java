package com.gee12.drunkbassist.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gee12.drunkbassist.mng.ModelsManager;


/**
 * Created by Иван on 16.02.2015.
 */

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public DrawThread drawThread;
    public static String text = "";

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (drawThread != null) {
            drawThread.onStop();
        }
        drawThread = new DrawThread(this);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null || !ModelsManager.isLoaded()) return;

        //scene
        ModelsManager.Scene.drawModel(canvas);
        // drink
        ModelsManager.getCurDrink().drawModel(canvas);
        // food
        ModelsManager.getCurFood().drawModel(canvas);
        // hero
        ModelsManager.Hero.drawModel(canvas);
        // bass
        ModelsManager.Bass.drawModel(canvas);

//        Paint p = new Paint();
//        p.setStyle(Paint.Style.FILL);
//        canvas.drawText(text, getWidth()/2, getHeight()/2, p);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.onStop();
    }
}
