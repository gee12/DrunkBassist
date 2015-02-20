package com.gee12.drunkbassist;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

/**
 * Created by Иван on 19.02.2015.
 */
public class ModelsManager {

    public static Hero hero;
    public static Scene scene;
    public static SceneMask mask;
    public static Drink drink;

    public static void load(Resources resources, int viewWidth, int viewHeight) {
        if (resources == null || viewWidth == 0 || viewHeight == 0) return;

        hero = new Hero(BitmapFactory.decodeResource(resources, R.drawable.crossfire), 30, 30,
                new PointF(viewWidth/2, viewHeight/2));
        scene = new Scene(BitmapFactory.decodeResource(resources, R.drawable.scene), viewWidth, viewHeight);
        mask = new SceneMask(BitmapFactory.decodeResource(resources, R.drawable.mask), viewWidth, viewHeight);
        drink = new Drink(BitmapFactory.decodeResource(resources, R.drawable.beer_light1), 30, 30, 5, 50, 5000);
    }
}
