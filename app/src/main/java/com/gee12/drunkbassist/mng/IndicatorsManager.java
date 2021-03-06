package com.gee12.drunkbassist.mng;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.control.TextViewIndicator;

/**
 * Created by Иван on 24.02.2015.
 */
public class IndicatorsManager {

    public static TextViewIndicator Points;
    public static TextViewIndicator Bonus;
    public static TextViewIndicator PointsInc;
    public static TextViewIndicator Degree;
    public static TextViewIndicator DegreeInc;

//    public static TextModel Points;
//    public static TextModel Bonus;
//    public static TextModel PointsInc;
//    public static TextModel Degree;
//    public static TextModel DegreeInc;

    public static void load(Context context, View rootView/*, int viewWidth, int viewHeight, float density*/) {
        //
        Points = (TextViewIndicator)rootView.findViewById(R.id.label_points);
        Points.init(0, "%d", View.VISIBLE);
        Bonus = (TextViewIndicator)rootView.findViewById(R.id.label_bonus);
        Bonus.init(0, "%+d", View.VISIBLE);
        Degree = (TextViewIndicator)rootView.findViewById(R.id.label_degree);
        Degree.init(0, "%d", View.VISIBLE);
        // Inc
        PointsInc = (TextViewIndicator)rootView.findViewById(R.id.label_points_inc);
        PointsInc.init(0, "%+d", AnimationUtils.loadAnimation(context, R.anim.alpha), View.GONE);

        DegreeInc = (TextViewIndicator)rootView.findViewById(R.id.label_degree_inc);
        DegreeInc.init(0, "%+d", AnimationUtils.loadAnimation(context, R.anim.alpha), View.GONE);

//        int strokeColor = context.getResources().getColor(R.color.text_stroke_color);
//        int indicatorColor = context.getResources().getColor(R.color.indicators_color);
//        int indicatorIncColor = context.getResources().getColor(R.color.indicators_inc_color);
//        float strokeWidth = 2;
//        Typeface tf = Utils.getTypeface(context, context.getString(R.string.main_font_name), Typeface.BOLD);
//        int incMsec = 2000;
//        float topPadding = 39 * density;
//
//        Points = new TextModel(0, "%d", new PointF(76 * density, topPadding),
//                36 * density, indicatorColor, strokeColor, strokeWidth, tf, 0);
//        Bonus = new TextModel(0, "%+d", new PointF(170 * density, topPadding - 7),
//                16 * density, indicatorColor, strokeColor, strokeWidth, tf, 0);
//        PointsInc = new TextModel(0, "%+d", new PointF(10 * density, topPadding * 2.2f),
//                48 * density, indicatorIncColor, strokeColor, strokeWidth, tf, incMsec);
//        PointsInc.setVisible(false);
//        Degree = new TextModel(0, "%d", new PointF(viewWidth - 127 * density, topPadding),
//                36 * density, indicatorColor, strokeColor, strokeWidth, tf, 0);
//        DegreeInc = new TextModel(0, "%+d", new PointF(viewWidth - 100 * density, topPadding * 2.2f),
//                48 * density, indicatorIncColor, strokeColor, strokeWidth, tf, incMsec);
//        DegreeInc.setVisible(false);
    }
}
