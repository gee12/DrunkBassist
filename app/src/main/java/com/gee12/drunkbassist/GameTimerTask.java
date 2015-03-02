package com.gee12.drunkbassist;

import android.graphics.PointF;

import com.gee12.drunkbassist.struct.BitmapModel;
import com.gee12.drunkbassist.struct.Drink;
import com.gee12.drunkbassist.struct.Food;
import com.gee12.drunkbassist.struct.SceneMask;
import com.gee12.drunkbassist.struct.TextModel;

import java.util.Random;
import java.util.TimerTask;

/**
 * Created by Иван on 02.03.2015.
 */
class GameTimerTask extends TimerTask {

    public final static float POSITIONS_ROUND = 20f;
    public final static float OFFSTEP_STEP_INC = 0.05f;
    public final static int SCALE_DELTA = 500;

    private GameListener gameListener;
    private int randomMoveCounter = 0;
    private float offsetStep = 1 - OFFSTEP_STEP_INC;
    private long pauseTime;
    private long tempTime;
    private long delayBetweenFoodStartTime;
    private int delayBetweenFoods;
    private boolean isFoodDisplay;
    private boolean isRunning;

    long seconds = 0;
    long fps = 0;
    long old_fps = 0;

    public GameTimerTask(GameListener listener) {
        this.gameListener = listener;
        isRunning = true;
        pauseTime = 0;
        tempTime = 0;
        setFoodDisplay(false);
//            nextRandomDrink();
    }

    @Override
    public void run() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
                if (!isRunning) return;

                long temp = System.currentTimeMillis() / 1000;
                if (temp > seconds) {
                    seconds = temp;
                    old_fps = fps;
                    fps = 0;
                } else {
                    fps++;
                }
                DrawView.text = String.format("%d", old_fps);

                long gameTime = System.currentTimeMillis() - pauseTime;

                randomHeroOffset();
                onHeroPositionStatus();
                onHeroDrinking();
                onHeroEating();

