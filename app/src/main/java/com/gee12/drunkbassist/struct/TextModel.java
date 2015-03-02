package com.gee12.drunkbassist.struct;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Иван on 24.02.2015.
 */
public class TextModel extends Model {

    protected PointF pos;
    protected int value;
    protected String format;
    protected PointF step;
    protected float accumulation;

    public TextModel(int value, String format, PointF pos, Paint p, int msec) {
        init(value, format, pos, p, msec);
    }

    public TextModel(int value, String format, PointF pos, float textSize, int col, int msec) {
        Paint p = new Paint();
        p.setTextSize(textSize);
        p.setColor(col);
        init(value, format, pos, p, msec);
    }

    public void init(int value, String format, PointF pos, Paint p, int msec) {
        this.pos = pos;
        this.value = value;
        this.format = format;
        this.paint = p;
        this.isVisible = true;
        this.msec = msec;
        this.step = new PointF(0, 0);
        this.accumulation = 0;
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
        canvas.drawText(String.format(format, value), pos.x, pos.y, paint);
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
        return divident / (float) (msec);
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
        paint.setAlpha(alpha);
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

    public void addValue(int value) {
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

}
