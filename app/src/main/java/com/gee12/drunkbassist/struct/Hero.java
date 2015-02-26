package com.gee12.drunkbassist.struct;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by Иван on 17.02.2015.
 */
public class Hero extends BitmapModel {

    public enum HeroFrames {
        STAND(0),
        MOVE1(1),
        MOVE2(2),
        AT_THE_EDGE1(3),
        AT_THE_EDGE2(4);

        private int frame;

        HeroFrames(int col) {
            this.frame = col;
        }

        public int getColor() {
            return frame;
        }

//        public static HeroStatus valueOf(int col) {
//            for (HeroStatus status : values()) {
//                if (status.getColor() == col)
//                    return status;
//            }
//            return null;
//        }
    }

    protected int curFrame;

    public Hero() {
        super();
        init(0);
    }

    public Hero(Bitmap bitmap) {
        super(bitmap);
        init(0);
    }

    public Hero(Bitmap bitmap, PointF pos) {
        super(bitmap, pos);
        init(0);
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight) {
        super(bitmap, destWidth, destHeight);
        init(0);
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        super(bitmap, destWidth, destHeight, pos);
        init(0);
    }

    private void init(int curFrame) {
        this.curFrame = curFrame;
    }

    /////////////////////////////////////////////////////////////////////////
    // set

    public void setCurFrame(int curFrame) {
        this.curFrame = curFrame;
    }


    /////////////////////////////////////////////////////////////////////////
    // get

    public int getCurFrame() {
        return curFrame;
    }
}
