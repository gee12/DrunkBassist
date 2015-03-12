package com.gee12.drunkbassist;

import android.annotation.TargetApi;
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
}
