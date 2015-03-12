package com.gee12.drunkbassist;

import android.os.Handler;

import com.gee12.drunkbassist.sound.Sound;
import com.gee12.drunkbassist.sound.SoundManager;
import com.gee12.drunkbassist.sound.TimerSound;
import com.gee12.drunkbassist.struct.Food;
import com.gee12.drunkbassist.struct.SceneMask;

import java.util.Random;
import java.util.TimerTask;

/**
 * Created by Иван on 02.03.2015.
 */
public class GameTimerTask extends TimerTask {

    public final static int DELAY_BEFORE_FINISH = 2000;
    public final static int RANDOM_SOUND_DELAY_MIN = 5000;
    public final static int RANDOM_SOUND_DELAY_MAX = 15000;


    private GameListener gameListener;
    private long pauseTime;
    private long tempTime;
    private boolean isRunning;
    private int viewWidth;
    private Handler handler;

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
        if (MainActivity.getInstance() == null) return;

        MainActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                ModelsManager.Hero.onTouchOffset();
                onHeroPositionStatus();
//                ModelsManager.Hero.onHeroStand();
                ModelsManager.Hero.onHeroDrinking(pauseTime);
                ModelsManager.Hero.onHeroEating(pauseTime);
                onRandomSounds(gameTime, pauseTime);

                // animation
                ModelsManager.getCurDrink().onAnimate(gameTime, pauseTime);
                ModelsManager.getCurFood().onAnimate(gameTime, pauseTime);
//                IndicatorsManager.PointsInc.onAnimate(gameTime);
//                IndicatorsManager.DegreeInc.onAnimate(gameTime);
            }
        });
    }

    public void onHeroPositionStatus() {
        SceneMask.PositionStatus status = ModelsManager.Mask.getHitStatus(ModelsManager.Hero.getAbsPivotPoint());
        switch(status) {
            case ON_SCENE:
                ModelsManager.Hero.heroOnScene();
                break;

            case AT_THE_EDGE:
                ModelsManager.Hero.heroAtTheEdge(viewWidth);
                break;

            case OUT_FROM_SCENE:

                ModelsManager.Hero.heroOutOfScene(viewWidth);
                // finish game after delay
                handler.postDelayed(finishGame, DELAY_BEFORE_FINISH);

                break;
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

    public void onRandomSounds(long gameTime, long pauseTime) {
        int degree = IndicatorsManager.Degree.getValue();
        int degreeRound = (int)(degree / 10) * 10;
        switch (degreeRound) {
            case 200:
                setTimerSound(SoundManager.IiiSound, pauseTime);
            case 150:
                setTimerSound(SoundManager.HicSound, pauseTime);
            case 100:
                setTimerSound(SoundManager.BunchSound, pauseTime);
            case 50:
                setTimerSound(SoundManager.BurpSound, pauseTime);
        }
        for (Sound sound : SoundManager.getSounds()) {
            if (sound instanceof TimerSound) {
                TimerSound timerSound = (TimerSound)sound;
                if (timerSound.isNeedToPlay()) {
                    timerSound.onPlay(gameTime);
                }
            }
        }
    }

    public void setTimerSound(TimerSound timerSound, long pauseTime) {
        if (!timerSound.isNeedToPlay()) {
            // play right now
            timerSound.play();
            // and play after random delay every time
            int msec = new Random().nextInt(RANDOM_SOUND_DELAY_MAX - RANDOM_SOUND_DELAY_MIN) + RANDOM_SOUND_DELAY_MIN;
            timerSound.setTimer(msec, pauseTime);
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
