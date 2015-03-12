package com.gee12.drunkbassist;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gee12.drunkbassist.struct.Indicator;

/**
 * Created by Иван on 24.02.2015.
 */
public class IndicatorsManager {

//    public static TextModel Degree;
//    public static TextModel Points;
//    public static TextModel Bonus;
//    public static TextModel PointsInc;
//    public static TextModel DegreeInc;

    public static Indicator Points;
    public static Indicator Bonus;
    public static Indicator PointsInc;
    public static Indicator Degree;
    public static Indicator DegreeInc;


//    public static void load(Resources resources, int viewWidth, int viewHeight) {
//        if (resources == null || viewWidth == 0 || viewHeight == 0) return;
//
//        float defTextSize = 24;
//        int y = 25;
//        Points = new TextModel(0, "ОЧКОВ %4d", new PointF(10, y), defTextSize, Color.BLUE, 0);
//        Bonus = new TextModel(0, "%+d", new PointF(145, y), defTextSize-6, Color.BLUE, 0);
//        PointsInc = new TextModel(0, "%+d", new PointF(25, y+30), defTextSize+8, Color.RED, 2000);
//        PointsInc.setVisible(false);
//
//        Degree = new TextModel(0, "АЛК %3d%%", new PointF(viewWidth-110, y), defTextSize, Color.BLUE, 0);
//        DegreeInc = new TextModel(0, "%+d", new PointF(viewWidth-70, y+30), defTextSize+8, Color.RED, 2000);
//        DegreeInc.setVisible(false);
//    }

    public static void load(Context context, View rootView) {
//        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        Animation alphaAnim = AnimationUtils.loadAnimation(context, R.anim.alpha);

        Points = new Indicator(0, "%d", (TextView)rootView.findViewById(R.id.label_points));
        Bonus = new Indicator(0, "%+d", (TextView)rootView.findViewById(R.id.label_bonus));
        Degree = new Indicator(0, "%d", (TextView)rootView.findViewById(R.id.label_degree));
        // Inc
        PointsInc = new Indicator(0, "%+d", (TextView)rootView.findViewById(R.id.label_points_inc),
                AnimationUtils.loadAnimation(context, R.anim.alpha));
        PointsInc.getView().setVisibility(View.GONE);
        DegreeInc = new Indicator(0, "%+d", (TextView)rootView.findViewById(R.id.label_degree_inc),
                AnimationUtils.loadAnimation(context, R.anim.alpha));
        DegreeInc.getView().setVisibility(View.GONE);
    }
}
