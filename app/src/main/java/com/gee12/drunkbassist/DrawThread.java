package com.gee12.drunkbassist;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by Иван on 01.03.2015.
 */
public class DrawThread extends Thread {

//    static final long FPS = 10;

    private boolean isRunning = false;
    private SurfaceView view;


    public DrawThread(SurfaceView view) {
        this.view = view;
    }

    @Override
    public void run() {
//        long ticksPS = 1000 / FPS;
        long startTime = 0;
//        long sleepTime;
//        long seconds = 0;
//        long fps = 0;
//        long old_fps = 0;

        while (isRunning) {
            Canvas canvas = null;

//            long temp = System.currentTimeMillis() / 1000;
//            if (temp > seconds) {
//                seconds = temp;
//                old_fps = fps;
//                fps = 0;
//            } else {
//                fps++;
//            }
//            view.text = String.format("%d", old_fps);
//            startTime = System.currentTimeMillis();

            SurfaceHolder holder = view.getHolder();
            try {
                canvas = holder.lockCanvas(null);
                synchronized (holder) {
                    view.draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            //
//            sleepTime = ticksPS -(System.currentTimeMillis() - startTime);
//            try {
//                if (sleepTime > 0)
//                    sleep(sleepTime);
//                else
//                    sleep(10);
//            } catch (Exception e) {}
        }
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public void onStop() {
        boolean retry = true;
        setRunning(false);
        while (retry) {
            try {
                join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

}
