package com.gee12.drunkbassist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.gee12.drunkbassist.struct.Drink;
import com.gee12.drunkbassist.struct.Food;
import com.gee12.drunkbassist.struct.SceneMask;
import com.gee12.drunkbassist.struct.TextModel;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 */
public class MainActivity extends Activity implements SensorEventListener {

    public final static String POINTS = "com.gee12.drunkbassist.POINTS";
    public final static float POSITIONS_ROUND = 10f;
    public final static float OFFSTEP_STEP_INC = 0.2f;
    public final static int DELAY_DEFORE_DRINKING_MSEC = 1000;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private DrawView drawView;
    private float oldAccX=0, oldAccY=0;
    private int randomMoveCounter = 0;
    private float offsetStep = 1 - OFFSTEP_STEP_INC;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        drawView = new DrawView(this);
        setContentView(drawView);

        // event when view will be created (for getWidth and getHeight)
        drawView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // load and init models
                ModelsManager.load(getResources(), drawView.getWidth(), drawView.getHeight());
                IndicatorsManager.load(getResources(), drawView.getWidth(), drawView.getHeight());

                //
                if (mTimer != null) {
                    mTimer.cancel();
                }
                //
                mTimer = new Timer();
                mMyTimerTask = new MyTimerTask();
//                if (mMyTimerTask != null) {
                    mMyTimerTask.start();
//                }

            }
        });

        // accelerometer
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        if(sensors.size() > 0) {
            for (Sensor sensor : sensors) {
                switch (sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        if (mAccelerometerSensor == null) mAccelerometerSensor = sensor;
                        break;
                    default:
                        break;
                }
            }
        }

        // lock bright wake up
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyTimerTask.cancel();
        mTimer.cancel();
    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//    if (mMyTimerTask != null)
