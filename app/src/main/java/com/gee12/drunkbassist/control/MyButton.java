package com.gee12.drunkbassist.control;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.Utils;

/**
 * Created by Иван on 21.03.2015.
 */
public class MyButton extends Button {

//    protected int textColor;
//    protected int strokeColor;
//    protected float strokeWidth;

    protected TextPaint strokePaint;
    protected StaticLayout strokeLayout, fillLayout;

    public MyButton(Context context) {
        super(context);
        init();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttributes(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttributes(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initAttributes(context, attrs);
    }

    protected void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyFont, 0, 0);
        try {
//            strokeColor = ta.getColor(R.styleable.MyFont_strokeColor, getTextColors().getDefaultColor());
//            strokeWidth = ta.getFloat(R.styleable.MyFont_strokeWidth, 0f);
//            String customFont = ta.getString(R.styleable.MyFont_customFont);
//            setCustomFont(context, customFont);
            strokePaint.setColor(ta.getColor(R.styleable.MyFont_strokeColor, Color.BLACK));
            strokePaint.setStrokeWidth(ta.getFloat(R.styleable.MyFont_strokeWidth, 0f));
//            setTypeface(context, ta.getString(R.styleable.MyFont_customFont));
            setTypeface(Utils.getTypeface(context, ta.getString(R.styleable.MyFont_customFont), Typeface.NORMAL),
                    ta.getInt(R.styleable.MyFont_android_textStyle, Typeface.NORMAL));
        } finally {
            ta.recycle();
        }
    }

    protected void init() {
        strokePaint = new TextPaint();
        strokePaint.setFlags(getPaintFlags());
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setTextSize(getTextSize());
        getPaint().setColor(getCurrentTextColor());

        // for preview window
        if (isInEditMode()) {
            initLayouts(getLayout().getAlignment());
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getLayout() == null) return;
                initLayouts(getLayout().getAlignment());
            }
        });
    }

    protected void initLayouts(Layout.Alignment alignment) {
        String text = getText().toString();
        strokeLayout = new StaticLayout(text, strokePaint,
                getWidth(), alignment, 1, 1, false);
        fillLayout = new StaticLayout(text, getPaint(),
                getWidth(), alignment, 1, 1, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // stroke
//        setTextColor(strokeColor);
//        getPaint().setStrokeWidth(strokeWidth);
//        getPaint().setStyle(Paint.Style.STROKE);
//        super.onDraw(canvas);
//        // text
//        setTextColor(textColor);
//        getPaint().setStrokeWidth(0);
//        getPaint().setStyle(Paint.Style.FILL);
//        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        strokeLayout.draw(canvas);
        fillLayout.draw(canvas);
        canvas.restore();
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

    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
        if (strokePaint != null) {
            strokePaint.setTypeface(tf);
        }
    }

    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(tf, style);
        if (strokePaint != null) {
            strokePaint.setTypeface(Typeface.create(tf, style));
            if ((style & Typeface.ITALIC) != 0) {
                strokePaint.setTextSkewX(-0.25f);
            }
        }
    }

    public void setTextSize(float size) {
        super.setTextSize(size);
        strokePaint.setTextSize(size);
    }

//    public void setCustomTextColor(int textColor) {
//        this.textColor = textColor;
//    }
//
//    public void setStrokeColor(int strokeColor) {
//        this.strokeColor = strokeColor;
//    }
//
//    public void setStrokeWidth(float strokeWidth) {
//        this.strokeWidth = strokeWidth;
//    }

}
