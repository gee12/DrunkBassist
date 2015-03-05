package com.gee12.drunkbassist.sound;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Иван on 04.03.2015.
 */
public class MediaPlayerTrack {
    private MediaPlayer track;
    private String name;

    /**
     * Конструктор
     *
     * @param contex - контекст
     * @param resId - идентификатор ресурса
     * @param name - имя трека
     * @param looped - зацикленность проигрывания
     */
    public MediaPlayerTrack(Context contex, int resId, String name, boolean looped) {
        this.track = MediaPlayer.create(contex, resId);
        this.track.setLooping(looped);
        this.name = name;
    }

    /**
     * запускает трек если в настройках mSettings есть звук
     */
    public void start() {
        if (!track.isPlaying())
            track.start();
        else track.pause();
    }

    /**
     * перезапускает звук независимо от того играется он в данный момент или нет
     */
    public void forceStart() {
        if (track.isPlaying()) {
            track.pause();
            track.seekTo(0);
            track.start();
        } else {
            track.start();
        }
    }

    /**
     * приостанавливает проигрывание звука! ВНИМАНИЕ!!! Не высвобождает память,
     * не отсоединяется от потока... просто приостанавливает!
     */
    public void pause() {
        if (track.isPlaying())
            track.pause();
    }

    /**
     * перематывает трек на начало
     */
    public void rewind() {
        this.pause();
        track.seekTo(0);
        this.start();
    }

    public String getName() {
        return name;
    }

}
