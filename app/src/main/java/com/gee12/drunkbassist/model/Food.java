package com.gee12.drunkbassist.model;

import android.graphics.Bitmap;

import com.gee12.drunkbassist.mng.ModelsManager;

import java.util.Random;

/**
 * Created by Иван on 19.02.2015.
 */
public class Food extends BitmapModel {

    public final static int BETWEEN_DELAY_MAX_MSEC = 10000;
    public final static int BETWEEN_DELAY_MIN_MSEC = 5000;
    public final static float SCALE_KOEF = 0.7f;

    private static int delayBetweenFoods;
    private static long delayBetweenFoodStartTime;
    private static boolean isFoodDisplay;

    protected int degree;

    public Food() {
        super();
        this.degree = 0;
        this.msec = 0;
    }

    public Food(Bitmap bitmap, int degree, int msec) {
        super(bitmap);
        this.degree = degree;
        this.msec = msec;
    }

    public Food(Bitmap bitmap, int destWidth, int destHeight, int degree, int msec) {
        super(bitmap, destWidth, destHeight, msec);
        this.degree = degree;
        this.msec = msec;
    }

    public int getDegree() {
        return degree;
    }

    public void onAnimate(long gameTime, long pauseTime) {
//        Food curFood = ModelsManager.getCurFood();
        if (isFoodDisplay && gameTime - startTime >= msec) {
            setFoodDisplay(pauseTime, false);
        } else if (!isFoodDisplay && gameTime - delayBetweenFoodStartTime >= delayBetweenFoods) {
            setFoodDisplay(pauseTime, true);
        } else if (isFoodDisplay) {
            makeCenterScale();
        }
    }

    public static void setFoodDisplay(long pauseTime, boolean isFoodNeedToDisplay) {
        if (!isFoodNeedToDisplay) {
            isFoodDisplay = false;
            delayBetweenFoodStartTime = System.currentTimeMillis();
            setRandomDelayBetweenFoods();
            ModelsManager.getCurFood().setVisible(false);
        } else {
            isFoodDisplay = true;
            ModelsManager.nextRandomFood(pauseTime);
        }
    }

    public static void setRandomDelayBetweenFoods() {
        int interval = Food.BETWEEN_DELAY_MAX_MSEC - Food.BETWEEN_DELAY_MIN_MSEC;
        delayBetweenFoods = (interval > 0)
                ? new Random().nextInt(interval) + Food.BETWEEN_DELAY_MIN_MSEC
                : Food.BETWEEN_DELAY_MIN_MSEC;
    }

    public void resetFood(long pauseTime) {
        setRandomPositionInScene(ModelsManager.Mask);
        resetDestDimension();
        setScaleStepFromMsec(SCALE_KOEF);
        setStartTime(pauseTime);
        setVisible(true);
    }

    public static boolean isFoodDisplay() {
        return isFoodDisplay;
    }
}
