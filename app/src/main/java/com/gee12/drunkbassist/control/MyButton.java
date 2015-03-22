package com.gee12.drunkbassist.control;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.gee12.drunkbassist.R;

/**
 * Created by Иван on 21.03.2015.
 */
public class MyButton extends Button {

    protected int textColor;
    protected int strokeColor;
    protected float strokeWidth;

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributes(context, attrs);
    }

    protected void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyFont, 0, 0);
        try {
            textColor = ta.getColor(R.styleable.MyFont_textColor, getTextColors().getDefaultColor());
            strokeColor = ta.getColor(R.styleable.MyFont_strokeColor, getTextColors().getDefaultColor());
            strokeWidth = ta.getFloat(R.styleable.MyFont_strokeWidth, 0f);
            String customFont = ta.getString(R.styleable.MyFont_customFont);
            setCustomFont(context, customFont);
        } finally {
            ta.recycle();
        }
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

    public boolean setCustomFont(Context context, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(context.getAssets(), asset);
        } catch (Exception e) {
            return false;
        }
        setTypeface(tf);
        return true;
    }

    public void setCustomTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

}
