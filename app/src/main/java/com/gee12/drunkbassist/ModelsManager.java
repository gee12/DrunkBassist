package com.gee12.drunkbassist;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import com.gee12.drunkbassist.struct.Drink;
import com.gee12.drunkbassist.struct.Food;
import com.gee12.drunkbassist.struct.Hero;
import com.gee12.drunkbassist.struct.Scene;
import com.gee12.drunkbassist.struct.SceneMask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Иван on 19.02.2015.
 */
public class ModelsManager {

    public static com.gee12.drunkbassist.struct.Hero Hero;
    public static com.gee12.drunkbassist.struct.Scene Scene;
    public static SceneMask Mask;
    public static List<Drink> Drinks;
    public static List<Food> Foods;
    private static int curDrinkIndex = 0;
    private static int curFoodIndex = 0;

    public static void load(Resources resources, int viewWidth, int viewHeight) {
        if (resources == null || viewWidth == 0 || viewHeight == 0) return;

        Hero = new Hero(BitmapFactory.decodeResource(resources, R.drawable.crossfire),
                new PointF(viewWidth/2, viewHeight/2));
        Scene = new Scene(BitmapFactory.decodeResource(resources, R.drawable.scene), viewWidth, viewHeight);
        Mask = new SceneMask(BitmapFactory.decodeResource(resources, R.drawable.mask), viewWidth, viewHeight);

        Drinks = new ArrayList<>();
        Drinks.add(new Drink(BitmapFactory.decodeResource(resources, R.drawable.drink_beer_light1), 5, 10, 4000));
        Drinks.add(new Drink(BitmapFactory.decodeResource(resources, R.drawable.drink_beer_light2), 5, 10, 4000));
        Drinks.add(new Drink(BitmapFactory.decodeResource(resources, R.drawable.drink_beer_dark1), 10, 15, 4000));
        Drinks.add(new Drink(BitmapFactory.decodeResource(resources, R.drawable.drink_beer_dark2), 10, 15, 4000));

        Foods = new ArrayList<>();
        Foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_hot_dog), -3, 3000));
        Foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_burger), -3, 3000));
        Foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_cheburek), -3, 3000));
        Foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_cheese), -2, 3000));
        Foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_pizza_part), -2, 3000));
        Foods.add(new Food(BitmapFactory.decodeResource(resources, R.drawable.food_pizza), -5, 2000));
    }

    public static Drink nextRandomDrink() {
        curDrinkIndex = new Random().nextInt(Drinks.size());
        return getCurDrink();
    }

    public static Drink getCurDrink() {
        return Drinks.get(curDrinkIndex);
    }

    public static Food nextRandomFood() {
        curFoodIndex = new Random().nextInt(Foods.size());
        return getCurFood();
    }

    public static Food getCurFood() {
        return Foods.get(curFoodIndex);
    }
}
