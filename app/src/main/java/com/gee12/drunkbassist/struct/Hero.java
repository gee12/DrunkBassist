package com.gee12.drunkbassist.struct;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.gee12.drunkbassist.IndicatorsManager;
import com.gee12.drunkbassist.ModelsManager;
import com.gee12.drunkbassist.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Иван on 17.02.2015.
 */
public class Hero extends Body {

    public enum Stands {
        STAND,
        LEFT_LEG_UP,
        RIGHT_LEG_UP
    }
//    public static int MOVE_MAX_COUNTER = 50;
    public final static float POSITIONS_ROUND = 10f;
    public final static float OFFSTEP_STEP_INC = 0.03f;
    public static int LEG_UP_LENGTH = 5;
    public static int MOVE_DIST = 10;
//
//    protected HeroFrames curFrame;
//    protected int moveCounter = MOVE_MAX_COUNTER;

    public Limb lLeg;
    public Limb rLeg;
    public Limb body;
    public Limb head;

    private Stands curHeroStand = Stands.STAND;
    private PointF moveMeter = new PointF();

    private int randomMoveCounter = 0;
    private float offsetStep = 0;


    public Hero() {
        super();
        init();
    }

    public Hero(Bitmap bitmap) {
        super(bitmap);
        init();
    }

    public Hero(Bitmap bitmap, PointF pos) {
        super(bitmap, pos);
        init();
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight) {
        super(bitmap, destWidth, destHeight);
        init();
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight, PointF pos) {
        super(bitmap, destWidth, destHeight, pos);
        init();
    }

    private void init() {

    }

    public void initLimbs(Resources res) {
        limbs = new ArrayList<>();
        addLimb(lLeg = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_l_leg), new PointF(10, 69)));
        addLimb(rLeg = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_r_leg), new PointF(25, 69)));
        addLimb(body = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_body), new PointF(0, 30)));
        addLimb(head = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_head), new PointF(3, 0)));

//        DimensionF dim = getDestDimension();
//        for(Limb limb : limbs) {
//            limb.getPosition().offset(dim.width/6, dim.height/6);
//        }
    }

    @Override
    public void drawModel(Canvas canvas) {
        super.drawModel(canvas);

//        Paint p = new Paint();
//        p.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(matrix.getDestRect(), p);
    }

    /////////////////////////////////////////////////////////////////////////
    // set
    long heroLastStand = 0;

    public void onHeroStand(long gameTime) {

    }

    public void setHeroOffset(float dx, float dy) {
        offsetPosition(dx, dy);

        // skew
//        ModelsManager.Hero.offsetSkew(-dx/20.0f, 0);

        onHeroLegs(dx, dy);
    }

    public void onHeroLegs(float dx, float dy) {
        moveMeter.offset(dx, dy);
        if (moveMeter.x > MOVE_DIST || moveMeter.y > MOVE_DIST ||
                moveMeter.x < -MOVE_DIST || moveMeter.y < -MOVE_DIST) {
            moveMeter = new PointF();

            switch(curHeroStand) {
                case STAND:
                    curHeroStand = Stands.LEFT_LEG_UP;
                    lLeg.offsetPosition(0, -LEG_UP_LENGTH);
                    break;
                case LEFT_LEG_UP:
                    curHeroStand = Stands.RIGHT_LEG_UP;
                    lLeg.offsetPosition(0, LEG_UP_LENGTH);
                    rLeg.offsetPosition(0, -LEG_UP_LENGTH);
                    break;
                case RIGHT_LEG_UP:
                    curHeroStand = Stands.LEFT_LEG_UP;
                    lLeg.offsetPosition(0, -LEG_UP_LENGTH);
                    rLeg.offsetPosition(0, LEG_UP_LENGTH);
                    break;
            }
        }
    }

    public void onHeroDrinking(long pauseTime) {
        Drink drink = ModelsManager.getCurDrink();
        // if (Hero.pos ~= Drink.pos)
        if (BitmapModel.isSamePositions(getAbsPivotPoint(), drink.getCenter(), POSITIONS_ROUND)) {
            // Hero drinking !!
            int pointsInc = drink.getPoints() + IndicatorsManager.Bonus.getValue();
            int degreeInc = drink.getDegree();
            IndicatorsManager.Points.addValue(pointsInc);
            IndicatorsManager.Degree.addValue(degreeInc);
            offsetStep += OFFSTEP_STEP_INC;
            //
            IndicatorsManager.PointsInc.initBeforeDisplay(pointsInc, pauseTime);
            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc, pauseTime);
            //
            ModelsManager.nextRandomDrink(pauseTime);
        }
    }

    public void onHeroEating(long pauseTime) {
        Food food = ModelsManager.getCurFood();
        // if (Hero.pos ~= Food.pos)
        if (Food.isFoodDisplay()
                && BitmapModel.isSamePositions(getAbsPivotPoint(), food.getCenter(), POSITIONS_ROUND)) {
            // Hero eating !!
            int degreeInc = food.getDegree();
            IndicatorsManager.Degree.addValue(degreeInc);
            offsetStep -= OFFSTEP_STEP_INC;
            //
            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc, pauseTime);

            Food.setFoodDisplay(pauseTime, false);
        }
    }

    public void randomHeroOffset() {
        // offset Hero position, while counter > 0
        if (randomMoveCounter-- > 0) {
            // add random offset
            PointF step = getMatrix().getTransStep();
            setHeroOffset(step.x, step.y);
        } else {
            // Hero alcoholic intoxication (degree)
            int degree = IndicatorsManager.Degree.getValue() * 10;
            if (degree > 0) {
                Random rand = new Random();
                // new random delay
                randomMoveCounter = rand.nextInt(degree);
                // new random offset
                getMatrix().setTransStep(new PointF(
                        rand.nextFloat() * offsetStep - offsetStep / 2.f,
                        rand.nextFloat() * offsetStep - offsetStep / 2.f));
            }
        }
    }

}
