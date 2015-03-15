package com.gee12.drunkbassist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //
//        this.drawView = new DrawView(this);
//        setContentView(drawView);
        setContentView(R.layout.activity_main);

        this.drawView = (DrawView)findViewById(R.id.draw_view);

        // event when view will be created (for getWidth and getHeight)
        // OR maybe use onWindowFocusChanged()
        drawView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onGlobalLayout() {
                drawView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                MainActivity.this.instance = MainActivity.this;

                Display disp = getWindowManager().getDefaultDisplay();
                DisplayMetrics metrics = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) ?
                        Utils.getNewMetrics(disp) : Utils.getMetrics(disp);

                // load and init models
                ModelsManager.load(getResources(), drawView.getWidth(), drawView.getHeight(), metrics.density);
//                IndicatorsManager.load(getResources(), drawView.getWidth(), drawView.getHeight());
                IndicatorsManager.load(getBaseContext(), findViewById(R.id.main_layout));

                SoundManager.reinitTimerSounds();

                //
                mTimer = new Timer();
                mGameTimerTask = new GameTimerTask(MainActivity.this);
                // start delay 0 ms and repeat even 1 ms
                mTimer.schedule(mGameTimerTask, 0, 1);
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
            isStarted = true;
        } else {
            SoundManager.resumeMainBackSound();
        }
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

    public static final float STEP = 10f;


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        float dy = (keyCode == KeyEvent.KEYCODE_W) ? -STEP : (keyCode == KeyEvent.KEYCODE_S) ? STEP : 0;
        float dx = (keyCode == KeyEvent.KEYCODE_A) ? -STEP : (keyCode == KeyEvent.KEYCODE_D) ? STEP : 0;
        ModelsManager.Hero.setHeroOffset(dx, dy);
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_MOVE) {
            ModelsManager.Hero.setTouchOffset(event.getX(), event.getY());
        } else if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_CANCEL) {
            ModelsManager.Hero.setTouchOffset(0, 0);
        }
//        switch(action) {
//            case MotionEvent.ACTION_DOWN:
//                isTouchPressed = true;
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                break;
//            case MotionEvent.ACTION_CANCEL:
//
//                break;
//        }
//        if (isTouchPressed) {
//            int shotX = (int)event.getX();
//            int shotY = (int)event.getY();
//            // angle = Math.atan((double)(y - gameView.shotY) / (x - gameView.shotX));
//            // x += mSpeed * Math.cos(angle);
//            // y += mSpeed * Math.sin(angle);
//            ModelsManager.Hero.setTouchOffset(dx, dy);
//            return true;
//        }
        return super.onTouchEvent(event);
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
        Intent menuIntent = new Intent(this, MenuActivity.class);
        menuIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // send pause state value
        menuIntent.putExtra(MenuActivity.EXTRA_STATE, MenuActivity.EXTRA_STATE_PAUSE);
        startActivity(menuIntent);
    }

    @Override
    public void onFinish() {
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
