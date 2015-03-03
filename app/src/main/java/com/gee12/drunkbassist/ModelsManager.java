package com.gee12.drunkbassist;

import android.content.res.Resources;
import android.graphics.Bitmap;
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

    public static void load(Resources res, int viewWidth, int viewHeight) {
        if (res == null || viewWidth == 0 || viewHeight == 0) return;

        // HERO
        // scale hero bitmap for limbs don't clip on the border (after rotate/skew)
        Bitmap heroBitmap = decodeBitmap(res, R.drawable.hero1);
//        int widthExt = (int)(heroBitmap.getWidth() * 1.5);
//        int heightExt = (int)(heroBitmap.getHeight() * 1.5);
//        heroBitmap = Bitmap.createScaledBitmap(heroBitmap, widthExt, heightExt, false);

        PointF heroPos = new PointF(viewWidth/2 - heroBitmap.getWidth()/2,
                viewHeight/2 - heroBitmap.getHeight()/2);
        Hero = new Hero(heroBitmap, heroPos);
        Hero.setPivotPoint(heroBitmap.getWidth()/4,
                heroBitmap.getHeight());
        Hero.initLimbs(res);

        // SCENE, MASK
        Scene = new Scene(decodeBitmap(res, R.drawable.scene), viewWidth, viewHeight);
        Mask = new SceneMask(decodeBitmap(res, R.drawable.mask), viewWidth, viewHeight);

        // DRINKS
        Drinks = new ArrayList<>();
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_beer_light1), 5, 10, 4000));
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_beer_light2), 5, 10, 4000));
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_beer_dark1), 10, 15, 4000));
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_beer_dark2), 10, 15, 4000));

        // FOODS
        Foods = new ArrayList<>();
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_hot_dog), -3, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_burger), -3, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_cheburek), -3, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_cheese), -2, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_pizza_part), -2, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_pizza), -5, 2000));
    }

    public static Bitmap decodeBitmap(Resources res, int id) {
        return BitmapFactory.decodeResource(res, id);
    }


    public static Drink getCurDrink() {
        return Drinks.get(curDrinkIndex);
    }


    public static Food getCurFood() {
        return Foods.get(curFoodIndex);
    }


    public static void nextRandomDrink(long pauseTime) {
        curDrinkIndex = new Random().nextInt(Drinks.size());
        Drink curDrink = getCurDrink();
        curDrink.setRandomPositionInScene(ModelsManager.Mask);
        //
        curDrink.resetDestDimension();
        curDrink.setScaleStepFromMsec(GameTimerTask.SCALE_DELTA);
        //
        curDrink.setStartTime(pauseTime);
    }

    public static void nextRandomFood(long pauseTime) {
        curFoodIndex = new Random().nextInt(Foods.size());
        Food curFood = getCurFood();
        curFood.setRandomPositionInScene(ModelsManager.Mask);
        //
        curFood.resetDestDimension();
        curFood.setScaleStepFromMsec(GameTimerTask.SCALE_DELTA);
        //
        curFood.setStartTime(pauseTime);
        curFood.setVisible(true);
    }

}
