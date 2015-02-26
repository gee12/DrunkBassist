package com.gee12.drunkbassist.struct;

import android.graphics.Bitmap;

/**
 * Created by Иван on 19.02.2015.
 */
public class Food extends BitmapModel {

    public final static int BETWEEN_DELAY_MAX_MSEC = 10000;
    public final static int BETWEEN_DELAY_MIN_MSEC = 5000;

    protected int degree;

    public Food() {
        super();
        this.degree = 0;
        this.msec = 0;
    }

    public Food(Bitmap bitmap, int degree, int msec) {
        super(bitmap);
        this.degree = degree;
        this.msec = msec;
    }

    public Food(Bitmap bitmap, int destWidth, int destHeight, int degree, int msec) {
        super(bitmap, destWidth, destHeight, msec);
        this.degree = degree;
        this.msec = msec;
    }

    public int getDegree() {
        return degree;
    }
}
