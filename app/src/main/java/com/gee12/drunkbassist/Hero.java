package com.gee12.drunkbassist;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by Иван on 17.02.2015.
 */
public class Hero {

    public static int HERO_WIDTH = 30;
    public static int HERO_HEIGHT = 30;

    public Bitmap bitmap;
    public PointF pos;
    public int degree;
    public int points;

    public Hero() {
        this.bitmap = null;
        this.pos = new PointF();
        this.degree = 0;
        this.points = 0;
    }

    public Hero(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.pos = new PointF();
        this.degree = 0;
        this.points = 0;
    }
}
