package com.gee12.drunkbassist.sound;

/**
 * Created by Иван on 04.03.2015.
 */
public class Sound {

    private int resId;
    private int soundId;
    private int streamId;
    private float volume;
    private float rate;
    private int duration;

    public Sound(int resId) {
        init(resId, -1, -1, SoundManager.DEF_VOLUME, SoundManager.DEF_RATE, 0);
    }

    public Sound(int resId, float volume, int duration) {
        init(resId, -1, -1, volume, SoundManager.DEF_RATE, duration);
    }

    public Sound(int resId, float volume, float rate, int duration) {
        init(resId, -1, -1, volume, rate, duration);
    }

    private void init(int resId, int soundId, int streamId, float volume, float rate, int duration) {
        this.resId = resId;
        this.soundId = soundId;
        this.streamId = streamId;
        this.volume = volume;
        this.rate = rate;
        this.duration = duration;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void offsetRate(float offset) {
        this.rate += offset;
    }
    public int getSoundId() {
        return soundId;
    }

    public int getStreamId() {
        return streamId;
    }

    public int getResId() {
        return resId;
    }

    public float getRate() {
        return rate;
    }

    public int getDuration() {
        return duration;
    }

    public float getVolume() {
        return volume;
    }
}
