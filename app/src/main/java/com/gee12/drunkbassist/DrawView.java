package com.gee12.drunkbassist;

import android.annotation.TargetApi;
import android.content.Context;
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
@TargetApi(9)
public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
    private float accX=0.f, accY=0.f;
    private Hero hero;
    private RectF sceneOuterRectF,
            sceneInnerRectF,
            heroDestRectF;

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);

        hero = new Hero(
                BitmapFactory.decodeResource(getResources(), R.drawable.crossfire),
                new PointF());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();

        //
        PointF startPos = new PointF(getWidth() / 2, getHeight() / 2);
        hero.pos = startPos;
        //
        heroDestRectF = getGeroDestRectF(startPos);
        //
        sceneOuterRectF = new RectF(20, 20, getWidth() - 20, getHeight() - 20);
        //
        sceneInnerRectF = new RectF(40, 40, getWidth() - 40, getHeight() - 40);
    }

    public void setAccelerometerXY(float x, float y) {
        accX = x;
        accY = y;
    }

    public void setHeroPosition(float x, float y) {
        hero.pos.set(x, y);
        heroDestRectF = getGeroDestRectF(hero.pos);
    }

    public void setHeroOffset(float dx, float dy) {
        hero.pos.offset(dx, dy);
        heroDestRectF = getGeroDestRectF(hero.pos);
    }

    public RectF getGeroDestRectF(PointF pos) {
        return new RectF(pos.x, pos.y,
                pos.x + Hero.HERO_WIDTH, pos.y + Hero.HERO_HEIGHT);
    }

    public void draw(Canvas canvas) {
        if (canvas == null) return;

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);


        // on scane
        onHeroOutOfScene(canvas);

        // hero
        canvas.drawBitmap(hero.bitmap, new Rect(0, 0, hero.bitmap.getWidth(), hero.bitmap.getHeight()),
                heroDestRectF, p);

        canvas.drawText(String.format("x=%1.3f", accX), 10, 10, p);
        canvas.drawText(String.format("y=%1.3f", accY), 10, 30, p);
    }

    public void onHeroOutOfScene(Canvas canvas) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        if (!sceneOuterRectF.contains(heroDestRectF)) {
              canvas.drawColor(Color.RED);
            p.setColor(Color.LTGRAY);
            canvas.drawRect(sceneOuterRectF, p);
            p.setColor(Color.WHITE);
            canvas.drawRect(sceneInnerRectF, p);

        } else if (!sceneInnerRectF.contains(heroDestRectF)) {

            canvas.drawColor(Color.GRAY);
            p.setColor(Color.RED);
            canvas.drawRect(sceneOuterRectF, p);
            p.setColor(Color.WHITE);
            canvas.drawRect(sceneInnerRectF, p);
        } else {
            canvas.drawColor(Color.GRAY);
            p.setColor(Color.LTGRAY);
            canvas.drawRect(sceneOuterRectF, p);
            p.setColor(Color.WHITE);
            canvas.drawRect(sceneInnerRectF, p);
        }
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

        public Rect getCanvasRect() {
            if (canvas == null) return null;
            return canvas.getClipBounds();
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
