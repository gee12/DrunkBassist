package com.gee12.drunkbassist.sound;

import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;

import com.gee12.drunkbassist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 03.03.2015.
 */
public class SoundManager {

//    public static final int LOOPED = 0;
    public static final int DEF_PRIORITY = 1;
    public static final float DEF_VOLUME = 1f;
    public static final float DEF_RATE = 1f;
    public static final float RATE_CHANGE_STEP = 0.01f;

    protected static SoundPool soundPool;
    protected static int loadedSoundCount = 0;
    protected static Handler h;

    private static List<Sound> sounds;
    public static Sound StartMainSound;
    public static Sound BackMainSound;
    public static Sound BackMenuSound;
    public static Sound DrinkingSound;
    public static Sound EatingSound;
    public static Sound BurpSound;

    /**
     *
     * @param context
     */
    public static void load(final Context context) {
        // create
        sounds = new ArrayList<>();
        float streamVolume = SoundUtility.getStreamVolume(context);
        float backVolume = streamVolume / 3.f;
        sounds.add(StartMainSound = new Sound(R.raw.back_start_main, backVolume,
                SoundUtility.getDuration(context, R.raw.back_start_main)));
        sounds.add(BackMainSound = new Sound(R.raw.back_main, backVolume,
                SoundUtility.getDuration(context, R.raw.back_main)));
        sounds.add(BackMenuSound = new Sound(R.raw.back_menu, backVolume,
                SoundUtility.getDuration(context, R.raw.back_menu)));

        sounds.add(DrinkingSound = new Sound(R.raw.drinking, streamVolume,
                SoundUtility.getDuration(context, R.raw.drinking)));
        sounds.add(EatingSound = new Sound(R.raw.eating, streamVolume,
                SoundUtility.getDuration(context, R.raw.eating)));
        sounds.add(BurpSound = new Sound(R.raw.burp, streamVolume,
                SoundUtility.getDuration(context, R.raw.burp)));

        // load
        h = new Handler();
        soundPool = SoundUtility.createSoundPool(sounds.size());
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    loadedSoundCount++;
                }
//                if (sampleId == StartMainSound.getSoundId()) {
//                    playSound(StartMainSound);
//                    h.postDelayed(loopBackSound, StartMainSound.getDuration());
//                }
                if (sampleId == BackMenuSound.getSoundId()) {
                    playSound(BackMenuSound);
                }
//                if (loadedSoundCount == sounds.size()) {
////                    h.post(loopBackSound);
//                }
            }
        });

        loadSounds(context);
    }

    protected static void loadSounds(Context context) {
        if (soundPool == null || context == null) return;
        for(Sound sound : sounds) {
            sound.setSoundId(soundPool.load(context, sound.getResId(), 0));
        }
    }

    public static void startPlayMainBackSound() {
        StartMainSound.setRate(DEF_RATE);
        playSound(StartMainSound);
        h.postDelayed(loopBackSound, StartMainSound.getDuration());
    }

    public static void resumeMainBackSound() {
        h.post(loopBackSound);
    }


    public static void stopMainBackSound() {
        stopSound(StartMainSound);
        stopSound(BackMainSound);
        h.removeCallbacks(loopBackSound);
    }

    private static Runnable loopBackSound = new Runnable() {
        public void run() {
            playSound(BackMainSound);
            // run again after duration msec
            int duration = (int)(BackMainSound.getDuration() * (2 - BackMainSound.getRate()));
            h.postDelayed(loopBackSound, duration);
//                h.postDelayed(loopBackSound, BackMainSound.getDuration());
        }
    };

    public static void playSound(Sound sound) {
        if (sound.getSoundId() > 0) {
            sound.setStreamId(soundPool.play(sound.getSoundId(), sound.getVolume(), sound.getVolume(), DEF_PRIORITY, 0, sound.getRate()));
        }
    }

    public static void playSound(Sound sound, float rate) {
        if (sound.getSoundId() > 0) {
            sound.setRate(rate);
            sound.setStreamId(soundPool.play(sound.getSoundId(), sound.getVolume(), sound.getVolume(), DEF_PRIORITY, 0, rate));
        }
    }

    public static void setSoundRate(Sound sound, float rate) {
        if (sound.getStreamId() > 0) {
            sound.setRate(rate);
            soundPool.setRate(sound.getStreamId(), rate);
        }
    }

    public static void offsetSoundRate(Sound sound, float offset) {
        if (sound.getStreamId() > 0) {
            sound.offsetRate(offset);
            soundPool.setRate(sound.getStreamId(), sound.getRate());
        }
    }

    public static void pauseSound(Sound sound) {
        if (sound.getSoundId() > 0) {
            soundPool.pause(sound.getStreamId());
        }
    }

    public static void resumeSound(Sound sound) {
        if (sound.getSoundId() > 0) {
            soundPool.resume(sound.getStreamId());
        }
    }

    public static void stopSound(Sound sound) {
        if (sound.getSoundId() > 0) {
            soundPool.stop(sound.getStreamId());
        }
    }

    public static void pauseAll() {
        if (h != null) {
            h.removeCallbacks(loopBackSound);
        }
        if (soundPool != null) {
            soundPool.autoPause();
        }
    }

    public static void stopAll() {
        if (h != null) {
            h.removeCallbacks(loopBackSound);
        }
        if (soundPool != null) {
            for(Sound sound : sounds) {
                soundPool.stop(sound.getStreamId());
            }
        }
    }
    public static void resumeAll() {
        if (h != null) {
            h.post(loopBackSound);
        }
        if (soundPool != null) {
            soundPool.autoResume();
        }
    }


    public static void release() {
        if (h != null) {
            h.removeCallbacks(loopBackSound);
        }
        if (soundPool != null) {
//            stopAll();
            soundPool.release();
        }
    }

}
