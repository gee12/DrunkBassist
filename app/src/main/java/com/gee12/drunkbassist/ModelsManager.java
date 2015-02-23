package com.gee12.drunkbassist;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Иван on 19.02.2015.
 */
public class ModelsManager {

    public static Hero hero;
    public static Scene scene;
    public static SceneMask mask;
    public static List<Drink> drinks;
    public static List<Food> foods;
    private static int curDrinkIndex = 0;
    private static int curFoodIndex = 0;

    public static void load(Resources resources, int viewWidth, int viewHeight) {
        if (resources == null || viewWidth == 0 || viewHeight == 0) return;

        hero = new Hero(BitmapFactory.decodeResource(resources, R.drawable.crossfire), 40, 40,
                new PointF(viewWidth/2, viewHeight/2));
        scene = new Scene(BitmapFactory.decodeResource(resources, R.drawable.scene), viewWidth, viewHeight);
        mask = new SceneMask(BitmapFactory.decodeResource(resources, R.drawable.mask), viewWidth, viewHeight);

        drinks = new ArrayList<>();
        drinks.add(new Drink(BitmapFactory.decodeResource(resources, R.drawable.drink_beer_light1), 5, 50, 5000));
        drinks.add(new Drink(BitmapFactory.decodeResource(resources, R.drawable.drink_beer_light2), 5, 50, 5000));
        drinks.add(new Drink(BitmapFactory.decodeResource(resources, R.drawable.drink_beer_dark1), 10, 70, 5000));
        drinks.add(new Drink(BitmapFactory.decodeResource(resources, R.drawable.drink_beer_dark2), 10, 70, 5000));

        foods = new ArrayList<>();
        foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_hot_dog), -3, 3000));
        foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_burger), -3, 3000));
        foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_cheburek), -3, 3000));
        foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_cheese), -2, 3000));
        foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_pizza_part), -2, 3000));
        foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_pizza), -5, 2000));
    }

    public static Drink nextRandomDrink() {
        curDrinkIndex = new Random().nextInt(drinks.size());
        return getCurDrink();
    }

    public static Drink getCurDrink() {
        return drinks.get(curDrinkIndex);
    }

    public static Food nextRandomFood() {
        curFoodIndex = new Random().nextInt(foods.size());
        return getCurFood();
    }

    public static Food getCurFood() {
        return foods.get(curFoodIndex);
    }
}
