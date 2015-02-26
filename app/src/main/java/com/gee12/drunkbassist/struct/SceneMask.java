package com.gee12.drunkbassist.struct;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;

/**
 * Created by Иван on 19.02.2015.
 */
public class SceneMask extends Scene {

    public enum HitStatus {
        IN_SCENE(Color.GREEN),
        AT_THE_EDGE(Color.RED),
        OUT_FROM_SCENE(Color.BLACK),
        OUT_FROM_SCREEN(-2),
        NONE(-1);

        private int color;

        HitStatus(int col) {
            this.color = col;
        }

        public int getColor() {
            return color;
        }

        public static HitStatus valueOf(int col) {
            for (HitStatus status : values()) {
                if (status.getColor() == col)
                    return status;
            }
            return NONE;
        }
    }

    public SceneMask() {
        super();
    }

    public SceneMask(Bitmap bitmap) {
        super(bitmap);
    }

    public SceneMask(Bitmap bitmap, int destWidth, int destHeight) {
        // scale mask bitmap to screen dimension
        super(Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false), destWidth, destHeight);
    }

    public HitStatus getHitStatus(PointF pos) {
        if (pos == null)
            return HitStatus.NONE;
        int x = (int)(pos.x + 0.5);
        int y = (int)(pos.y + 0.5);
        if (x < 0 || y < 0 || x >= bitmap.getWidth() || y >= bitmap.getHeight())
            return HitStatus.OUT_FROM_SCREEN;

        int pixel = bitmap.getPixel(x, y);
        return HitStatus.valueOf(pixel);
    }

}
