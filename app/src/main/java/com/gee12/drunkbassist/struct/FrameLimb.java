package com.gee12.drunkbassist.struct;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by Иван on 01.03.2015.
 */
public class FrameLimb extends FrameBitmapModel {

    protected PointF pivot;

    public FrameLimb(Bitmap[] frames, PointF pos) {
        super(frames, pos);
    }

    public void init(PointF pivot) {
        this.pivot = pivot;
    }

}
