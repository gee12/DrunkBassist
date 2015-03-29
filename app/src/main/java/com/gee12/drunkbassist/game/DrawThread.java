package com.gee12.drunkbassist.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by Иван on 01.03.2015.
 */
public class DrawThread extends Thread {

    static final long FPS = 10;
    static final int MS_PER_FRAME = 16; // ~60 fps

    private boolean isRunning = false;
    private SurfaceView view;


    public DrawThread(SurfaceView view) {
        this.view = view;
    }

    @Override
    public void run() {
//        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
//        long seconds = 0;
//        long fps = 0;
//        long old_fps = 0;

        while (isRunning) {
            Canvas canvas = null;
            startTime = System.currentTimeMillis();

//            long temp = System.currentTimeMillis() / 1000;
//            if (temp > seconds) {
//                seconds = temp;
//                old_fps = fps;
//                fps = 0;
//            } else {
//                fps++;
//            }
//            view.text = String.format("%d", old_fps);

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

            try {
                if ((sleepTime = (startTime + MS_PER_FRAME - System.currentTimeMillis())) > 0)
                    sleep(sleepTime);
            } catch (InterruptedException e) {}
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
