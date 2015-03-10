package com.gee12.drunkbassist.sound;

import android.content.Context;
import android.media.SoundPool;

/**
 * Created by Иван on 04.03.2015.
 */
public class Sound {

    //    public static final int LOOPED = 0;
    public static final int DEF_PRIORITY = 1;
    public static final float DEF_VOLUME = 1f;
    public static final float DEF_RATE = 1f;

    protected int resId;
    protected int soundId;
    protected int streamId;
    protected float volume;
    protected float rate;
    protected int duration;

    protected SoundPool soundPool;

    public Sound(SoundPool sp, int resId) {
        init(sp, resId, -1, -1, DEF_VOLUME, DEF_RATE, 0);
    }

    public Sound(SoundPool sp, int resId, float volume) {
        init(sp, resId, -1, -1, volume, DEF_RATE, 0);
    }

    public Sound(SoundPool sp, int resId, float volume, int duration) {
        init(sp, resId, -1, -1, volume, DEF_RATE, duration);
    }

    public Sound(SoundPool sp, int resId, float volume, float rate, int duration) {
        init(sp, resId, -1, -1, volume, rate, duration);
    }

    private void init(SoundPool sp, int resId, int soundId, int streamId, float volume, float rate, int duration) {
        this.soundPool = sp;
        this.resId = resId;
        this.soundId = soundId;
        this.streamId = streamId;
        this.volume = volume;
        this.rate = rate;
        this.duration = duration;
    }

    public void load(Context context) {
        if (context == null || soundPool == null) return;
        this.soundId = soundPool.load(context, resId, DEF_PRIORITY);
    }

    public void load(Context context, SoundPool soundPool) {
        if (context == null || soundPool == null) return;
        this.soundId = soundPool.load(context, resId, DEF_PRIORITY);
    }

    public void play(SoundPool soundPool) {
        if (soundPool != null && soundId > 0) {
            this.streamId = soundPool.play(soundId, volume, volume, DEF_PRIORITY, 0, rate);
        }
    }

    public void play() {
        if (soundPool != null && soundId > 0) {
            this.streamId = soundPool.play(soundId, volume, volume, DEF_PRIORITY, 0, rate);
        }
    }

    public void play(float rate) {
        if (soundPool != null && soundId > 0) {
            this.rate = rate;
            this.streamId = soundPool.play(soundId, volume, volume, DEF_PRIORITY, 0, rate);
        }
    }

    public void play(SoundPool soundPool, float rate) {
        if (soundPool != null && soundId > 0) {
            this.rate = rate;
            this.streamId = soundPool.play(soundId, volume, volume, DEF_PRIORITY, 0, rate);
        }
    }

    public void setRate(float rate) {
        if (soundPool != null && streamId > 0) {
            this.rate = rate;
            soundPool.setRate(streamId, rate);
        }
    }

    public void setRate(SoundPool soundPool, float rate) {
        if (soundPool != null && streamId > 0) {
            this.rate = rate;
            soundPool.setRate(streamId, rate);
        }
    }

    public void offsetRate(float offset) {
        if (soundPool != null && streamId > 0) {
            this.rate += offset;
            soundPool.setRate(streamId, rate);
        }
    }

    public void offsetRate(SoundPool soundPool, float offset) {
        if (soundPool != null && streamId > 0) {
            this.rate += offset;
            soundPool.setRate(streamId, rate);
        }
    }

    public void pause() {
        if (soundPool != null && streamId > 0) {
            soundPool.pause(streamId);
        }
    }

    public void pause(SoundPool soundPool) {
        if (soundPool != null && streamId > 0) {
            soundPool.pause(streamId);
        }
    }

    public void resume() {
        if (soundPool != null && streamId > 0) {
            soundPool.resume(streamId);
        }
    }

    public void resume(SoundPool soundPool) {
        if (soundPool != null && streamId > 0) {
            soundPool.resume(streamId);
        }
    }

    public void stop() {
        if (soundPool != null && streamId > 0) {
            soundPool.stop(streamId);
        }
    }

    public void stop(SoundPool soundPool) {
        if (soundPool != null && streamId > 0) {
            soundPool.stop(streamId);
        }
    }

    public void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
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

//    public void setRate(float rate) {
//        this.rate = rate;
//    }

//    public void offsetRate(float offset) {
//        this.rate += offset;
//    }

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
