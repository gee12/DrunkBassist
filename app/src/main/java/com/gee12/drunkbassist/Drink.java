package com.gee12.drunkbassist;

import android.graphics.Bitmap;

/**
 * Created by Иван on 19.02.2015.
 */
public class Drink extends Model {

    public final static int DEF_MSEC = 5000;

    protected int degree;
    protected int points;
    protected int msec;

    public Drink() {
        super();
        this.degree = 0;
        this.points = 0;
        this.msec = 0;
    }

    public Drink(Bitmap bitmap, int degree, int points, int msec) {
        super(bitmap);
        this.degree = degree;
        this.points = points;
        this.msec = msec;
    }

    public Drink(Bitmap bitmap, int destWidth, int destHeight, int degree, int points, int msec) {
        super(bitmap, destWidth, destHeight);
        this.degree = degree;
        this.points = points;
        this.msec = msec;
    }

    public int getDegree() {
        return degree;
    }

    public int getPoints() {
        return points;
    }

    public int getMsec() {
        return msec;
    }
}
