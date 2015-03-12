package com.gee12.drunkbassist.struct;

import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Created by Иван on 12.03.2015.
 */
public class Indicator implements Animation.AnimationListener {

    protected int value;
    protected String format;
    protected TextView view;
    protected Animation animation;

    public Indicator(int value, String format, TextView view) {
        init(value, format, view, new Animation() {});
    }

    public Indicator(int value, String format, TextView view, Animation anim) {
        init(value, format, view, anim);
    }

    protected void init(int value, String format, TextView view, Animation anim) {
        this.value = value;
        this.format = format;
        this.view = view;
        this.animation = anim;
        animation.setAnimationListener(this);
    }

    public void startAnimation() {
        view.startAnimation(animation);
    }

    public void setValue(int value) {
        this.value = value;
        view.setText(String.format(format, value));
    }

    public void offsetValue(int offset) {
        this.value += offset;
        view.setText(String.format(format, value));
    }

    public void setView(TextView view) {
        this.view = view;
    }

    public int getValue() {
        return value;
    }

    public TextView getView() {
        return view;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        view.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
