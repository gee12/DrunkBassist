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
    public static Sound DrinkSound;
    public static Sound FoodSound;
    public static Sound SnoreSound;
    public static Sound Step1Sound;
    public static Sound Step2Sound;
    public static Sound DownSound;
    public static TimerSound BurpSound;
    public static TimerSound HicSound;
    public static TimerSound BunchSound;
    public static TimerSound IiiSound;

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

        sounds.add(DrinkSound = new Sound(soundPool, R.raw.drink, streamVolume * 0.5f));
        sounds.add(DrinkingSound = new Sound(soundPool, R.raw.drinking, streamVolume * 2));
        sounds.add(EatingSound = new Sound(soundPool, R.raw.eating, streamVolume));
        sounds.add(FoodSound = new Sound(soundPool, R.raw.food, streamVolume));
        sounds.add(SnoreSound = new Sound(soundPool, R.raw.snore, streamVolume));
        sounds.add(Step1Sound = new Sound(soundPool, R.raw.step1, streamVolume * 2));
        sounds.add(Step2Sound = new Sound(soundPool, R.raw.step2, streamVolume * 2));
        sounds.add(DownSound = new Sound(soundPool, R.raw.down, streamVolume * 2));

        sounds.add(BurpSound = new TimerSound(soundPool, R.raw.burp, streamVolume * 2));
        sounds.add(HicSound = new TimerSound(soundPool, R.raw.hic, streamVolume * 2));
        sounds.add(BunchSound = new TimerSound(soundPool, R.raw.bunch, streamVolume * 2));
        sounds.add(IiiSound = new TimerSound(soundPool, R.raw.iii, streamVolume * 2));

        // load
        h = new Handler();
        soundPool = SoundUtility.createSoundPool(sounds.size());
        //
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0 && loadedSoundCount++ == sounds.size()) {
                    // all sounds loaded
                }
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
            sound.load(context);
        }
    }

    public static void reinitTimerSounds() {
        for (Sound sound : SoundManager.getSounds()) {
            if (sound instanceof TimerSound) {
                TimerSound timerSound = (TimerSound)sound;
                timerSound.init(0, 0, false);
            }
        }
    }

    public static void startMainBackSound() {
        MainStartBackSound.play(Sound.DEF_RATE);
        MainBackSound.setRate(Sound.DEF_RATE);
        h.postDelayed(loopBackSound, MainStartBackSound.getDuration());
    }

    public static void resumeMainBackSound() {
        h.post(loopBackSound);
    }


    public static void stopMainBackSound() {
        MainStartBackSound.stop();
        MainBackSound.stop();
        h.removeCallbacks(loopBackSound);
    }

    private static Runnable loopBackSound = new Runnable() {
        public void run() {
            MainBackSound.play();
            // run again after duration msec
            int duration = (int)(MainBackSound.getDuration() * (2 - MainBackSound.getRate()));
            h.postDelayed(loopBackSound, duration);
        }
    };

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

    public static List<Sound> getSounds() {
        return sounds;
    }

}
