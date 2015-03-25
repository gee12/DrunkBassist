package com.gee12.drunkbassist.model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Иван on 24.02.2015.
 */
public abstract class Model {

    protected boolean isVisible;
    protected Paint paint;
    protected int msec;
    protected long startTime;

    /////////////////////////////////////////////////////////////////////////
    //

    public abstract void drawModel(Canvas canvas);

    /////////////////////////////////////////////////////////////////////////
    // set

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

    public void setStartTime(long pauseTime) {
        this.startTime = System.currentTimeMillis() - pauseTime;
    }

    /////////////////////////////////////////////////////////////////////////
    // get

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

}
