package com.gee12.drunkbassist;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PointF;

/**
 * Created by Иван on 24.02.2015.
 */
public class IndicatorsManager {

    public static TextModel Degree;
    public static TextModel Points;
    public static TextModel Bonus;
    public static TextModel PointsInc;
    public static TextModel DegreeInc;

    public static void load(Resources resources, int viewWidth, int viewHeight) {
        if (resources == null || viewWidth == 0 || viewHeight == 0) return;

        float defTextSize = 18;
        int y = 20;
        Points = new TextModel(0, "ОЧКОВ %4d", new PointF(10, y), defTextSize, Color.BLUE, 0);
        Bonus = new TextModel(0, "%+d", new PointF(107, y-5), defTextSize-6, Color.BLUE, 0);
        PointsInc = new TextModel(0, "%+d", new PointF(30, y+20), defTextSize+4, Color.RED, 2000);
        PointsInc.setVisible(false);

        Degree = new TextModel(0, "АЛК %3d%%", new PointF(viewWidth-80, y), defTextSize, Color.BLUE, 0);
        DegreeInc = new TextModel(0, "%+d", new PointF(viewWidth-50, y+20), defTextSize+2, Color.RED, 2000);
        DegreeInc.setVisible(false);
    }
}
