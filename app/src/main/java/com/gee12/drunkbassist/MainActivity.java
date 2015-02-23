package com.gee12.drunkbassist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

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
    private PointF randomOffset = new PointF();
    private float offsetStep = 1 - OFFSTEP_STEP_INC;
//    private boolean isneedChangeDrinkPos;
//    private boolean isneedChangeFoodPos;

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

                //
                if (mTimer != null) {
                    mTimer.cancel();
                }
                mTimer = new Timer();
                mMyTimerTask = new MyTimerTask();
                mMyTimerTask.start();

                nextRandomDrink();
//                nextRandomFood();
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
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mMyTimerTask.cancel();
        mTimer.cancel();
//        mMyTimerTask = null;
//        mTimer = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
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
//        updateHeroOffset(-dAccY, -dAccX);
    }

    public void randomHeroOffset() {
        // offset hero position, while counter > 0
        if (randomMoveCounter-- > 0) {
            // add random offset
            updateHeroOffset(randomOffset.x, randomOffset.y);
        } else {
            // hero alcoholic intoxication (degree)
            int degree = ModelsManager.hero.getDegree();
            if (degree > 0) {
                Random rand = new Random();
                // new random delay
                randomMoveCounter = rand.nextInt(degree);
                // new random offset
                randomOffset = new PointF(
                        rand.nextFloat() * offsetStep - offsetStep / 2.f,
                        rand.nextFloat() * offsetStep - offsetStep / 2.f);
            }
        }

    }

    public void updateHeroOffset(float dx, float dy) {
        ModelsManager.hero.setOffset(dx, dy);
    }

    public void onHeroPositionStatus() {
        SceneMask.HitStatus status = ModelsManager.mask.getHitStatus(ModelsManager.hero.getCenter());
        switch(status) {
            case IN_SCENE:
                break;

            case AT_THE_EDGE:
                break;

            case OUT_FROM_SCENE:
                onFinish();
                break;
        }
//        drawView.text = status.toString();
    }


    public void onHeroDrinking() {
        Drink drink = ModelsManager.getCurDrink();
        // if (hero.pos ~= drink.pos)
        if (mMyTimerTask != null
                && mMyTimerTask.isStarted()
                && ModelsManager.hero.isSamePositions(drink, POSITIONS_ROUND)) {
            // hero drinking !!
            ModelsManager.hero.addDegree(drink.getDegree());
            ModelsManager.hero.addPoints(drink.getPoints());
            offsetStep += OFFSTEP_STEP_INC;
            //
            nextRandomDrink();
        }
    }

    public void onHeroEating() {
        Food food = ModelsManager.getCurFood();
        // if (hero.pos ~= food.pos)
        if (mMyTimerTask != null
                && mMyTimerTask.isStarted() && mMyTimerTask.isFoodDisplay()
                && ModelsManager.hero.isSamePositions(food, POSITIONS_ROUND)) {
            // hero eating !!
            ModelsManager.hero.addDegree(food.getDegree());
            offsetStep -= OFFSTEP_STEP_INC;

            mMyTimerTask.setFoodDisplay(false);
//            nextRandomFood();
        }
    }

    public void nextRandomDrink() {
        Drink curDrink = ModelsManager.nextRandomDrink();
        curDrink.setRandomPositionInScene(ModelsManager.mask);
        //
        mMyTimerTask.setCurDrinkStartTime();
    }

    public void nextRandomFood() {
        Food curFood = ModelsManager.nextRandomFood();
        curFood.setRandomPositionInScene(ModelsManager.mask);
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
        // send points
        finishIntent.putExtra(POINTS, ModelsManager.hero.getPoints());
        startActivity(finishIntent);
        // FINISH
        finish();
    }

    class MyTimerTask extends TimerTask {

        private long curTime;
        private long curDrinkStartTime;
        private long curFoodStartTime;
        private long delayBetweenFoodStartTime;
        private boolean isStarted;
        private int delayBetweenFoods;
        private boolean isFoodDisplay;

        public void start() {
            curTime = System.currentTimeMillis();
            isStarted = false;
            setFoodDisplay(false);
            // delay and repeat even 1 ms
//            mTimer.schedule(mMyTimerTask, DELAY_DEFORE_DRINKING_MSEC, 1);
        }

        @Override
        public void run() {
            isStarted = true;


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    long curTime = System.currentTimeMillis();
                    // drink
                    if (curTime - curDrinkStartTime >= ModelsManager.getCurDrink().getMsec()) {
                        nextRandomDrink();
                    }
                    // food
                    if (isFoodDisplay && curTime - curFoodStartTime >= ModelsManager.getCurFood().getMsec()) {
                        setFoodDisplay(false);
                    } else if (!isFoodDisplay && curTime - delayBetweenFoodStartTime >= delayBetweenFoods) {
                        setFoodDisplay(true);
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
                setCurFoodStartTime();
                nextRandomFood();
                ModelsManager.getCurFood().setVisible(true);
            }
        }

        public void setCurDrinkStartTime() {
            this.curDrinkStartTime = System.currentTimeMillis();
        }

        public void setCurFoodStartTime() {
            this.curFoodStartTime = System.currentTimeMillis();
        }

        public void setRandomDelayBetweenFoods() {
            int interval = Food.BETWEEN_DELAY_MAX_MSEC - Food.BETWEEN_DELAY_MIN_MSEC;
//            delayBetweenFoods = 2000;
            delayBetweenFoods = (interval > 0)
                    ? new Random().nextInt(interval) + Food.BETWEEN_DELAY_MIN_MSEC
                    : Food.BETWEEN_DELAY_MIN_MSEC;
        }

        public boolean isStarted() {
            return isStarted;
        }

        public boolean isFoodDisplay() {
            return isFoodDisplay;
        }
    }
}
