package com.gee12.drunkbassist;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by Иван on 19.02.2015.
 */
public class Scene extends BitmapModel {


    public Scene() {
        super();
    }

    public Scene(Bitmap bitmap) {
        super(bitmap);
    }

    public Scene(Bitmap bitmap, int destWidth, int destHeight) {
        super(bitmap, destWidth, destHeight);
    }

    public Scene(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        super(bitmap, destWidth, destHeight, pos);
    }

}
