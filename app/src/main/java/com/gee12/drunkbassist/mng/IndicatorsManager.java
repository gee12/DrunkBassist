package com.gee12.drunkbassist.mng;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.control.AnimTextView;

/**
 * Created by Иван on 24.02.2015.
 */
public class IndicatorsManager {

    public static AnimTextView Points;
    public static AnimTextView Bonus;
    public static AnimTextView PointsInc;
    public static AnimTextView Degree;
    public static AnimTextView DegreeInc;

    public static void load(Context context, View rootView) {
        //
        Points = (AnimTextView)rootView.findViewById(R.id.label_points);
        Points.init(0, "%d", View.VISIBLE);
        Bonus = (AnimTextView)rootView.findViewById(R.id.label_bonus);
        Bonus.init(0, "%+d", View.VISIBLE);
        Degree = (AnimTextView)rootView.findViewById(R.id.label_degree);
        Degree.init(0, "%d", View.VISIBLE);
        // Inc
        PointsInc = (AnimTextView)rootView.findViewById(R.id.label_points_inc);
        PointsInc.init(0, "%+d", AnimationUtils.loadAnimation(context, R.anim.alpha), View.VISIBLE);

        DegreeInc = (AnimTextView)rootView.findViewById(R.id.label_degree_inc);
        DegreeInc.init(0, "%+d", AnimationUtils.loadAnimation(context, R.anim.alpha), View.VISIBLE);
    }
}
