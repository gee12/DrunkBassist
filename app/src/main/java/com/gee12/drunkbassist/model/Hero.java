package com.gee12.drunkbassist.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.Utils;
import com.gee12.drunkbassist.game.GameTimerTask;
import com.gee12.drunkbassist.mng.IndicatorsManager;
import com.gee12.drunkbassist.mng.ModelsManager;
import com.gee12.drunkbassist.mng.SoundManager;

import java.util.ArrayList;

/**
 * Created by Иван on 17.02.2015.
 */
public class Hero extends Body {

    public enum Stands {
        STAND,
        LEFT_LEG_UP,
        RIGHT_LEG_UP
    }

    public enum HeadFrames {
        HEAD1,
        HEAD2,
        HEAD3,
        HEAD4,
        HEAD5;
    }

    public enum BodyFrames {
        BODY1,
        BODY2,
        BODY3,
        BODY4,
        BODY_WITHOUT_BASS
    }

    public enum DrunkStages {
        DRUNK0,
        DRUNK1,
        DRUNK2,
        DRUNK3,
        DRUNK4,
        FALL
    }

    public static final float OFFSTEP_STEP_INC = 0.01f * GameTimerTask.MSEC_PER_TICK;
    public static final float TOUCH_SPEED = 0.2f * GameTimerTask.MSEC_PER_TICK;

    public static float POSITIONS_ROUND = 15f;
    public static int LEG_VERTICAL_OFFSET = 5;
    public static int MIN_MOVE_DIST = 10;

    public static final float ANGLE_AT_THE_EDGE = 15;
    public static final float ANGLE_OUT_OF_SCENE = 90;
//    public static final float SMALL_OFFSET_MAX = 3;
//    public static final int TIKS_TO_STAND = 100;

    public FrameLimb LLeg;
    public FrameLimb RLeg;
    public FrameLimb Body;
    public FrameLimb Head;
    public FrameLimb LEye;
    public FrameLimb REye;

    private Stands curHeroStand = Stands.STAND;
    private PointF moveMeter = new PointF();
    private PointF touchOffset = new PointF();
    private int randomMoveCounter = 0;
    private float offsetStep = 0;
    private SceneMask.PositionStatus curPositonStatus;
//    private boolean isSmallOffset;
//    private long heroStandCounter = 0;
    private boolean isCanMove = true;
    private DrunkStages curDrunkStage = DrunkStages.DRUNK0;
    private float density;

    public Hero() {
        super();
        init(1f);
    }

    public Hero(Bitmap bitmap, float density) {
        super(bitmap);
        init(density);
    }

    public Hero(Bitmap bitmap, PointF pos, float density) {
        super(bitmap, pos);
        init(density);
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight, float density) {
        super(bitmap, destWidth, destHeight);
        init(density);
    }

    public Hero(Bitmap bitmap, int destWidth, int destHeight, PointF pos, float density) {
        super(bitmap, destWidth, destHeight, pos);
        init(density);
    }

    private void init(float density) {
        this.density = density;
//        POSITIONS_ROUND *= density;
        LEG_VERTICAL_OFFSET *= density;
//        MIN_MOVE_DIST *= density;
    }

    public void loadLimbs(Resources res) {
        limbs = new ArrayList<>();

        // LEGS
        addLimb(LLeg = new FrameLimb(new Bitmap[]{BitmapFactory.decodeResource(res, R.drawable.hero_l_leg)},
                new PointF(12 * density, 100 * density)));
        addLimb(RLeg = new FrameLimb(new Bitmap[]{BitmapFactory.decodeResource(res, R.drawable.hero_r_leg)},
                new PointF(45 * density, 100 * density)));

        // BODY
        Bitmap[] bodyFrames = new Bitmap[] {
                BitmapFactory.decodeResource(res, R.drawable.hero_body1),
                BitmapFactory.decodeResource(res, R.drawable.hero_body2),
                BitmapFactory.decodeResource(res, R.drawable.hero_body3),
                BitmapFactory.decodeResource(res, R.drawable.hero_body4),
                BitmapFactory.decodeResource(res, R.drawable.hero_body_without_bass)
        };
        addLimb(Body = new FrameLimb(bodyFrames,
            new PointF(4 * density, 45 * density)));

        int headLeftPadding = 17;

        // EYES
        Bitmap eyeBitmap = BitmapFactory.decodeResource(res, R.drawable.hero_eye);
        addLimb(LEye = new FrameLimb(new Bitmap[]{eyeBitmap},
                new PointF((headLeftPadding + 14) * density, 18 * density)));
        LEye.setPivotPoint(eyeBitmap.getWidth()*0.5f, eyeBitmap.getHeight()*0.5f);

        addLimb(REye = new FrameLimb(new Bitmap[]{BitmapFactory.decodeResource(res, R.drawable.hero_eye)},
                new PointF((headLeftPadding + 27) * density, 18 * density)));
        REye.setPivotPoint(eyeBitmap.getWidth() * 0.5f, eyeBitmap.getHeight() * 0.5f);

        // HEAD
        Bitmap[] headFrames = new Bitmap[] {
                BitmapFactory.decodeResource(res, R.drawable.hero_head1),
                BitmapFactory.decodeResource(res, R.drawable.hero_head2),
                BitmapFactory.decodeResource(res, R.drawable.hero_head3),
                BitmapFactory.decodeResource(res, R.drawable.hero_head4),
                BitmapFactory.decodeResource(res, R.drawable.hero_head5)
        };
        addLimb(Head = new FrameLimb(headFrames, new PointF(headLeftPadding * density, 0)));

    }

