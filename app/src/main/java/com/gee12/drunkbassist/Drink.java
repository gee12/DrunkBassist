package com.gee12.drunkbassist;

import android.graphics.Bitmap;

/**
 * Created by Иван on 19.02.2015.
 */
public class Drink extends BitmapModel {

    protected int degree;
    protected int points;

    public Drink() {
        super();
        this.degree = 0;
        this.points = 0;
    }

    public Drink(Bitmap bitmap, int degree, int points, int msec) {
        super(bitmap);
        this.degree = degree;
        this.points = points;
        this.msec = msec;
    }

    public Drink(Bitmap bitmap, int destWidth, int destHeight, int degree, int points, int msec) {
        super(bitmap, destWidth, destHeight, msec);
        this.degree = degree;
        this.points = points;
    }

    public int getDegree() {
        return degree;
    }

    public int getPoints() {
        return points;
    }

}
