package com.gee12.drunkbassist.struct;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.gee12.drunkbassist.R;

/**
 * Created by Иван on 12.03.2015.
 */
public class AnimTextView extends TextView implements Animation.AnimationListener {

    protected int value;
    protected String format;
    protected Animation animation;
    protected int textColor;
    protected int strokeColor;
    protected float strokeWidth;

    public AnimTextView(Context context) {
        super(context);
    }

    public AnimTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributes(context, attrs);
    }

    protected void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimTextView, 0, 0);
        try {
            textColor = ta.getColor(R.styleable.AnimTextView_textColor, getTextColors().getDefaultColor());
            strokeColor = ta.getColor(R.styleable.AnimTextView_strokeColor, getTextColors().getDefaultColor());
            strokeWidth = ta.getFloat(R.styleable.AnimTextView_strokeWidth, 0f);
        } finally {
            ta.recycle();
        }
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

    @Override
    protected void onDraw(Canvas canvas) {
        // stroke
        setTextColor(strokeColor);
        getPaint().setStrokeWidth(strokeWidth);
        getPaint().setStyle(Paint.Style.STROKE);
        super.onDraw(canvas);
        // text
        setTextColor(textColor);
        getPaint().setStrokeWidth(0);
        getPaint().setStyle(Paint.Style.FILL);
        super.onDraw(canvas);
    }

    public void startAnimation() {
        startAnimation(animation);
    }

//    @Override
    public void setCustomTextColor(int textColor) {
//        super.setTextColor(textColor);
        this.textColor = textColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
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
