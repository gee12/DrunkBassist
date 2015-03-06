package com.gee12.drunkbassist;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PointF;

import com.gee12.drunkbassist.struct.TextModel;

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

        float defTextSize = 24;
        int y = 25;
        Points = new TextModel(0, "ОЧКОВ %4d", new PointF(10, y), defTextSize, Color.BLUE, 0);
        Bonus = new TextModel(0, "%+d", new PointF(145, y), defTextSize-6, Color.BLUE, 0);
        PointsInc = new TextModel(0, "%+d", new PointF(25, y+30), defTextSize+8, Color.RED, 2000);
        PointsInc.setVisible(false);

        Degree = new TextModel(0, "АЛК %3d%%", new PointF(viewWidth-110, y), defTextSize, Color.BLUE, 0);
        DegreeInc = new TextModel(0, "%+d", new PointF(viewWidth-70, y+30), defTextSize+8, Color.RED, 2000);
        DegreeInc.setVisible(false);
    }
}
