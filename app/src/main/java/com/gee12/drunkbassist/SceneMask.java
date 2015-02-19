package com.gee12.drunkbassist;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;

/**
 * Created by Иван on 19.02.2015.
 */
public class SceneMask extends Scene {

    public enum HitStatus {
        IN_SCENE(Color.BLACK),
        AT_THE_EDGE(Color.RED),
        OUT_FROM_SCENE(Color.WHITE);

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
            return null;
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
        this.bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        init(bitmap, destWidth, destHeight, new PointF());
    }

    public HitStatus hitStatus(PointF heroPos) {
        if (heroPos == null) return HitStatus.OUT_FROM_SCENE;
        int pixel = bitmap.getPixel((int)(heroPos.x + 0.5), (int)(heroPos.y + 0.5));
        return HitStatus.valueOf(pixel);
    }

}