    /////////////////////////////////////////////////////////////////////////
    //

    public void onNextDrunkStage(DrunkStages nextDrunkStage) {
        if (curDrunkStage != nextDrunkStage) {
            switch(nextDrunkStage) {
                case DRUNK0:
                    Body.setCurFrame(BodyFrames.BODY1.ordinal());
                    Head.setCurFrame(HeadFrames.HEAD1.ordinal());
                    break;
                case DRUNK1:
                    Body.setCurFrame(BodyFrames.BODY2.ordinal());
                    Head.setCurFrame(HeadFrames.HEAD2.ordinal());
                    setLegsCrossed(4);
                    break;
                case DRUNK2:
                    Body.setCurFrame(BodyFrames.BODY3.ordinal());
                    Head.setCurFrame(HeadFrames.HEAD3.ordinal());
                    setLegsCrossed(7);
                    break;
                case DRUNK3:
                    Body.setCurFrame(BodyFrames.BODY4.ordinal());
                    Head.setCurFrame(HeadFrames.HEAD4.ordinal());
                    setLegsCrossed(10);
                    break;
                case DRUNK4:
                    Head.setCurFrame(HeadFrames.HEAD5.ordinal());
                    Head.offsetPosition(0, 10);
                    setLegsCrossed(13);
                    break;
                case FALL:

                    break;
            }
            curDrunkStage = nextDrunkStage;
        }
    }

    public SceneMask.PositionStatus onPositionStatus(int viewWidth) {
        SceneMask.PositionStatus newStatus = ModelsManager.Mask.getHitStatus(ModelsManager.Hero.getAbsPivotPoint());
        if (curPositonStatus != newStatus) {
            switch (newStatus) {
                case ON_SCENE:
                    rotate(0);
                    break;

                case AT_THE_EDGE:
                    float angle = (getPosition().x < viewWidth / 2) ? -ANGLE_AT_THE_EDGE : ANGLE_AT_THE_EDGE;
                    rotate(angle);
                    break;

                case OUT_FROM_SCENE:
                    angle = (getPosition().x < viewWidth / 2) ? -ANGLE_OUT_OF_SCENE : ANGLE_OUT_OF_SCENE;
                    rotate(angle);
                    Body.setCurFrame(BodyFrames.BODY_WITHOUT_BASS.ordinal());

                    isCanMove = false;
                    break;
            }
            curPositonStatus = newStatus;
        }
        return newStatus;
    }

    public void setTouchOffset(float touchX, float touchY) {
        if (touchX == 0 && touchY == 0) {
            touchOffset.set(0, 0);
            setSkew(0,0);
        } else {
            PointF pos = getAbsPivotPoint();
            float dTouchY = touchY - pos.y;
            float dTouchX = touchX - pos.x;
            double angle = Math.atan2(dTouchY, dTouchX);

            float dx = TOUCH_SPEED * (float)Math.cos(angle);
            float dy = TOUCH_SPEED * (float)Math.sin(angle);
            touchOffset.set(dx, dy);
            setSkew(dx / GameTimerTask.MSEC_PER_TICK, 0);
        }
    }

    public void onTouchOffset() {
        if (touchOffset.x == 0 && touchOffset.y == 0) return;
        setOffset(touchOffset.x, touchOffset.y);
    }

    public void setOffset(float dx, float dy) {
        if (!isCanMove) return;

        offsetPosition(dx, dy);
        // skew
//        ModelsManager.Hero.setSkew(-dx/20.0f, 0);
        // on stand
//        isSmallOffset = (Math.abs(dx) < SMALL_OFFSET_MAX && Math.abs(dy) < SMALL_OFFSET_MAX);

        onLegs(dx, dy);
    }

    public void onLegs(float dx, float dy) {
        moveMeter.offset(dx, dy);
        if (moveMeter.x > MIN_MOVE_DIST || moveMeter.y > MIN_MOVE_DIST ||
                moveMeter.x < -MIN_MOVE_DIST || moveMeter.y < -MIN_MOVE_DIST) {
            moveMeter = new PointF();

            switch(curHeroStand) {
                case STAND:
                    curHeroStand = Stands.LEFT_LEG_UP;
                    LLeg.offsetPosition(0, -LEG_VERTICAL_OFFSET);
                    break;
                case LEFT_LEG_UP:
                    curHeroStand = Stands.RIGHT_LEG_UP;
                    LLeg.offsetPosition(0, LEG_VERTICAL_OFFSET);
                    RLeg.offsetPosition(0, -LEG_VERTICAL_OFFSET);

                    SoundManager.Step2Sound.play();
                    break;
                case RIGHT_LEG_UP:
                    curHeroStand = Stands.LEFT_LEG_UP;
                    LLeg.offsetPosition(0, -LEG_VERTICAL_OFFSET);
                    RLeg.offsetPosition(0, LEG_VERTICAL_OFFSET);

                    SoundManager.Step1Sound.play();
                    break;
            }
        }
    }

