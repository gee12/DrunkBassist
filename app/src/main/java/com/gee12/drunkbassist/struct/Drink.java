package com.gee12.drunkbassist.struct;

import android.graphics.Bitmap;

import com.gee12.drunkbassist.IndicatorsManager;
import com.gee12.drunkbassist.ModelsManager;

/**
 * Created by Иван on 19.02.2015.
 */
public class Drink extends BitmapModel {

    public static final float SCALE_KOEF = 0.5f;

    protected int degree;
    protected int points;
    protected float scaleKoef = SCALE_KOEF;
    protected boolean isNeedBonus = false;

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


    public static void onDrinkAnimate(long gameTime, long pauseTime) {
        Drink curDrink = ModelsManager.getCurDrink();
        if (gameTime - curDrink.getStartTime() >= curDrink.getMsec()) {
            ModelsManager.nextRandomDrink(pauseTime);
        } else {
            IndicatorsManager.Bonus.setValue((int) ((curDrink.getStartTime() + curDrink.getMsec() - gameTime) / 100.));
            curDrink.makeCenterScale();
        }
    }

    public void onAnimate(long gameTime, long pauseTime) {
        if (gameTime - startTime >= msec) {
//            ModelsManager.nextRandomDrink(pauseTime);

            setScaleStepFromMsec(scaleKoef *= -1);
            setStartTime(pauseTime);
            isNeedBonus = false;
        } else {
            if (isNeedBonus) {
                IndicatorsManager.Bonus.setValue((int) ((startTime + msec - gameTime) / 100.));
            }
            makeCenterScale();
        }
    }

    public void resetFood(long pauseTime) {
        setRandomPositionInScene(ModelsManager.Mask);
        resetDestDimension();
        setScaleStepFromMsec(scaleKoef);
        setStartTime(pauseTime);
        isNeedBonus = true;
    }

}
