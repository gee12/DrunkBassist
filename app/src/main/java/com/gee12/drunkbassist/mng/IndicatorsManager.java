package com.gee12.drunkbassist.mng;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Typeface;

import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.Utils;
import com.gee12.drunkbassist.model.TextModel;

/**
 * Created by Иван on 24.02.2015.
 */
public class IndicatorsManager {

//    public static AnimTextView Points;
//    public static AnimTextView Bonus;
//    public static AnimTextView PointsInc;
//    public static AnimTextView Degree;
//    public static AnimTextView DegreeInc;

    public static TextModel Points;
    public static TextModel Bonus;
    public static TextModel PointsInc;
    public static TextModel Degree;
    public static TextModel DegreeInc;

    public static void load(Context context, /*View rootView, */int viewWidth, int viewHeight, float density) {
        //
//        Points = (AnimTextView)rootView.findViewById(R.id.label_points);
//        Points.init(0, "%d", View.VISIBLE);
//        Bonus = (AnimTextView)rootView.findViewById(R.id.label_bonus);
//        Bonus.init(0, "%+d", View.VISIBLE);
//        Degree = (AnimTextView)rootView.findViewById(R.id.label_degree);
//        Degree.init(0, "%d", View.VISIBLE);
//        // Inc
//        PointsInc = (AnimTextView)rootView.findViewById(R.id.label_points_inc);
//        PointsInc.init(0, "%+d", AnimationUtils.loadAnimation(context, R.anim.alpha), View.GONE);
//
//        DegreeInc = (AnimTextView)rootView.findViewById(R.id.label_degree_inc);
//        DegreeInc.init(0, "%+d", AnimationUtils.loadAnimation(context, R.anim.alpha), View.GONE);

        int strokeColor = context.getResources().getColor(R.color.text_stroke_color);
        int indicatorColor = context.getResources().getColor(R.color.indicators_color);
        int indicatorIncColor = context.getResources().getColor(R.color.indicators_inc_color);
        float strokeWidth = 2;
        Typeface tf = Utils.getTypeface(context, context.getString(R.string.main_font_name), Typeface.BOLD);
        int incMsec = 2000;
        float topPadding = 39 * density;

        Points = new TextModel(0, "%d", new PointF(76 * density, topPadding),
                36 * density, indicatorColor, strokeColor, strokeWidth, tf, 0);
        Bonus = new TextModel(0, "%+d", new PointF(170 * density, topPadding - 7),
                16 * density, indicatorColor, strokeColor, strokeWidth, tf, 0);
        PointsInc = new TextModel(0, "%+d", new PointF(10 * density, topPadding * 2.2f),
                48 * density, indicatorIncColor, strokeColor, strokeWidth, tf, incMsec);
        PointsInc.setVisible(false);
        Degree = new TextModel(0, "%d", new PointF(viewWidth - 127 * density, topPadding),
                36 * density, indicatorColor, strokeColor, strokeWidth, tf, 0);
        DegreeInc = new TextModel(0, "%+d", new PointF(viewWidth - 100 * density, topPadding * 2.2f),
                48 * density, indicatorIncColor, strokeColor, strokeWidth, tf, incMsec);
        DegreeInc.setVisible(false);
    }
}
