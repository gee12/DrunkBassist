package com.gee12.drunkbassist.sound;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.IOException;

/**
 * Created by Иван on 04.03.2015.
 */
public class SoundUtility {

    /**
     *
     * @param maxStreams
     * @return
     */
    public static SoundPool createSoundPool(int maxStreams) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                ? createNewSoundPool() : createOldSoundPool(maxStreams);
    }

    /**
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static SoundPool createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        return new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    /**
     *
     * @param maxStreams
     * @return
     */
    @SuppressWarnings("deprecation")
    public static SoundPool createOldSoundPool(int maxStreams){
        return new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
    }
    /**
     *
     * @param context
     * @param soundFile sound file object (can be one of: Integer(resource id), String(file path), or File)
     * @return duration of given sound file in millis (0 if failed)
     */
    public static long getDurationOfSound(Context context, Object soundFile)
    {
        int millis = 0;
        MediaPlayer mp = new MediaPlayer();
        try {
            Class<? extends Object> currentArgClass = soundFile.getClass();
            if(currentArgClass == Integer.class) {
                AssetFileDescriptor afd = context.getResources().openRawResourceFd((Integer)soundFile);
                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
            }
            else if(currentArgClass == String.class) {
                mp.setDataSource((String)soundFile);
            }
            else if(currentArgClass == File.class) {
                mp.setDataSource(((File)soundFile).getAbsolutePath());
            }
            mp.prepare();
            millis = mp.getDuration();
        }
        catch(Exception e) {
            //  Logger.e(e.toString());
        }
        finally {
            mp.release();
            mp = null;
        }
        return millis;
    }

    /**
     *
     * @param context
     * @param resId
     * @return
     */
    public static int getDuration(Context context, int resId) {
        MediaPlayer mp = MediaPlayer.create(context, resId);
        int duration = mp.getDuration();
        mp.release();
        return duration;
    }

    /**
     *
     * @param resourcePath - for example "android.resource://<package_name>/raw/<file_name>"
     * @return
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public static int getDuration(String resourcePath) {
        String mediaPath = Uri.parse(resourcePath).getPath();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mediaPath);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Integer.parseInt(durationStr);
    }

    /**
     *
     * @param context
     * @param resourcePath - for example "android.resource://<package_name>/raw/<file_name>"
     * @return
     */
    public static int getDuration(Context context, String resourcePath) {
        MediaPlayer mp = new MediaPlayer();
        AssetFileDescriptor d = null;
        try {
            d = context.getAssets().openFd(resourcePath);
            mp.reset();
            mp.setDataSource(d.getFileDescriptor(), d.getStartOffset(), d.getLength());
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mp.getDuration();
    }

    /**
     *
     * @param context
     * @return
     */
    public static float getStreamVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
