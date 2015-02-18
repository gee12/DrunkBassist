package com.gee12.drunkbassist;

import com.gee12.drunkbassist.util.SystemUiHider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
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


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity implements SensorEventListener, HeroListener {

    public final static String POINTS = "com.gee12.drunkbassist.POINTS";
    public final static int MSEC = 3000;
    public final static int MSEC_MAX = MSEC * 20;
    public final static int POINTS_FOR_DEGREE = 10;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private DrawView drawView;
    private float oldAccX=0, oldAccY=0;
    private int points = 0;
    private int degree = 0;
    DegreeTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        timer = new DegreeTimer(MSEC_MAX, MSEC);
        timer.start();
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
                drawView.setAccelerometerXY(values[SensorManager.AXIS_X],
                        event.values[SensorManager.AXIS_Y]);
                //
                heroPositionChange(values[SensorManager.DATA_X],
                        event.values[SensorManager.DATA_Y]);
                //
                randomHeroMove();
            }
            break;
        }
    }

    public void randomHeroMove() {

        if (randomMoveCounter-- > 0) {
            drawView.setHeroOffset(randomOffset.x, randomOffset.y);
        } else {
            Random rand = new Random();
            randomMoveCounter = rand.nextInt(50);
            randomOffset = new PointF(
                    rand.nextFloat() * degree - degree/2.f,
                    rand.nextFloat() * degree - degree/2.f
            );
        }

    }

    public void heroPositionChange(float accX, float accY) {
        float dAccX = oldAccX - accX;
        float dAccY = oldAccY - accY;
        drawView.setHeroOffset(-dAccY, -dAccX);
    }

    public void heroDegreeChange(int degree) {
        drawView.setHeroDegree(degree);
    }

    public void heroPointsChange(int points) {
        drawView.setHeroPoints(points);
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

    @Override
    public void onFinish() {
        timer.cancel();

        // to FinishActivity
        Intent finishIntent = new Intent(MainActivity.this, FinishActivity.class);
        // send points
        finishIntent.putExtra(POINTS, points);
        startActivity(finishIntent);
        finish();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }

    public class DegreeTimer extends CountDownTimer {

        public DegreeTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
        }

        public void onTick(long millisUntilFinished) {
            heroDegreeChange(degree++);
            heroPointsChange(points+=POINTS_FOR_DEGREE);
        }
    }
}
