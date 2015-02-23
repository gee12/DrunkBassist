package com.gee12.drunkbassist;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by Иван on 17.02.2015.
 */
public class Hero extends Model {

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
    protected int degree;
    protected int points;

    public Hero() {
        super();
        init(0, 0, 0);
    }

    public Hero(Bitmap bitmap) {
        super(bitmap);
        init(0, 0, 0);
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight) {
        super(bitmap, destWidth, destHeight);
        init(0, 0, 0);
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        super(bitmap, destWidth, destHeight, pos);
        init(0, 0, 0);
    }

    private void init(int curFrame, int degree, int points) {
        this.curFrame = curFrame;
        this.degree = degree;
        this.points = points;
    }

    /////////////////////////////////////////////////////////////////////////

    public void addPoints(int points) {
        this.points += points;
    }

    public void addDegree(int degree) {
        this.degree += degree;
    }

    /////////////////////////////////////////////////////////////////////////
    // set

    public void setCurFrame(int curFrame) {
        this.curFrame = curFrame;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    /////////////////////////////////////////////////////////////////////////
    // get

    public int getCurFrame() {
        return curFrame;
    }

    public int getDegree() {
        return degree;
    }

    public int getPoints() {
        return points;
    }

}