//            mMyTimerTask.setRunning(false);
//    }

    @Override
    protected void onPause() {
        if (mMyTimerTask != null)
            mMyTimerTask.setRunning(false);
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        if (mMyTimerTask != null)
            mMyTimerTask.setRunning(true);
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mMyTimerTask == null) return;

        float[] values = event.values;
        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                //
                randomHeroOffset();
                //
                onAccelerometerChanged(values[SensorManager.DATA_X],
                        event.values[SensorManager.DATA_Y]);
                //
                onHeroPositionStatus();
                //
                onHeroDrinking();
                onHeroEating();
            }
            break;
        }
    }

    public void onAccelerometerChanged(float accX, float accY) {
        float dAccX = oldAccX - accX;
        float dAccY = oldAccY - accY;
        ModelsManager.Hero.offset(-dAccY, -dAccX);
    }

    public void randomHeroOffset() {
        // offset Hero position, while counter > 0
        if (randomMoveCounter-- > 0) {
            // add random offset
            ModelsManager.Hero.offset();
        } else {
            // Hero alcoholic intoxication (degree)
            int degree = IndicatorsManager.Degree.getValue();
            if (degree > 0) {
                Random rand = new Random();
                // new random delay
                randomMoveCounter = rand.nextInt(degree);
                // new random offset
                ModelsManager.Hero.setOffsetStep(
                        rand.nextFloat() * offsetStep - offsetStep / 2.f,
                        rand.nextFloat() * offsetStep - offsetStep / 2.f);
            }
        }

    }

    public void onHeroPositionStatus() {
        SceneMask.HitStatus status = ModelsManager.Mask.getHitStatus(ModelsManager.Hero.getCenter());
        switch(status) {
            case IN_SCENE:
                break;

            case AT_THE_EDGE:
                break;

            case OUT_FROM_SCENE:
                onFinish();
                break;
        }
    }


    public void onHeroDrinking() {
        Drink drink = ModelsManager.getCurDrink();
        // if (Hero.pos ~= Drink.pos)
        if (ModelsManager.Hero.isSamePositions(drink, POSITIONS_ROUND)) {
            // Hero drinking !!
            int pointsInc = drink.getPoints() + IndicatorsManager.Bonus.getValue();
            int degreeInc = drink.getDegree();
            IndicatorsManager.Points.addValue(pointsInc);
            IndicatorsManager.Degree.addValue(degreeInc);
            offsetStep += OFFSTEP_STEP_INC;
            //
            IndicatorsManager.PointsInc.initBeforeDisplay(pointsInc);
            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc);
            //
            nextRandomDrink();
        }
    }

    public void onHeroEating() {
        Food food = ModelsManager.getCurFood();
        // if (Hero.pos ~= Food.pos)
        if (mMyTimerTask.isFoodDisplay()
                && ModelsManager.Hero.isSamePositions(food, POSITIONS_ROUND)) {
            // Hero eating !!
            int degreeInc = food.getDegree();
            IndicatorsManager.Degree.addValue(degreeInc);
            offsetStep -= OFFSTEP_STEP_INC;
            //
            IndicatorsManager.DegreeInc.initBeforeDisplay(degreeInc);

            mMyTimerTask.setFoodDisplay(false);
        }
    }

    public void nextRandomDrink() {
        Drink curDrink = ModelsManager.nextRandomDrink();
        curDrink.setRandomPositionInScene(ModelsManager.Mask);
        //
        curDrink.resetDestDimension();
        curDrink.setOffsetStepFromMsec();
        //
//        curDrink.setStartTime();
        curDrink.setStartTime(mMyTimerTask.getPauseTime());
    }

    public void nextRandomFood() {
        Food curFood = ModelsManager.nextRandomFood();
        curFood.setRandomPositionInScene(ModelsManager.Mask);
        //
        curFood.resetDestDimension();
        curFood.setOffsetStepFromMsec();
        //
//        curFood.setStartTime();
        curFood.setStartTime(mMyTimerTask.getPauseTime());
        curFood.setVisible(true);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu) {

            // to MenuActivity
            toMenuActivity();
            // NO FINISH !
//            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        // to MenuActivity
        toMenuActivity();
        // NO FINISH !
        //super.onBackPressed(); (-> finish());
    }

    private void toMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(menuIntent);
    }


    public void onFinish() {
        // to FinishActivity
        Intent finishIntent = new Intent(MainActivity.this, FinishActivity.class);
        // send Points
        finishIntent.putExtra(POINTS, IndicatorsManager.Points.getValue());
        startActivity(finishIntent);
        // FINISH
        finish();
    }

    class MyTimerTask extends TimerTask {

        private long pauseTime;
        private long tempTime;
        private long delayBetweenFoodStartTime;
        private int delayBetweenFoods;
        private boolean isFoodDisplay;
        private boolean isRunning;

        public void start() {
            isRunning = true;
            pauseTime = 0;
            tempTime = 0;
            setFoodDisplay(false);
            nextRandomDrink();
            // start delay 0 ms and repeat even 1 ms
            mTimer.schedule(mMyTimerTask, 0, 1);
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isRunning) return;

                    long gameTime = System.currentTimeMillis() - pauseTime;

                    // drink
                    Drink curDrink = ModelsManager.getCurDrink();
                    if (gameTime - curDrink.getStartTime() >= curDrink.getMsec()) {
                        nextRandomDrink();
                    } else {
                        IndicatorsManager.Bonus.setValue((int) ((curDrink.getStartTime() + curDrink.getMsec() - gameTime) / 100.));
                        curDrink.makeCenterOffset();
                    }
                    // food
                    Food curFood = ModelsManager.getCurFood();
                    if (isFoodDisplay && gameTime - curFood.getStartTime() >= curFood.getMsec()) {
                        setFoodDisplay(false);
                    } else if (!isFoodDisplay && gameTime - delayBetweenFoodStartTime >= delayBetweenFoods) {
                        setFoodDisplay(true);
                    } else if (isFoodDisplay) {
                        curFood.makeCenterOffset();
                    }

                    // points increment
                    TextModel pointsInc = IndicatorsManager.PointsInc;
                    if (pointsInc.isVisible()) {
                        if (gameTime - pointsInc.getStartTime() >= pointsInc.getMsec()) {
                            pointsInc.setVisible(false);
                        } else {
                            pointsInc.incDelta(pointsInc.getOffsetStep().x);
                            pointsInc.setTextAlpha((int)pointsInc.getDelta());
                        }
                    }
                    // degree increment
                    TextModel degreeInc = IndicatorsManager.DegreeInc;
                    if (degreeInc.isVisible()) {
                        if (gameTime - degreeInc.getStartTime() >= degreeInc.getMsec()) {
                            degreeInc.setVisible(false);
                        } else {
                            degreeInc.incDelta(degreeInc.getOffsetStep().x);
                            degreeInc.setTextAlpha((int)degreeInc.getDelta());
                        }
                    }
                }
            });
        }

        public void setFoodDisplay(boolean isFoodNeedToDisplay) {
            if (!isFoodNeedToDisplay) {
                isFoodDisplay = false;
                delayBetweenFoodStartTime = System.currentTimeMillis();
                setRandomDelayBetweenFoods();
                ModelsManager.getCurFood().setVisible(false);
            } else {
                isFoodDisplay = true;
                nextRandomFood();
            }
        }

        public void setRandomDelayBetweenFoods() {
            int interval = Food.BETWEEN_DELAY_MAX_MSEC - Food.BETWEEN_DELAY_MIN_MSEC;
            delayBetweenFoods = (interval > 0)
                    ? new Random().nextInt(interval) + Food.BETWEEN_DELAY_MIN_MSEC
                    : Food.BETWEEN_DELAY_MIN_MSEC;
        }

        public void setRunning(boolean isRun) {
            this.isRunning = isRun;
            if (!isRun) {
                tempTime = System.currentTimeMillis();
            } else if (isRun && tempTime != 0) {
                tempTime = System.currentTimeMillis() - tempTime;
                pauseTime += tempTime;
                tempTime = 0;
            }
        }

        public boolean isFoodDisplay() {
            return isFoodDisplay;
        }

        public long getPauseTime() {
            return pauseTime;
        }
    }
}
