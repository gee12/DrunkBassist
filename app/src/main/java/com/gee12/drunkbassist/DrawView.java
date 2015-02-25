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

    private DrawThread drawThread;
    public String text = "";

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

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
        canvas.drawText(text, 10, 10, p);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public class DrawThread extends Thread {

        private boolean running = false;
        private SurfaceHolder surfaceHolder;
        Canvas canvas;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while (running) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null)
                        continue;
                    //
                    draw(canvas);
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

}
