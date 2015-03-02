package com.gee12.drunkbassist.struct;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by Иван on 01.03.2015.
 */
public class Limb extends BitmapModel {

    protected PointF pivot;

    public Limb(Bitmap bitmap, PointF pos) {
        super(bitmap, pos);
    }

    public void init(PointF pivot) {
        this.pivot = pivot;
    }


}