    public void onEyes() {
        PointF drinkPos = ModelsManager.getCurDrink().getCenter();
        PointF c = Head.getCenter();
        PointF pos = getPosition();
        PointF eyesPos = new PointF(pos.x + c.x, pos.y + c.y);
        float angle = (float)Math.toDegrees(Math.atan2(drinkPos.y - eyesPos.y, drinkPos.x - eyesPos.x));
        LEye.rotate(angle);
        REye.rotate(angle);
    }

//    public void onStand() {
//        if (isSmallOffset) {
//            if (heroStandCounter++ > TIKS_TO_STAND) {
//                stand();
//                heroStandCounter = 0;
//            }
//        } else {
//            heroStandCounter = 0;
//        }
//    }
//
//    public void stand() {
//        switch(curHeroStand) {
//            case STAND:
//                break;
//            case LEFT_LEG_UP:
//                LLeg.offsetPosition(0, LEG_VERTICAL_OFFSET);
//                break;
//            case RIGHT_LEG_UP:
//                RLeg.offsetPosition(0, LEG_VERTICAL_OFFSET);
//                break;
//        }
//        // other animation
//        // ...
//
//        curHeroStand = Stands.STAND;
//    }

    public void setLegsCrossed(float angle) {
        LLeg.rotate(-angle);
        RLeg.rotate(angle);
    }

    public void onDrinking(long pauseTime) {
        Drink drink = ModelsManager.getCurDrink();
        // if (Hero.pos ~= Drink.pos)
        if (BitmapModel.isNearPositions(getAbsPivotPoint(), drink.getCenter(), POSITIONS_ROUND)) {

            // HERO DRINKING !!
            int pointsInc = drink.getPoints() + IndicatorsManager.Bonus.getValue();
            int degreeInc = drink.getDegree();
            IndicatorsManager.Points.offsetValue(pointsInc);
            IndicatorsManager.Degree.offsetValue(degreeInc);
            offsetStep += OFFSTEP_STEP_INC;
            //
            IndicatorsManager.PointsInc.setValue(pointsInc);
            IndicatorsManager.DegreeInc.setValue(degreeInc);
            IndicatorsManager.PointsInc.initBeforeDisplay(pointsInc, pauseTime);
            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc, pauseTime);
//            IndicatorsManager.PointsInc.startAnimation();
//            IndicatorsManager.DegreeInc.startAnimation();
            //
            ModelsManager.nextRandomDrink(pauseTime);
            // sound
            SoundManager.DrinkingSound.play();
            // slow down the back sound
            SoundManager.MainBackSound.offsetRate(-SoundManager.RATE_CHANGE_STEP);
        }
    }

    public void onEating(long pauseTime) {
        Food food = ModelsManager.getCurFood();
        // if (Hero.pos ~= Food.pos)
        if (Food.isFoodDisplay()
                && BitmapModel.isNearPositions(getAbsPivotPoint(), food.getCenter(), POSITIONS_ROUND)) {

            // HERO EATING !!
            int degreeInc = food.getDegree();
            IndicatorsManager.Degree.offsetValue(degreeInc);
            if (IndicatorsManager.Degree.getValue() < 0) IndicatorsManager.Degree.setValue(0);
            offsetStep -= OFFSTEP_STEP_INC;
            //
            IndicatorsManager.DegreeInc.setValue(degreeInc);
            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc, pauseTime);
//            IndicatorsManager.DegreeInc.startAnimation();

            Food.setFoodDisplay(pauseTime, false);
            // sound
            SoundManager.EatingSound.play();
            // speed up the back sound
            SoundManager.MainBackSound.offsetRate(SoundManager.RATE_CHANGE_STEP);
        }
    }

    public void randomOffset() {
        // offset Hero position, while counter > 0
        if (randomMoveCounter-- > 0) {
            // add random offset
            PointF step = getMatrix().getTransStep();
            setOffset(step.x, step.y);
        } else {
            // Hero alcoholic intoxication (degree)
            int degree = IndicatorsManager.Degree.getValue() * 10 / GameTimerTask.MSEC_PER_TICK;
            if (degree > 0 && offsetStep > 0) {
                // new random delay
                randomMoveCounter = Utils.Random.nextInt(degree);
                // new random offset
                getMatrix().setTransStep(new PointF(
                        Utils.Random.nextFloat() * offsetStep - offsetStep / 2.f,
                        Utils.Random.nextFloat() * offsetStep - offsetStep / 2.f));
            }
        }
    }

    public void setCanMove(boolean isCanMove) {
        this.isCanMove = isCanMove;
    }

}
