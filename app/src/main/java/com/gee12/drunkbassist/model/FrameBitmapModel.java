package com.gee12.drunkbassist.model;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by Иван on 19.02.2015.
 */
public class FrameBitmapModel extends BitmapModel {

    protected Bitmap[] frames;
    protected int curFrame;

    public FrameBitmapModel(Bitmap[] frames) {
        init(frames, frames[0].getWidth(), frames[0].getHeight(), new PointF(), 0);
    }

    public FrameBitmapModel(Bitmap[] frames, PointF pos) {
        super(frames[0], frames[0].getWidth(), frames[0].getHeight(), pos, 0);
        init(frames, frames[0].getWidth(), frames[0].getHeight(), pos, 0);
    }

    public FrameBitmapModel(Bitmap[] frames, int destWidth, int destHeight) {
        init(frames, destWidth, destHeight, new PointF(), 0);
    }

    public FrameBitmapModel(Bitmap[] frames, int destWidth, int destHeight, PointF pos) {
        init(frames, destWidth, destHeight, pos, 0);
    }

    public FrameBitmapModel(Bitmap[] frames, int destWidth, int destHeight, int msec) {
        init(frames, destWidth, destHeight, new PointF(), msec);
    }

    public FrameBitmapModel(Bitmap[] frames, int destWidth, int destHeight, PointF pos, int msec) {
        init(frames, destWidth, destHeight, pos, msec);
    }

    public void init(Bitmap[] frames, int destWidth, int destHeight, PointF pos, int msec) {
        this.frames = frames;
        this.curFrame = 0;
//        super.init(frames[curFrame], destWidth, destHeight, pos, msec);
    }

    public void setCurFrame(int curFrame) {
        if (curFrame < 0 || curFrame >= frames.length) return;
        this.curFrame = curFrame;
        this.bitmap = frames[curFrame];
    }

    /////////////////////////////////////////////////////////////////////////
    //

    public int getCurFrame() {
        return curFrame;
    }

}