                // animation
                onDrinkAnimate(gameTime);
                onFoodAnimate(gameTime);
                onPointsIncAnimate(gameTime);
                onDegreeIncAnimate(gameTime);
//            }
//        });
    }


    public void onDrinkAnimate(long gameTime) {
        Drink curDrink = ModelsManager.getCurDrink();
        if (gameTime - curDrink.getStartTime() >= curDrink.getMsec()) {
            ModelsManager.nextRandomDrink(pauseTime);
        } else {
            IndicatorsManager.Bonus.setValue((int) ((curDrink.getStartTime() + curDrink.getMsec() - gameTime) / 100.));
            curDrink.makeCenterScale();
        }
    }

    public void onFoodAnimate(long gameTime) {
        Food curFood = ModelsManager.getCurFood();
        if (isFoodDisplay && gameTime - curFood.getStartTime() >= curFood.getMsec()) {
            setFoodDisplay(false);
        } else if (!isFoodDisplay && gameTime - delayBetweenFoodStartTime >= delayBetweenFoods) {
            setFoodDisplay(true);
        } else if (isFoodDisplay) {
            curFood.makeCenterScale();
        }
    }

    public void onPointsIncAnimate(long gameTime) {
        TextModel pointsInc = IndicatorsManager.PointsInc;
        if (pointsInc.isVisible()) {
            if (gameTime - pointsInc.getStartTime() >= pointsInc.getMsec()) {
                pointsInc.setVisible(false);
            } else {
                pointsInc.incAccumulation(pointsInc.getStep().x);
                pointsInc.setTextAlpha((int)pointsInc.getAccumulation());
            }
        }
    }

    public void onDegreeIncAnimate(long gameTime) {
        TextModel degreeInc = IndicatorsManager.DegreeInc;
        if (degreeInc.isVisible()) {
            if (gameTime - degreeInc.getStartTime() >= degreeInc.getMsec()) {
                degreeInc.setVisible(false);
            } else {
                degreeInc.incAccumulation(degreeInc.getStep().x);
                degreeInc.setTextAlpha((int)degreeInc.getAccumulation());
            }
        }
    }

    public void randomHeroOffset() {
        // offset Hero position, while counter > 0
        if (randomMoveCounter-- > 0) {
            // add random offset
            PointF step = ModelsManager.Hero.getMatrix().getTransStep();
            setHeroOffset(step.x, step.y);
        } else {
            // Hero alcoholic intoxication (degree)
            int degree = IndicatorsManager.Degree.getValue();
            if (degree > 0) {
                Random rand = new Random();
                // new random delay
                randomMoveCounter = rand.nextInt(degree);
                // new random offset
                ModelsManager.Hero.getMatrix().setTransStep(new PointF(
                        rand.nextFloat() * offsetStep - offsetStep / 2.f,
                        rand.nextFloat() * offsetStep - offsetStep / 2.f));
            }
        }
    }

    public void setHeroOffset(float dx, float dy) {
        ModelsManager.Hero.offsetPosition(dx, dy);

        // skew
//        ModelsManager.Hero.offsetSkew(-dx/20.0f, 0);
    }

    public void onHeroPositionStatus() {
        SceneMask.HitStatus status = ModelsManager.Mask.getHitStatus(ModelsManager.Hero.getAbsPivotPoint());
        switch(status) {
            case IN_SCENE:
                break;

            case AT_THE_EDGE:
                break;

            case OUT_FROM_SCENE:
                setRunning(false);
                gameListener.onFinish();
                break;
        }
    }

    public void onHeroDrinking() {
        Drink drink = ModelsManager.getCurDrink();
        // if (Hero.pos ~= Drink.pos)
        if (BitmapModel.isSamePositions(ModelsManager.Hero.getAbsPivotPoint(), drink.getCenter(), POSITIONS_ROUND)) {
            // Hero drinking !!
            int pointsInc = drink.getPoints() + IndicatorsManager.Bonus.getValue();
            int degreeInc = drink.getDegree();
            IndicatorsManager.Points.addValue(pointsInc);
            IndicatorsManager.Degree.addValue(degreeInc);
            offsetStep += OFFSTEP_STEP_INC;
            //
            IndicatorsManager.PointsInc.initBeforeDisplay(pointsInc, pauseTime);
            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc, pauseTime);
            //
            ModelsManager.nextRandomDrink(pauseTime);
        }
    }

    public void onHeroEating() {
        Food food = ModelsManager.getCurFood();
        // if (Hero.pos ~= Food.pos)
        if (isFoodDisplay
                && BitmapModel.isSamePositions(ModelsManager.Hero.getAbsPivotPoint(), food.getCenter(), POSITIONS_ROUND)) {
            // Hero eating !!
            int degreeInc = food.getDegree();
            IndicatorsManager.Degree.addValue(degreeInc);
            offsetStep -= OFFSTEP_STEP_INC;
            //
            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc, pauseTime);

            setFoodDisplay(false);
        }
    }

    public void setFoodDisplay(boolean isFoodNeedToDisplay) {
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

    public void setRandomDelayBetweenFoods() {
        int interval = Food.BETWEEN_DELAY_MAX_MSEC - Food.BETWEEN_DELAY_MIN_MSEC;
        delayBetweenFoods = (interval > 0)
                ? new Random().nextInt(interval) + Food.BETWEEN_DELAY_MIN_MSEC
                : Food.BETWEEN_DELAY_MIN_MSEC;
    }

    public void setRunning(boolean isRun) {
        this.isRunning = isRun;
        if (!isRun) {
            tempTime = System.currentTimeMillis();
        } else if (isRun && tempTime != 0) {
            tempTime = System.currentTimeMillis() - tempTime;
            pauseTime += tempTime;
            tempTime = 0;
        }
    }

    public boolean isFoodDisplay() {
        return isFoodDisplay;
    }

    public long getPauseTime() {
        return pauseTime;
    }
}
