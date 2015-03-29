package com.gee12.drunkbassist.mng;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.Utils;
import com.gee12.drunkbassist.game.GameTimerTask;
import com.gee12.drunkbassist.model.BitmapModel;
import com.gee12.drunkbassist.model.Drink;
import com.gee12.drunkbassist.model.Food;
import com.gee12.drunkbassist.model.Hero;
import com.gee12.drunkbassist.model.SceneMask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 19.02.2015.
 */
public class ModelsManager {

    public static Hero Hero;
    public static BitmapModel Scene;
    public static SceneMask Mask;
    public static List<Drink> Drinks;
    public static List<Food> Foods;
    public static BitmapModel Bass;
    private static int curDrinkIndex = 0;
    private static int curFoodIndex = 0;
    private static boolean isLoaded;
    public static boolean isBassFlyStarted;

    public static void load(Resources res, int viewWidth, int viewHeight, float density) {
        // HERO
        Bitmap heroBitmap = decodeBitmap(res, R.drawable.hero);
        PointF heroPos = new PointF(viewWidth/2 - heroBitmap.getWidth()/2 * density,
                viewHeight/2 - heroBitmap.getHeight()/2 * density);
        Hero = new Hero(heroBitmap, heroPos, density);
        Hero.setPivotPoint(34 * density,
                (heroBitmap.getHeight() - 10));
        Hero.loadLimbs(res);
        Hero.setCanMove(false);

        // SCENE, MASK
        Mask = new SceneMask(decodeBitmap(res, R.drawable.mask), viewWidth, viewHeight);
        Scene = new BitmapModel(decodeBitmap(res, R.drawable.scene), viewWidth, viewHeight);

        // DRINKS
        Drinks = new ArrayList<>();
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_beer_light1), 5, 10, 3000));
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_beer_light2), 5, 10, 3000));
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_beer_dark1), 10, 15, 3000));
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_beer_dark2), 10, 15, 3000));
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_champagne), 15, 20, 3000));
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_vodka), 15, 20, 3000));
        Drinks.add(new Drink(decodeBitmap(res, R.drawable.drink_cognac), 15, 20, 3000));

        // FOODS
        Foods = new ArrayList<>();
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_hot_dog), -3, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_burger), -3, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_cheburek), -3, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_cheese), -2, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_pizza_part), -2, 3000));
        Foods.add(new Food(decodeBitmap(res, R.drawable.food_pizza), -5, 2000));

        // BASS
        Bitmap bassBitmap = decodeBitmap(res, R.drawable.bass);
        Bass = new BitmapModel(bassBitmap);
        Bass.setPivotPoint(bassBitmap.getWidth()*0.5f, bassBitmap.getHeight()*0.5f);
        Bass.setVisible(false);
        isBassFlyStarted = false;

        isLoaded = true;
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
        curDrinkIndex = Utils.Random.nextInt(Drinks.size());
        getCurDrink().resetFood(pauseTime);

        // sound
        SoundManager.DrinkSound.play();
    }

    public static void nextRandomFood(long pauseTime) {
        curFoodIndex = Utils.Random.nextInt(Foods.size());
        getCurFood().resetFood(pauseTime);

        // sound
        SoundManager.FoodSound.play();
    }


    /**
     * Bass fly animation
     *
     * @param viewWidth
     */
    public static void onBassFly(int viewWidth) {
        if (!isBassFlyStarted) {
            isBassFlyStarted = true;

            PointF heroPos = Hero.getPosition();
            PointF pos = new PointF(heroPos.x + Hero.Body.getPosition().x,
                    heroPos.y + Hero.Body.getPosition().y);
            Bass.setPosition(pos);
            // rotate
            if (heroPos.x < viewWidth / 2) {
                Bass.offsetRotate(-Hero.ANGLE_OUT_OF_SCENE);
                Bass.setRotateStep(GameTimerTask.MSEC_PER_TICK);
                Bass.setTransStep(new PointF(0.5f * GameTimerTask.MSEC_PER_TICK, 0));
            } else {
                Bass.offsetRotate(Hero.ANGLE_OUT_OF_SCENE);
                Bass.setRotateStep(-1 * GameTimerTask.MSEC_PER_TICK);
                Bass.setTransStep(new PointF(-0.5f * GameTimerTask.MSEC_PER_TICK, 0));
            }
            Bass.setVisible(true);

        } else {
            Bass.offsetRotate();
            Bass.offsetPosition();
        }
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

}
