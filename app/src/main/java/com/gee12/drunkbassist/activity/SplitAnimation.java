package com.gee12.drunkbassist.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by Иван on 01.04.2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SplitAnimation {

    public static Bitmap mBmp1 = null;
    public static Bitmap mBmp2 = null;
    private static int[] mLoc1;
    private static int[] mLoc2;
    private static ImageView mTopImage;
    private static ImageView mBottomImage;
    private static AnimatorSet mSetAnim;
    private static int splitYCoord;

    public static void startActivity(Activity currActivity, Intent intent) {
        // Подготовка acticity
        prepare(currActivity);

        currActivity.startActivity(intent);
        // Чистка анимаций текущей activity
        currActivity.overridePendingTransition(0, 0);
    }

    public static void prepareAnimation(final Activity destActivity) {
        mTopImage = createImageView(destActivity, mBmp1, mLoc1);
        mBottomImage = createImageView(destActivity, mBmp2, mLoc2);
    }

    public static void animate(final Activity destActivity, final int duration, final TimeInterpolator interpolator) {

        // Post this on the UI thread's message queue. It's needed for the items to be already measured
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                mSetAnim = new AnimatorSet();
                mTopImage.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                mBottomImage.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                mSetAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        clean(destActivity);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        clean(destActivity);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                // Animating the 2 parts away from each other
                Animator anim1 = ObjectAnimator.ofFloat(mTopImage, "translationY", mTopImage.getHeight() * -1);
                Animator anim2 = ObjectAnimator.ofFloat(mBottomImage, "translationY", mBottomImage.getHeight());

                if (interpolator != null) {
                    anim1.setInterpolator(interpolator);
                    anim2.setInterpolator(interpolator);
                }

                mSetAnim.setDuration(duration);
                mSetAnim.playTogether(anim1, anim2);
                mSetAnim.start();
            }
        });
    }

    public static void animate(final Activity destActivity, final int duration) {
        animate(destActivity, duration, new DecelerateInterpolator());
    }

    public static void cancel() {
        if (mSetAnim != null)
            mSetAnim.cancel();
    }

    private static void clean(Activity activity) {
        if (mTopImage != null) {
            mTopImage.setLayerType(View.LAYER_TYPE_NONE, null);
            try {
                // If we use the regular removeView() we'll get a small UI glitch
                activity.getWindowManager().removeViewImmediate(mBottomImage);
            } catch (Exception ignored) {
            }
        }
        if (mBottomImage != null) {
            mBottomImage.setLayerType(View.LAYER_TYPE_NONE, null);
            try {
                activity.getWindowManager().removeViewImmediate(mTopImage);
            } catch (Exception ignored) {
            }
        }

        mBmp1 = null;
        mBmp2 = null;
    }

    private static void prepare(Activity currActivity) {

        // Получаем content activity и делаем из него bitmap
        View root = currActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        root.setDrawingCacheEnabled(true);
        Bitmap bmp = root.getDrawingCache();

        // Указываем в какой части экрана будет делится activity
        splitYCoord = bmp.getHeight() / 2;

        // Разделяем экран
        mBmp1 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), splitYCoord);
        mBmp2 = Bitmap.createBitmap(bmp, 0, splitYCoord, bmp.getWidth(), bmp.getHeight() - splitYCoord);

        // Указываем координаты положения частей activity
        mLoc1 = new int[]{0, root.getTop()};
        mLoc2 = new int[]{0, root.getTop() + splitYCoord};
    }

    private static ImageView createImageView(Activity destActivity, Bitmap bmp, int loc[]) {
        ImageView imageView = new ImageView(destActivity);
        imageView.setImageBitmap(bmp);

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = loc[0];
        windowParams.y = loc[1];
        windowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;
        destActivity.getWindowManager().addView(imageView, windowParams);

        return imageView;
    }
}
