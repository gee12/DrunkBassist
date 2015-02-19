package com.gee12.drunkbassist;

import android.graphics.BitmapFactory;
import android.view.View;

/**
 * Created by Иван on 19.02.2015.
 */
public class ModelsManager {

    public static Hero hero;
    public static Scene scene;
    public static SceneMask mask;
    public static Drink drink;

    public static void load(View view) {
        hero = new Hero(BitmapFactory.decodeResource(view.getResources(), R.drawable.crossfire), 30, 30);
        scene = new Scene(BitmapFactory.decodeResource(view.getResources(), R.drawable.scene), view.getWidth(), view.getHeight());
        mask = new SceneMask(BitmapFactory.decodeResource(view.getResources(), R.drawable.scene), view.getWidth(), view.getHeight());
        drink = new Drink(BitmapFactory.decodeResource(view.getResources(), R.drawable.beer_light1), 30, 30, 5, 50, 3000);
    }
}
