package com.gee12.drunkbassist;

import com.gee12.drunkbassist.struct.Food;
import com.gee12.drunkbassist.struct.SceneMask;

import java.util.TimerTask;

/**
 * Created by Иван on 02.03.2015.
 */
class GameTimerTask extends TimerTask {

    public final static int SCALE_DELTA = 1000;

    private GameListener gameListener;
    private long pauseTime;
    private long tempTime;
    private boolean isRunning;

    long seconds = 0;
    long fps = 0;
    long old_fps = 0;

    public GameTimerTask(GameListener listener) {
        this.gameListener = listener;
        isRunning = true;
        pauseTime = 0;
        tempTime = 0;
        Food.setFoodDisplay(pauseTime, false);
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
//                DrawView.text = String.format("%d", old_fps);

                long gameTime = System.currentTimeMillis() - pauseTime;

                //
                ModelsManager.Hero.randomHeroOffset();
                onHeroPositionStatus();
                ModelsManager.Hero.onHeroStand();
                ModelsManager.Hero.onHeroDrinking(pauseTime);
                ModelsManager.Hero.onHeroEating(pauseTime);

                // animation
                ModelsManager.getCurDrink().onAnimate(gameTime, pauseTime);
                ModelsManager.getCurFood().onAnimate(gameTime, pauseTime);
                IndicatorsManager.PointsInc.onAnimate(gameTime);
                IndicatorsManager.DegreeInc.onAnimate(gameTime);
//            }
//        });
    }

    public void onHeroPositionStatus() {
        SceneMask.PositionStatus status = ModelsManager.Mask.getHitStatus(ModelsManager.Hero.getAbsPivotPoint());
        switch(status) {
            case ON_SCENE:
                ModelsManager.Hero.heroOnScene();
                break;

            case AT_THE_EDGE:
                int width = MainActivity.getInstance().getWindow().getDecorView().getWidth();
                ModelsManager.Hero.heroAtTheEdge(width);
                break;

            case OUT_FROM_SCENE:
                setRunning(false);
                gameListener.onFinish();
                break;
        }
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

}
