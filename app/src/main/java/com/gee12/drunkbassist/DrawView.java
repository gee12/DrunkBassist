package com.gee12.drunkbassist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


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
        if (canvas == null) return;

        //scene
        ModelsManager.Scene.drawModel(canvas);
        // drink
        ModelsManager.getCurDrink().drawModel(canvas);
        // food
        ModelsManager.getCurFood().drawModel(canvas);
        // hero
        ModelsManager.Hero.drawModel(canvas);
        // indicators
        IndicatorsManager.Points.drawModel(canvas);
        IndicatorsManager.Bonus.drawModel(canvas);
        IndicatorsManager.Degree.drawModel(canvas);

        IndicatorsManager.PointsInc.drawModel(canvas);
        IndicatorsManager.DegreeInc.drawModel(canvas);

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        canvas.drawText(text, getWidth()/2, getHeight()/2, p);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.onStop();
    }
}
