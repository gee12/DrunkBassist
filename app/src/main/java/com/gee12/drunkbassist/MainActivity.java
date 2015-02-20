package com.gee12.drunkbassist;

import com.gee12.drunkbassist.util.SystemUiHider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import java.util.List;
import java.util.Random;

import static com.gee12.drunkbassist.ModelsManager.*;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity implements SensorEventListener {

    public final static String POINTS = "com.gee12.drunkbassist.POINTS";
    public final static int MSEC_MAX = 60000;
    public final static float POSITIONS_ROUND = 2f;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private PowerManager.WakeLock mWakeLock;
    private DrawView drawView;
    private float oldAccX=0, oldAccY=0;
    private int randomMoveCounter = 0;
    private PointF randomOffset = new PointF();
    DegreeTimer timer;

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

                load(getResources(), drawView.getWidth(), drawView.getHeight());

                // uncomment when will be several DRINKS
//              timer = new DegreeTimer(MSEC_MAX, drinks[i].getMsec());
                timer = new DegreeTimer(MSEC_MAX, drink.getMsec());
                timer.start();

                //
                randomDrinkPosition();
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
//        PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Brightness");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
//        mWakeLock.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
//        mWakeLock.acquire();

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
                //
                drawView.accX = values[SensorManager.DATA_X];
                drawView.accY = values[SensorManager.DATA_Y];
            }
            break;
        }
    }

    public void onAccelerometerChanged(float accX, float accY) {
        float dAccX = oldAccX - accX;
        float dAccY = oldAccY - accY;
        updateHeroOffset(-dAccY, -dAccX);
    }

    public void randomHeroOffset() {
        // offset hero position, while counter > 0
        if (randomMoveCounter-- > 0) {
            // add random offset
            updateHeroOffset(randomOffset.x, randomOffset.y);
        } else {
            // hero alcoholic intoxication (degree)
            int degree = hero.getDegree();
            if (degree > 0) {
                Random rand = new Random();
                // new random delay
                randomMoveCounter = rand.nextInt(10);// * degree);
                // new random offset
                randomOffset = new PointF(
                        (float) Math.sqrt(rand.nextFloat() * degree - degree / 2.f),
                        (float) Math.sqrt(rand.nextFloat() * degree - degree / 2.f));
            }
        }

    }

    public void updateHeroOffset(float dx, float dy) {
        hero.setOffset(dx, dy);
    }

    public void onHeroPositionStatus() {
        SceneMask.HitStatus status = mask.hitStatus(hero.getCenter());
        switch(status) {
            case IN_SCENE:
                break;

            case AT_THE_EDGE:
                break;

            case OUT_FROM_SCENE:
                //onFinish();
                break;
        }
        drawView.text = status.toString();
    }

    public void onHeroDrinking() {
        // if (hero.pos ~= drink.pos)
        if (Math.abs(hero.getCenter().x - drink.getCenter().x) < POSITIONS_ROUND
                && Math.abs(hero.getCenter().y - drink.getCenter().y) < POSITIONS_ROUND) {
//        if (hero.getCenter().x > 229 && hero.getCenter().x < 231) {
            // hero drinking !!

            drawView.text = "ПОПАЛ";

            hero.setDegree(hero.getDegree() + drink.getDegree());
            hero.setPoints(hero.getPoints() + drink.getPoints());
            //
            drink.setVisible(false);
            timer.cancel();

            // uncomment when will be several DRINKS
//            timer = new DegreeTimer(MSEC_MAX, drinks[i].getMsec());
            timer.start();
            randomDrinkPosition();
            drink.setVisible(true);
        }
    }

    public void randomDrinkPosition() {
        RectF destRectF = mask.getDestRectF();
        Random rand = new Random();
        do {
            PointF pos = new PointF(
                    rand.nextFloat() * destRectF.width() + destRectF.left,
                    rand.nextFloat() * destRectF.height() + destRectF.top
            );
            drink.setPosition(pos);
        } while (mask.hitStatus(drink.getCenter()) != SceneMask.HitStatus.IN_SCENE);

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
            Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(menuIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFinish() {
        timer.cancel();

        // to FinishActivity
        Intent finishIntent = new Intent(MainActivity.this, FinishActivity.class);
        // send points
        finishIntent.putExtra(POINTS, hero.getPoints());
        startActivity(finishIntent);
        finish();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        finish();
    }

    public class DegreeTimer extends CountDownTimer {

        public DegreeTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
        }

        public void onTick(long millisUntilFinished) {
            // change drink position
            randomDrinkPosition();
        }

    }

//    class DrinkTimerTask extends TimerTask {
//
//        @Override
//        public void run() {
//
////            runOnUiThread(new Runnable() {
////
////                @Override
////                public void run() {
////
////                }
////            });
//        }
//
////        @Override
////        public boolean cancel() {
////            return super.cancel();
////        }
//    }

}
