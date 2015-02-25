package com.gee12.drunkbassist;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Иван on 24.02.2015.
 */
public abstract class Model {

    protected PointF pos;
    protected PointF offsetStep;
    protected boolean isVisible;
    protected Paint paint;
    protected int msec;
    protected long startTime;

    /////////////////////////////////////////////////////////////////////////
    //

    public abstract void drawModel(Canvas canvas);

    /////////////////////////////////////////////////////////////////////////
    // set

    public void setPosition(PointF pos) {
        this.pos = pos;
    }

    public void offset(float dx, float dy) {
        pos.offset(dx, dy);
    }

    public void offset() {
        pos.offset(offsetStep.x, offsetStep.y);
    }

    public void setOffsetStep(PointF offsetStep) {
        this.offsetStep = offsetStep;
    }

    public void setOffsetStep(float x, float y) {
        this.offsetStep.set(x, y);
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setMsec(int msec) {
        this.msec = msec;
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    /////////////////////////////////////////////////////////////////////////
    // get

    public PointF getPosition() {
        return pos;
    }

    public PointF getPos() {
        return pos;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getMsec() {
        return msec;
    }

    public long getStartTime() {
        return startTime;
    }

    public PointF getOffsetStep() {
        return offsetStep;
    }
}
