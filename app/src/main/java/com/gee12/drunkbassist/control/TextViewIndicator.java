package com.gee12.drunkbassist.control;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by Иван on 12.03.2015.
 */
public class TextViewIndicator extends TextViewOutline implements Animation.AnimationListener {

    protected int value;
    protected String format;
    protected Animation animation;

    public TextViewIndicator(Context context) {
        super(context);
    }

    public TextViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    public TextViewIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(int value, String format, int visibility) {
        init(value, format, new Animation() {}, visibility);
    }

    public void init(int value, String format, Animation anim, int visibility) {
        this.value = value;
        this.format = format;
        this.animation = anim;
        animation.setAnimationListener(this);
        setVisibility(visibility);
    }

    public void startAnimation() {
        startAnimation(animation);
    }

    public void setValue(int value) {
        this.value = value;
        setText(String.format(format, value));
    }

    public void offsetValue(int offset) {
        this.value += offset;
        setText(String.format(format, value));
    }

    public int getValue() {
        return value;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        setVisibility(View.GONE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
