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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;
import java.util.Random;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private DrawView drawView;
    private float oldAccX=0, oldAccY=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
        drawView = new DrawView(this);
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
        float [] values = event.values;
        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                //
                drawView.setAccelerometerXY(event.values[SensorManager.AXIS_X],
                        event.values[SensorManager.AXIS_Y]);
                //
                heroPositionChange(event.values[SensorManager.DATA_X],
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
                    rand.nextFloat() * 4 - 2,
                    rand.nextFloat() * 4 - 2
            );
        }

    }

    public void heroPositionChange(float accX, float accY) {
        float dAccX = oldAccX - accX;
        float dAccY = oldAccY - accY;
        drawView.setHeroOffset(-dAccY, -dAccX);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onClickMenuButton(View view) {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
