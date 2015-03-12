package com.gee12.drunkbassist.struct;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.gee12.drunkbassist.IndicatorsManager;
import com.gee12.drunkbassist.ModelsManager;
import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.sound.SoundManager;

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

    public static final float POSITIONS_ROUND = 15f;
    public static final float OFFSTEP_STEP_INC = 0.03f;
    public static final float ANGLE_AT_THE_EDGE = 15;
    public static final float ANGLE_OUT_OF_SCENE = 90;
    public static final int LEG_VERTICAL_OFFSET = 5;
    public static final int MIN_MOVE_DIST = 10;
    public static final float TOUCH_SPEED = 0.2f;
    public Limb lLeg;

    public Limb rLeg;
    public Limb body;
    public Limb head;

    private Stands curHeroStand = Stands.STAND;
    private PointF moveMeter = new PointF();
    private PointF touchOffset = new PointF();
    private boolean isCanMove = true;

    private int randomMoveCounter = 0;
    private float offsetStep = 0;
    SceneMask.PositionStatus positonStatus;


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

    public void loadLimbs(Resources res, float density) {
        limbs = new ArrayList<>();

        addLimb(lLeg = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_l_leg), new PointF(10 * density, 69 * density)));
        addLimb(rLeg = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_r_leg), new PointF(25 * density, 69 * density)));
        addLimb(body = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_body), new PointF(0, 30 * density)));
        addLimb(head = new Limb(BitmapFactory.decodeResource(res, R.drawable.hero_head), new PointF(3 * density, 0)));
    }

    @Override
    public void drawModel(Canvas canvas) {
        super.drawModel(canvas);

//        Paint p = new Paint();
//        p.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(matrix.getDestRect(), p);
    }

    /////////////////////////////////////////////////////////////////////////
    //

    public void setTouchOffset(float touchX, float touchY) {
        if (touchX == 0 && touchY == 0) {
            this.touchOffset.set(0, 0);
        } else {
            PointF pos = getAbsPivotPoint();
            float dTouchX = touchX - pos.x;
            float dTouchY = touchY - pos.y;
            double angle = Math.atan2(dTouchY, dTouchX);

            float dx = TOUCH_SPEED * (float)Math.cos(angle);
            float dy = TOUCH_SPEED * (float)Math.sin(angle);
            this.touchOffset.set(dx, dy);
        }
    }

    public void onTouchOffset() {
        if (touchOffset.x == 0 && touchOffset.y == 0) return;
        setHeroOffset(touchOffset.x, touchOffset.y);
    }

    public void heroOnScene() {
        if (positonStatus != SceneMask.PositionStatus.ON_SCENE) {
            rotate(0);
            positonStatus = SceneMask.PositionStatus.ON_SCENE;
        }

    }

    public void heroAtTheEdge(int viewWidth) {
        if (positonStatus != SceneMask.PositionStatus.AT_THE_EDGE) {
            float angle = (getPosition().x < viewWidth/2) ? -ANGLE_AT_THE_EDGE : ANGLE_AT_THE_EDGE;
            rotate(angle);
            positonStatus = SceneMask.PositionStatus.AT_THE_EDGE;
        }
    }

    public void heroOutOfScene(int viewWidth) {
        if (positonStatus != SceneMask.PositionStatus.OUT_FROM_SCENE) {
            float angle = (getPosition().x < viewWidth/2) ? -ANGLE_OUT_OF_SCENE : ANGLE_OUT_OF_SCENE;
            rotate(angle);

            isCanMove = false;

            // sound
            SoundManager.DownSound.play();
            SoundManager.SnoreSound.play();

            positonStatus = SceneMask.PositionStatus.OUT_FROM_SCENE;
        }
    }

    public void setHeroOffset(float dx, float dy) {
        if (!isCanMove) return;

        offsetPosition(dx, dy);
        // skew
//        ModelsManager.Hero.offsetSkew(-dx/20.0f, 0);
        // on stand
//        isSmallOffset = (Math.abs(dx) < SMALL_OFFSET_MAX && Math.abs(dy) < SMALL_OFFSET_MAX);

        onHeroLegs(dx, dy);
    }

    public void onHeroLegs(float dx, float dy) {
        moveMeter.offset(dx, dy);
        if (moveMeter.x > MIN_MOVE_DIST || moveMeter.y > MIN_MOVE_DIST ||
                moveMeter.x < -MIN_MOVE_DIST || moveMeter.y < -MIN_MOVE_DIST) {
            moveMeter = new PointF();

            switch(curHeroStand) {
                case STAND:
                    curHeroStand = Stands.LEFT_LEG_UP;
                    lLeg.offsetPosition(0, -LEG_VERTICAL_OFFSET);
                    break;
                case LEFT_LEG_UP:
                    curHeroStand = Stands.RIGHT_LEG_UP;
                    lLeg.offsetPosition(0, LEG_VERTICAL_OFFSET);
                    rLeg.offsetPosition(0, -LEG_VERTICAL_OFFSET);

                    SoundManager.Step2Sound.play();
                    break;
                case RIGHT_LEG_UP:
                    curHeroStand = Stands.LEFT_LEG_UP;
                    lLeg.offsetPosition(0, -LEG_VERTICAL_OFFSET);
                    rLeg.offsetPosition(0, LEG_VERTICAL_OFFSET);

                    SoundManager.Step1Sound.play();
                    break;
            }
        }
    }

    public static final float SMALL_OFFSET_MAX = 3;
    public static final int TIKS_TO_STAND = 100;
    private boolean isSmallOffset;
    private long heroStandCounter = 0;

    public void onHeroStand() {
        if (isSmallOffset) {
            if (heroStandCounter++ > TIKS_TO_STAND) {
                heroStand();
                heroStandCounter = 0;
            }
        } else {
            heroStandCounter = 0;
        }
    }

    public void heroStand() {
        switch(curHeroStand) {
            case STAND:
                break;
            case LEFT_LEG_UP:
                lLeg.offsetPosition(0, LEG_VERTICAL_OFFSET);
                break;
            case RIGHT_LEG_UP:
                rLeg.offsetPosition(0, LEG_VERTICAL_OFFSET);
                break;
        }
        // other animation
        // ...

        curHeroStand = Stands.STAND;
    }

    public void onHeroDrinking(long pauseTime) {
        Drink drink = ModelsManager.getCurDrink();
        // if (Hero.pos ~= Drink.pos)
        if (BitmapModel.isSamePositions(getAbsPivotPoint(), drink.getCenter(), POSITIONS_ROUND)) {
            // Hero drinking !!
            int pointsInc = drink.getPoints() + IndicatorsManager.Bonus.getValue();
            int degreeInc = drink.getDegree();
            IndicatorsManager.Points.offsetValue(pointsInc);
            IndicatorsManager.Degree.offsetValue(degreeInc);
            offsetStep += OFFSTEP_STEP_INC;
            //
            IndicatorsManager.PointsInc.setValue(pointsInc);
            IndicatorsManager.DegreeInc.setValue(degreeInc);
//            IndicatorsManager.PointsInc.initBeforeDisplay(pointsInc, pauseTime);
//            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc, pauseTime);
            IndicatorsManager.PointsInc.startAnimation();
            IndicatorsManager.DegreeInc.startAnimation();
            //
            ModelsManager.nextRandomDrink(pauseTime);
            // sound
            SoundManager.DrinkingSound.play();
            // slow down the back sound
            SoundManager.MainBackSound.offsetRate(-SoundManager.RATE_CHANGE_STEP);
        }
    }

    public void onHeroEating(long pauseTime) {
        Food food = ModelsManager.getCurFood();
        // if (Hero.pos ~= Food.pos)
        if (Food.isFoodDisplay()
                && BitmapModel.isSamePositions(getAbsPivotPoint(), food.getCenter(), POSITIONS_ROUND)) {
            // Hero eating !!
            int degreeInc = food.getDegree();
            IndicatorsManager.Degree.offsetValue(degreeInc);
            offsetStep -= OFFSTEP_STEP_INC;
            //
            IndicatorsManager.DegreeInc.setValue(degreeInc);
//            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc, pauseTime);
            IndicatorsManager.DegreeInc.startAnimation();

            Food.setFoodDisplay(pauseTime, false);
            // sound
            SoundManager.EatingSound.play();
            // speed up the back sound
            SoundManager.MainBackSound.offsetRate(SoundManager.RATE_CHANGE_STEP);
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
