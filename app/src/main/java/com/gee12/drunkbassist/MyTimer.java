package com.gee12.drunkbassist;

import android.os.CountDownTimer;

/**
 * Created by Иван on 21.02.2015.
 */
public abstract class MyTimer extends CountDownTimer {

    public final static int MSEC_MAX = 60000;

    public MyTimer(long countDownInterval) {
        super(MSEC_MAX, countDownInterval);
    }

    public MyTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public abstract void onTick(long millisUntilFinished);

    @Override
    public void onFinish() {

    }

}
