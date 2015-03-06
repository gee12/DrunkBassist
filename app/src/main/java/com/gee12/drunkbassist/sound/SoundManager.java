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

    public static final float RATE_CHANGE_STEP = 0.01f;

    protected static SoundPool soundPool = null;
    protected static int loadedSoundCount = 0;
    protected static Handler h;

    private static List<Sound> sounds;
    public static Sound MainStartBackSound;
    public static Sound MainBackSound;
    public static Sound MenuBackSound;
    public static Sound DrinkingSound;
    public static Sound EatingSound;
    public static TimerSound BurpSound;
    public static Sound DrinkSound;
    public static Sound FoodSound;
    public static Sound SnoreSound;

    /**
     *
     * @param context
     */
    public static void load(final Context context) {
        // create
        float streamVolume = SoundUtility.getStreamVolume(context);
        float backVolume = streamVolume * 0.5f;
        sounds = new ArrayList<>();
        sounds.add(MainStartBackSound = new Sound(soundPool, R.raw.main_start_back, backVolume,
                SoundUtility.getDuration(context, R.raw.main_start_back)));
        sounds.add(MainBackSound = new Sound(soundPool, R.raw.main_back, backVolume,
                SoundUtility.getDuration(context, R.raw.main_back)));
        sounds.add(MenuBackSound = new Sound(soundPool, R.raw.menu_back, backVolume,
                SoundUtility.getDuration(context, R.raw.menu_back)));

        sounds.add(DrinkSound = new Sound(soundPool, R.raw.drink, streamVolume));
        sounds.add(DrinkingSound = new Sound(soundPool, R.raw.drinking, streamVolume*2));
        sounds.add(EatingSound = new Sound(soundPool, R.raw.eating, streamVolume));
        sounds.add(FoodSound = new Sound(soundPool, R.raw.food, streamVolume));
        sounds.add(BurpSound = new TimerSound(soundPool, R.raw.burp, streamVolume));
        sounds.add(SnoreSound = new Sound(soundPool, R.raw.snore, streamVolume));

        // load
        h = new Handler();
        soundPool = SoundUtility.createSoundPool(sounds.size());
        //
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    loadedSoundCount++;
                }
//                if (sampleId == MainStartBackSound.getSoundId()) {
//                    playSound(MainStartBackSound);
//                    h.postDelayed(loopBackSound, MainStartBackSound.getDuration());
//                }
//                if (sampleId == MenuBackSound.getSoundId()) {
//                    MenuBackSound.play();
//                    playSound(MenuBackSound);
                }
//                if (loadedSoundCount == sounds.size()) {
////                    h.post(loopBackSound);
//                }
//            }
        });

        loadSounds(context);
    }

    protected static void loadSounds(Context context) {
        if (context == null) return;
        for(Sound sound : sounds) {
            sound.setSoundPool(soundPool);
//            sound.setSoundId(soundPool.load(context, sound.getResId(), 0));
            sound.load(context);
        }
    }

    public static void startMainBackSound() {
        MainStartBackSound.play(Sound.DEF_RATE);
//        MainStartBackSound.setRate(Sound.DEF_RATE);
//        playSound(MainStartBackSound);
        h.postDelayed(loopBackSound, MainStartBackSound.getDuration());
    }

    public static void resumeMainBackSound() {
        h.post(loopBackSound);
    }


    public static void stopMainBackSound() {
        MainStartBackSound.stop();
        MainBackSound.stop();
//        stopSound(MainStartBackSound);
//        stopSound(MainBackSound);
        h.removeCallbacks(loopBackSound);
    }

    private static Runnable loopBackSound = new Runnable() {
        public void run() {
            MainBackSound.play();
//            playSound(MainBackSound);
            // run again after duration msec
            int duration = (int)(MainBackSound.getDuration() * (2 - MainBackSound.getRate()));
            h.postDelayed(loopBackSound, duration);
//                h.postDelayed(loopBackSound, MainBackSound.getDuration());
        }
    };

//    public static void playSound(Sound sound) {
//        if (sound.getSoundId() > 0) {
//            sound.setStreamId(soundPool.play(sound.getSoundId(), sound.getVolume(), sound.getVolume(), DEF_PRIORITY, 0, sound.getRate()));
//        }
//    }
//
//    public static void playSound(Sound sound, float rate) {
//        if (sound.getSoundId() > 0) {
//            sound.setRate(rate);
//            sound.setStreamId(soundPool.play(sound.getSoundId(), sound.getVolume(), sound.getVolume(), DEF_PRIORITY, 0, rate));
//        }
//    }
//
//    public static void setSoundRate(Sound sound, float rate) {
//        if (sound.getStreamId() > 0) {
//            sound.setRate(rate);
//            soundPool.setRate(sound.getStreamId(), rate);
//        }
//    }
//
//    public static void offsetSoundRate(Sound sound, float offset) {
//        if (sound.getStreamId() > 0) {
//            sound.offsetRate(offset);
//            soundPool.setRate(sound.getStreamId(), sound.getRate());
//        }
//    }
//
//    public static void pauseSound(Sound sound) {
//        if (sound.getSoundId() > 0) {
//            soundPool.pause(sound.getStreamId());
//        }
//    }
//
//    public static void resumeSound(Sound sound) {
//        if (sound.getSoundId() > 0) {
//            soundPool.resume(sound.getStreamId());
//        }
//    }
//
//    public static void stopSound(Sound sound) {
//        if (sound.getSoundId() > 0) {
//            soundPool.stop(sound.getStreamId());
//        }
//    }

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
//                soundPool.stop(sound.getStreamId());
                sound.stop();
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
