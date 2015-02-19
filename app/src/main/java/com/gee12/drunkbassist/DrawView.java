package com.gee12.drunkbassist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.gee12.drunkbassist.ModelsManager.*;


/**
 * Created by Иван on 16.02.2015.
 */

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
//    private HeroListener listener;
//    private RectF sceneOuterRectF,
//            sceneInnerRectF;

    public DrawView(Context context, HeroListener listener) {
        super(context);
//        this.listener = listener;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();

        //
        PointF startPos = new PointF(getWidth() / 2, getHeight() / 2);
        hero.setPosition(startPos);
        //
//        sceneOuterRectF = new RectF(20, 20, getWidth() - 20, getHeight() - 20);
        //
//        sceneInnerRectF = new RectF(40, 40, getWidth() - 40, getHeight() - 40);
    }

    public void draw(Canvas canvas) {
        if (canvas == null) return;

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);

        //scene
        ModelsManager.scene.drawModel(canvas, p);
        // drink
        ModelsManager.drink.drawModel(canvas, p);
        // hero
        ModelsManager.hero.drawModel(canvas, p);

        canvas.drawText(String.format("points=%d", hero.getPoints()), 10, 10, p);
        canvas.drawText(String.format("alc=%d", hero.getDegree()), getWidth()-30, 10, p);
    }

//    public void onHeroOutOfScene(Canvas canvas) {
//        Paint p = new Paint();
//        p.setStyle(Paint.Style.FILL);
//        if (!sceneOuterRectF.contains(hero.getDestRectF())) {
//            //
//
//            listener.onFinish();
//
////            canvas.drawColor(Color.RED);
////            p.setColor(Color.LTGRAY);
////            canvas.drawRect(sceneOuterRectF, p);
////            p.setColor(Color.WHITE);
////            canvas.drawRect(sceneInnerRectF, p);
//
//        } else if (!sceneInnerRectF.contains(hero.getDestRectF())) {
//
//            canvas.drawColor(Color.GRAY);
//            p.setColor(Color.RED);
//            canvas.drawRect(sceneOuterRectF, p);
//            p.setColor(Color.WHITE);
//            canvas.drawRect(sceneInnerRectF, p);
//        } else {
//            canvas.drawColor(Color.GRAY);
//            p.setColor(Color.LTGRAY);
//            canvas.drawRect(sceneOuterRectF, p);
//            p.setColor(Color.WHITE);
//            canvas.drawRect(sceneInnerRectF, p);
//        }
//    }

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
