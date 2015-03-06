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

import com.gee12.drunkbassist.sound.SoundManager;

import java.util.List;
import java.util.Timer;


/**
 *
 */
public class MainActivity extends Activity implements SensorEventListener, GameListener {

    public final static String EXTRA_POINTS = "com.gee12.drunkbassist.POINTS";
    public final static String EXTRA_DEGREE = "com.gee12.drunkbassist.DEGREE";
    public final static String EXTRA_STATE = "com.gee12.drunkbassist.STATE";
    public final static int EXTRA_STATE_PAUSE = 1;

    private static Activity instance;
    public static Activity getInstance() {
        return instance;
    }
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Timer mTimer;
    private GameTimerTask mGameTimerTask;
    private DrawView drawView;
    private float oldAccX=0, oldAccY=0;
    private boolean isStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //
        this.drawView = new DrawView(this);
        setContentView(drawView);

        // event when view will be created (for getWidth and getHeight)

        // OR maybe use onWindowFocusChanged()
        drawView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                MainActivity.this.instance = MainActivity.this;
                // load and init models
                ModelsManager.load(getResources(), drawView.getWidth(), drawView.getHeight());
                IndicatorsManager.load(getResources(), drawView.getWidth(), drawView.getHeight());

                //
                mTimer = new Timer();
                mGameTimerTask = new GameTimerTask(MainActivity.this);
                // start delay 0 ms and repeat even 1 ms
                mTimer.schedule(mGameTimerTask, 0, 1);

                //
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGameTimerTask.cancel();
        mTimer.cancel();
    }

    @Override
    protected void onPause() {
        if (mGameTimerTask != null)
            mGameTimerTask.setRunning(false);
        super.onPause();
        mSensorManager.unregisterListener(this);
        // sound
        SoundManager.stopMainBackSound();

    }

    @Override
    protected void onResume() {
        if (mGameTimerTask != null)
            mGameTimerTask.setRunning(true);
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        // sound
        if (!isStarted) {
            SoundManager.startMainBackSound();
        } else {
            SoundManager.resumeMainBackSound();
        }

//        SoundManager.resumeSound(SoundManager.MainStartBackSound);
//        SoundManager.resumeSound(SoundManager.MainBackSound);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (ModelsManager.Hero == null) return;

        float[] values = event.values;
        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                //
                onAccelerometerChanged(values[SensorManager.DATA_X],
                        event.values[SensorManager.DATA_Y]);
            }
            break;
        }
    }

    public void onAccelerometerChanged(float accX, float accY) {
        float dAccX = oldAccX - accX;
        float dAccY = oldAccY - accY;
        ModelsManager.Hero.setHeroOffset(-dAccY, -dAccX);
        // !
        ModelsManager.Hero.offsetSkew(dAccY/15.0f, 0);
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
            // to MenuActivity. NO FINISH !
            toMenuActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        // to MenuActivity. NO FINISH !
        toMenuActivity();
    }

    private void toMenuActivity() {
        // sound
//        SoundManager.pauseSound(SoundManager.MainStartBackSound);
//        SoundManager.pauseSound(SoundManager.MainBackSound);

        Intent menuIntent = new Intent(this, MenuActivity.class);
        menuIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // send pause state value
        menuIntent.putExtra(EXTRA_STATE, EXTRA_STATE_PAUSE);
        startActivity(menuIntent);
    }

    @Override
    public void onFinish() {
        // sound
//        SoundManager.stopSound(SoundManager.MainStartBackSound);
//        SoundManager.stopSound(SoundManager.MainBackSound);

        Intent finishIntent = new Intent(this, FinishActivity.class);
        finishIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        // send Points
        finishIntent.putExtra(EXTRA_POINTS, IndicatorsManager.Points.getValue());
        finishIntent.putExtra(EXTRA_DEGREE, IndicatorsManager.Degree.getValue());
        startActivity(finishIntent);
        // FINISH
        finish();
    }

}
