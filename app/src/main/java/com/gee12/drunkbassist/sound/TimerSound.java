package com.gee12.drunkbassist.sound;

import android.media.SoundPool;

/**
 * Created by Иван on 04.03.2015.
 */
public class TimerSound extends Sound {

    protected int msec;
    protected long startTime;
    protected boolean isNeedToPlay;

    public TimerSound(SoundPool sp, int resId) {
        super(sp, resId);
        init(0, 0, false);
    }

    public TimerSound(SoundPool sp, int resId, float volume) {
        super(sp, resId, volume);
        init(0, 0, false);
    }

    public TimerSound(SoundPool sp, int resId, float volume, int duration) {
        super(sp, resId, volume, duration);
        init(0, 0, false);
    }

    public TimerSound(SoundPool sp, int resId, float volume, float rate, int duration) {
        super(sp, resId, volume, rate, duration);
        init(0, 0, false);
    }

    public void init(int msec, long startTime, boolean isNeedToPlay) {
        this.msec = msec;
        this.startTime = startTime;
        this.isNeedToPlay = false;
    }

    public void setTimer(int msec, long pauseTime) {
        this.msec = msec;
        setStartTime(pauseTime);
        this.isNeedToPlay = true;
    }

//    public void onPlay(long gameTime) {
//        if (isNeedToPlay && gameTime - startTime >= msec) {
//            play();
//            isNeedToPlay = false;
//        }
//    }

    public boolean onPlay(long gameTime) {
        if (isNeedToPlay) {
            if (gameTime - startTime >= msec) {
                play();
                return true;
                // and play after random delay every time
//                int msec = new Random().nextInt(RANDOM_SOUND_DELAY_MAX - RANDOM_SOUND_DELAY_MIN) + RANDOM_SOUND_DELAY_MIN;
//                setTimer(msec, pauseTime);
            }
        }
        return false;
    }

    public void setNeedToPlay(boolean isNeedToPlay) {
        this.isNeedToPlay = isNeedToPlay;
    }

    public void setMsec(int msec) {
        this.msec = msec;
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    public void setStartTime(long pauseTime) {
        this.startTime = System.currentTimeMillis() - pauseTime;
    }

    /////////////////////////////////////////////////////////////////////////
    // get

    public int getMsec() {
        return msec;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isNeedToPlay() {
        return isNeedToPlay;
    }

}
