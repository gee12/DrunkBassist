package com.gee12.drunkbassist.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;

import com.gee12.drunkbassist.game.GameTimerTask;

/**
 * Created by Иван on 24.02.2015.
 */
public class TextModel extends Model {

    protected PointF pos;
    protected int value;
    protected String format;
    protected PointF step;
    protected float accumulation;
    protected int alpha;

    protected int textColor;
    protected int strokeColor;
    protected float strokeWidth;

    public TextModel(int value, String format, PointF pos, Paint p) {
        init(value, format, pos, p.getColor(), 0, 0, p, 0);
    }

    public TextModel(int value, String format, PointF pos, float textSize, int col) {
        Paint p = new Paint();
        p.setTextSize(textSize);
        p.setAntiAlias(true);
        init(value, format, pos, col, 0, 0, p, 0);
    }

    public TextModel(int value, String format, PointF pos, float textSize, int col,
                     int strokeCol, float strokeWidth, Typeface tf, int msec) {
        Paint p = new Paint();
        p.setTextSize(textSize);
        p.setAntiAlias(true);
        p.setTypeface(tf);
        init(value, format, pos, col, strokeCol, strokeWidth, p, msec);
    }

    public void init(int value, String format, PointF pos, int col,
                     int strokeCol, float strokeWidth, Paint p, int msec) {
        this.pos = pos;
        this.textColor = col;
        this.value = value;
        this.format = format;
        this.paint = p;
        this.isVisible = true;
        this.msec = msec;
        this.step = new PointF(0, 0);
        this.accumulation = 0;
        this.strokeColor = strokeCol;
        this.strokeWidth = strokeWidth;
        this.alpha = 255;
    }

    public void initBeforeDisplay(int value, long pauseTime) {
        setVisible(true);
        setValue(value);
        setStartTime(pauseTime);
        setTextAlpha(255);
        this.step = new PointF(-getStepFromMsec(255), 0);
        this.accumulation = 255;
    }

    /////////////////////////////////////////////////////////////////////////

    @Override
    public void drawModel(Canvas canvas) {
        if (canvas == null || !isVisible) return;

        String s = String.format(format, value);
        // stroke
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        paint.setAlpha(alpha);
        paint.setStrokeMiter(10);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawText(s, pos.x, pos.y, paint);
        // fill
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        paint.setAlpha(alpha);

        canvas.drawText(s, pos.x, pos.y, paint);
    }

    public void setStroke(int col, float width) {
        this.strokeColor = col;
        this.strokeWidth = width;
    }

    public boolean setCustomFont(Context context, String asset, int style) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(context.getAssets(), asset);
        } catch (Exception e) {
            return false;
        }
        paint.setTypeface(Typeface.create(tf, style));
        return true;
    }

    public void setPosition(PointF pos) {
        this.pos = pos;
    }

    public void offset(float dx, float dy) {
        pos.offset(dx, dy);
    }
    public PointF getPosition() {
        return pos;
    }

    public float getStepFromMsec(int divident) {
        return (msec != 0) ? (divident * GameTimerTask.MSEC_PER_TICK) / (float) (msec) : 0;
    }

    public void incAccumulation(float step) {
        this.accumulation += step;
    }

    public void setTextSize(float size) {
        if (size < 0) return;
        this.paint.setTextSize(size);
    }

    /*
    * Set a alpha component [0..255] from a text color.
    */
    public void setTextAlpha(int alpha) {
        if (alpha < 0 || alpha > 255) return;
//        paint.setAlpha(alpha);
        this.alpha = alpha;
    }

    public void offsetTextSize(float dSize) {
        float oldSize = this.paint.getTextSize();
        if (oldSize + dSize < 0) return;
        paint.setTextSize(oldSize + dSize);
    }

    public void offsetTextAlpha(int dAlpha) {
        int oldAlpha = this.paint.getAlpha();
        setTextAlpha(oldAlpha + dAlpha);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void offsetValue(int value) {
        this.value += value;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getValue() {
        return value;
    }

    public String getFormat() {
        return format;
    }

    public float getAccumulation() {
        return accumulation;
    }

    public PointF getStep() {
        return step;
    }

    public void onAnimate(long gameTime) {
        if (isVisible) {
            if (gameTime - startTime >= msec) {
                setVisible(false);
            } else {
                incAccumulation(step.x);
                setTextAlpha((int)accumulation);
            }
        }
    }

}
