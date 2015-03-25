package com.gee12.drunkbassist.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gee12.drunkbassist.R;

/**
 * Created by Иван on 03.03.2015.
 */
public enum HeroFrames {
//    STAND(HeroStates.STAND, R.drawable.hero),
//    MOVE1(HeroStates.MOVE, R.drawable.hero),
//    MOVE2(HeroStates.MOVE, R.drawable.hero),
//    AT_THE_EDGE1(HeroStates.AT_THE_EDGE, R.drawable.hero),
    AT_THE_EDGE2(HeroStates.AT_THE_EDGE, R.drawable.hero);

    public enum HeroStates {
        STAND,
        MOVE,
        AT_THE_EDGE
    }

    private int id;
    private HeroStates state;
    private Bitmap bitmap;

    HeroFrames(HeroStates state, int id) {
        this.state = state;
        this.id = id;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setBitmap(Resources resources) {
        this.bitmap = BitmapFactory.decodeResource(resources, id);
    }

    public static HeroFrames nextFrame(HeroFrames cur) {
        int next = cur.ordinal() + 1;
        return (next < values().length) ? values()[next] : values()[0];
    }

    public int getId() {
        return id;
    }

    public static HeroFrames valueOf(int resId) {
        for (HeroFrames frame : values()) {
            if (frame.getId() == resId)
                return frame;
        }
        return null;
    }

    public static void setBitmaps(Resources resources) {
        for (HeroFrames frame : values()) {
            frame.setBitmap(resources);
        }
    }
}
