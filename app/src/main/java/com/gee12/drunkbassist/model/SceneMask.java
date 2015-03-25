package com.gee12.drunkbassist.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;

/**
 * Created by Иван on 19.02.2015.
 */
public class SceneMask extends BitmapModel {

    public enum PositionStatus {
        ON_SCENE(Color.WHITE),
        AT_THE_EDGE(Color.RED),
        OUT_FROM_SCENE(Color.BLACK),
        OUT_FROM_SCREEN(-2),
        NONE(-1);

        private int color;

        PositionStatus(int col) {
            this.color = col;
        }

        public int getColor() {
            return color;
        }

        public static PositionStatus valueOf(int col) {
            for (PositionStatus status : values()) {
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

    public PositionStatus getHitStatus(PointF pos) {
        if (pos == null)
            return PositionStatus.NONE;
        int x = (int)(pos.x + 0.5);
        int y = (int)(pos.y + 0.5);
        if (x < 0 || y < 0 || x >= bitmap.getWidth() || y >= bitmap.getHeight())
            return PositionStatus.OUT_FROM_SCREEN;

        int pixel = bitmap.getPixel(x, y);
        return PositionStatus.valueOf(pixel);
    }

}
