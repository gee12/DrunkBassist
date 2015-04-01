package com.gee12.drunkbassist.game;

import android.os.Handler;

import com.gee12.drunkbassist.Utils;
import com.gee12.drunkbassist.activity.MainActivity;
import com.gee12.drunkbassist.mng.IndicatorsManager;
import com.gee12.drunkbassist.mng.ModelsManager;
import com.gee12.drunkbassist.mng.SoundManager;
import com.gee12.drunkbassist.model.Food;
import com.gee12.drunkbassist.model.Hero;
import com.gee12.drunkbassist.model.SceneMask;
import com.gee12.drunkbassist.sound.TimerSound;

import java.util.TimerTask;

/**
 * Created by Иван on 02.03.2015.
 */
public class GameTimerTask extends TimerTask {

    public static final int TICKS_PER_SECOND = 40;
    public static final int MSEC_PER_TICK = 1000 / TICKS_PER_SECOND;

    public static final int DELAY_BEFORE_FINISH = 2000;
    public static final int RANDOM_SOUND_DELAY_MIN = 10000;
    public static final int RANDOM_SOUND_DELAY_MAX = 15000;


    private boolean isRunning;
    private GameListener gameListener;
    private long pauseTime;
    private long tempTime;
    private int viewWidth;
    private Handler handler;
    private int lastDegree = 0;
    private boolean isHeroFalled = false;

    long seconds = 0;
    long fps = 0;
    long old_fps = 0;

    public GameTimerTask(GameListener listener) {
        this.gameListener = listener;
        this.viewWidth = MainActivity.getInstance().getWindow().getDecorView().getWidth();
        this.handler = new Handler();
        this.isRunning = true;
        this.pauseTime = 0;
        this.tempTime = 0;
        Food.setFoodDisplay(pauseTime, false);
        ModelsManager.nextRandomDrink(pauseTime);
    }

    @Override
    public void run() {
        if (MainActivity.getInstance() == null || !isRunning
                || !ModelsManager.isLoaded()) return;

//        long temp = System.currentTimeMillis() / 1000;
//        if (temp > seconds) {
//            seconds = temp;
//            old_fps = fps;
//            fps = 0;
//        } else {
//            fps++;
//        }
//        DrawView.text = String.format("%d", old_fps);

        final long gameTime = System.currentTimeMillis() - pauseTime;

        //
        ModelsManager.Hero.randomOffset();
        ModelsManager.Hero.onTouchOffset();
        onHeroPositionStatus();
//        ModelsManager.Hero.onHeroStand();
        onHeroDrunkStage(gameTime);
        ModelsManager.Hero.onEyes();

//        IndicatorsManager.PointsInc.onAnimate(gameTime);
//        IndicatorsManager.DegreeInc.onAnimate(gameTime);

        MainActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // animation
                ModelsManager.getCurDrink().onAnimate(gameTime, pauseTime);
                ModelsManager.getCurFood().onAnimate(gameTime, pauseTime);

                ModelsManager.Hero.onDrinking(pauseTime);
                ModelsManager.Hero.onEating(pauseTime);
            }
        });
    }

    public void onHeroPositionStatus() {
        if (!isHeroFalled && ModelsManager.Hero.onPositionStatus(viewWidth) == SceneMask.PositionStatus.OUT_FROM_SCENE) {
            isHeroFalled = true;

            // sound
            SoundManager.DownSound.play();
            SoundManager.SnoreSound.play();
            // finish game after delay
            handler.postDelayed(finishGame, DELAY_BEFORE_FINISH);
        }

        if (isHeroFalled) {
            ModelsManager.onBassFly(viewWidth);
        }
    }

    public void onHeroDrunkStage(long gameTime) {
        int degree = IndicatorsManager.Degree.getValue();
        if (degree != lastDegree) {
            if (degree >= 300) {
                ModelsManager.Hero.onNextDrunkStage(Hero.DrunkStages.DRUNK4);
                SoundManager.IiiSound.setNeedToPlay(true);
            } else if (degree >= 200) {
                ModelsManager.Hero.onNextDrunkStage(Hero.DrunkStages.DRUNK3);
                SoundManager.HicSound.setNeedToPlay(true);
            } else if (degree >= 100) {
                ModelsManager.Hero.onNextDrunkStage(Hero.DrunkStages.DRUNK2);
                SoundManager.BunchSound.setNeedToPlay(true);
            } else if (degree >= 50) {
                ModelsManager.Hero.onNextDrunkStage(Hero.DrunkStages.DRUNK1);
                SoundManager.BurpSound.setNeedToPlay(true);
            } else {
                ModelsManager.Hero.onNextDrunkStage(Hero.DrunkStages.DRUNK0);
            }
            lastDegree = degree;
        }

        onPlayTimerSound(SoundManager.BurpSound, gameTime);
        onPlayTimerSound(SoundManager.BunchSound, gameTime);
        onPlayTimerSound(SoundManager.HicSound, gameTime);
        onPlayTimerSound(SoundManager.IiiSound, gameTime);

    }

    private void onPlayTimerSound(TimerSound timerSound, long gameTime) {
        if (timerSound.onPlay(gameTime)) {
            // and play after random delay every time
            int msec = Utils.Random.nextInt(RANDOM_SOUND_DELAY_MAX - RANDOM_SOUND_DELAY_MIN) + RANDOM_SOUND_DELAY_MIN;
            timerSound.setTimer(msec, pauseTime);
        }
    }

    private Runnable finishGame = new Runnable() {
        @Override
        public void run() {
            setRunning(false);
            gameListener.onFinish();
            handler.removeCallbacks(finishGame);
        }

    };

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
