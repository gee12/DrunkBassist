package com.gee12.drunkbassist;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

import java.util.Random;

/**
 * Created by Иван on 12.03.2015.
 */
public class Utils {

    public static Random Random = new Random();

    /**
     *
     * @param disp
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static DisplayMetrics getNewMetrics(Display disp) {
        if (disp == null) return null;

        DisplayMetrics metrics = new DisplayMetrics();
        disp.getRealMetrics(metrics);
        return metrics;
    }

    /**
     *
     * @param disp
     * @return
     */
    public static DisplayMetrics getMetrics(Display disp) {
        if (disp == null) return null;

        DisplayMetrics metrics = new DisplayMetrics();
        disp.getMetrics(metrics);
        return metrics;
    }

    /**
     *
     * @return
     */
    public static String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     *
     * @return
     */
    public static String getAppVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }

    /**
     *
     * @param context
     * @param asset
     * @param style
     * @return
     */
    public static Typeface getTypeface(Context context, String asset, int style) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(context.getAssets(), asset);
        } catch (Exception e) {
            tf = Typeface.DEFAULT;
        } finally {
            return Typeface.create(tf, style);
        }
    }

}
