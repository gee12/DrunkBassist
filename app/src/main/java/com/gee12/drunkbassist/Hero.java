package com.gee12.drunkbassist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

/**
 * Created by Иван on 17.02.2015.
 */
public class Hero {

    public static int HERO_WIDTH = 30;
    public static int HERO_HEIGHT = 30;

    public Bitmap bitmap;
    public PointF pos;

    public Hero() {
        this.bitmap = null;
        this.pos = new PointF();
    }

    public Hero(Bitmap bitmap, PointF pos) {
        this.bitmap = bitmap;
        this.pos = pos;
    }
}
