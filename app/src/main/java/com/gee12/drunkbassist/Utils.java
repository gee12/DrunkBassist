package com.gee12.drunkbassist;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by Иван on 12.03.2015.
 */
public class Utils {


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
}
