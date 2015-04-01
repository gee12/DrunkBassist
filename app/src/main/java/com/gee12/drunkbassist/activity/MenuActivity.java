package com.gee12.drunkbassist.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.mng.RecordsManager;
import com.gee12.drunkbassist.mng.SoundManager;

/**
 *
 */
public class MenuActivity extends Activity {

    public final static String EXTRA_STATE = "com.gee12.drunkbassist.STATE";
    public final static int EXTRA_STATE_PAUSE = 1;
    public final static int EXTRA_STATE_FINISH_GAME = 2;

    private boolean isGameRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (savedInstanceState == null) {
            onStartApplication();
        }
    }

    public void onStartApplication() {
        //
        SoundManager.load(this.getApplicationContext());
        RecordsManager.load(this.getSharedPreferences(RecordsManager.APP_RECORDS_STORE, Context.MODE_PRIVATE));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //
        /*if(intent.getIntExtra(EXTRA_STATE, 0) == EXTRA_STATE_PAUSE) {
            isGameRunning = true;
            Button startButton = (Button)findViewById(R.id.button_start);
            startButton.setText(getString(R.string.return_to_game));
        } else*/ if(intent.getIntExtra(EXTRA_STATE, 0) == EXTRA_STATE_FINISH_GAME) {
            isGameRunning = false;
            Button startButton = (Button)findViewById(R.id.button_start);
            startButton.setText(getString(R.string.start));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // sound
//        if (SoundManager.isLoaded()) {
        if (SoundManager.MenuBackSound != null) {
            SoundManager.MenuBackSound.play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // sound
        if (SoundManager.MenuBackSound != null) {
            SoundManager.MenuBackSound.stop();
        }
    }

    public void onClickStartButton(View view) {
        toMainActivity();
    }

    public void onClickRecordsButton(View view) {
        toRecordsActivity();
    }

    public void onClickDevButton(View view) { toAboutActivity();
    }

    public void onClickExitButton(View view) {
        onCloseApplication();
    }

    @Override
    public void onBackPressed(){
        if (isGameRunning) {
            toMainActivity();
        } else {
            onCloseApplication();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void toMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);

        //
        isGameRunning = true;
        Button startButton = (Button)findViewById(R.id.button_start);
        startButton.setText(getString(R.string.return_to_game));
    }

    private void onCloseApplication() {
        // sound
        SoundManager.release();

        if (MainActivity.getInstance() != null) {
            MainActivity.getInstance().finish();
        }
        finish();
    }

    private void toRecordsActivity() {
        Intent recordsIntent = new Intent(this, RecordsActivity.class);
        recordsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(recordsIntent);
    }

    private void toAboutActivity() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        aboutIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        if (Utils.SDK_VERSION >= Build.VERSION_CODES.HONEYCOMB)
//            SplitAnimation.startActivity(this, new Intent(this, AboutActivity.class));
//        else startActivity(aboutIntent);
        startActivity(aboutIntent);
    }
}
