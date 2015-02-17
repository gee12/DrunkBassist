package com.gee12.drunkbassist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by Иван on 16.02.2015.
 */
public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
    private float accX=0.f, accY=0.f;
    Hero hero;

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);

        hero = new Hero(BitmapFactory.decodeResource(getResources(), R.drawable.crossfire),
                new PointF());
    }

    public void setAccelerometerXY(float x, float y) {
        accX = x;
        accY = y;
    }

    public void setHeroPosition(float x, float y) {
        hero.pos.set(x, y);
    }

    public void setHeroShift(float dx, float dy) {
        hero.pos.offset(dx, dy);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
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

    
    public void draw(Canvas canvas) {
        if (canvas == null) return;

        canvas.drawColor(Color.GRAY);

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);

        float cx = canvas.getWidth()/2;
        float cy = canvas.getHeight()/2;
        RectF dest = new RectF();
        dest.set(cx + hero.pos.x, cy + hero.pos.y,
                cx + hero.pos.x+30, cy + hero.pos.y+30);

        RectF scene = new RectF();
        scene.set(20, 20, canvas.getWidth()-20, canvas.getHeight()-20);

        if (!scene.contains(dest)) {
            canvas.drawColor(Color.RED);
        }

        canvas.drawBitmap(hero.bitmap, new Rect(0, 0, hero.bitmap.getWidth(), hero.bitmap.getHeight()),
                dest, p);

        canvas.drawText(String.format("x=%1.3f", accX), 10, 10, p);
        canvas.drawText(String.format("y=%1.3f", accY), 10, 50, p);
    }

    public class Hero {
        Bitmap bitmap;
        PointF pos;

        public Hero(Bitmap bitmap, PointF pos) {
            this.bitmap = bitmap;
            this.pos = pos;
        }
    }

    public class DrawThread extends Thread {

        private boolean running = false;
        private SurfaceHolder surfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (running) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null)
                        continue;

                    // DRAW
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
