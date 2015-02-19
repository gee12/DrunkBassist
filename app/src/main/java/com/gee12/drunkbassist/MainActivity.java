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
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import static com.gee12.drunkbassist.ModelsManager.*;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity implements SensorEventListener, HeroListener {

    public final static String POINTS = "com.gee12.drunkbassist.POINTS";
    //public final static int MSEC = 3000;
    public final static int MSEC_MAX = 60000;
    //public final static int POINTS_FOR_DEGREE = 10;
    public final static float POSITIONS_ROUND = 0.1f;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private DrawView drawView;
    private float oldAccX=0, oldAccY=0;
//    private int points = 0;
//    private int degree = 0;
    DegreeTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        load(this.getWindow().getDecorView());

        //
        drawView = new DrawView(this, this);
        setContentView(drawView);

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

        //
        timer = new DegreeTimer(MSEC_MAX, drink.getMsec());
        timer.start();

        //
        randomDrinkPosition();
    }


    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private int randomMoveCounter = 0;
    private PointF randomOffset = new PointF();

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                //
                onAccelerometerChanged(values[SensorManager.DATA_X],
                        event.values[SensorManager.DATA_Y]);
                //
                randomHeroOffset();
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

        if (randomMoveCounter-- > 0) {
            // add random offset
            updateHeroOffset(randomOffset.x, randomOffset.y);
        } else {
            Random rand = new Random();
            randomMoveCounter = rand.nextInt(50);
            // new random offset from hero alcoholic intoxication (degree)
            int degree = hero.getDegree();
            randomOffset = new PointF(
                    rand.nextFloat() * degree - degree/2.f,
                    rand.nextFloat() * degree - degree/2.f
            );
        }

    }

    public void updateHeroOffset(float dx, float dy) {
        hero.setOffset(dx, dy);

        onHeroDrinking();

        SceneMask.HitStatus status = mask.hitStatus(hero.getPosition());
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
        // if (hero.pos ~= drink.pos)
        if (hero.getPos().x - drink.getPos().x < POSITIONS_ROUND
                && hero.getPos().y - drink.getPos().y < POSITIONS_ROUND) {
            // hero drinking !!
            hero.setDegree(hero.getDegree() + drink.getDegree());
            hero.setPoints(hero.getPoints() + drink.getPoints());
            //
            //drink.setVisible(false);
            timer.cancel();

            //drink.setVisible(true);
            timer.start();
            randomDrinkPosition();
        }
    }

    public void randomDrinkPosition() {
        RectF destRectF = drink.getDestRectF();
        Random rand = new Random();
        PointF pos;
        do {
            pos = new PointF(
                    rand.nextFloat() * destRectF.width() + destRectF.left,
                    rand.nextFloat() * destRectF.height() + destRectF.bottom
            );
        } while (mask.hitStatus(pos) != SceneMask.HitStatus.IN_SCENE);
        drink.setPosition(pos);
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

    class DrinkTimerTask extends TimerTask {

        @Override
        public void run() {

//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//
//                }
//            });
        }

//        @Override
//        public boolean cancel() {
//            return super.cancel();
//        }
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

    @Override
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
    }

}
