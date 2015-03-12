package com.gee12.drunkbassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gee12.drunkbassist.sound.SoundManager;

/**
 *
 */
public class FinishActivity extends Activity {

    public static final String DEFAULT_USER_NAME = "Неизвестный";

    private EditText nameTextField;
    private TextView pointsLabel;
    private TextView degreeLabel;
    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        pointsLabel = (TextView)findViewById(R.id.label_points);
        degreeLabel = (TextView)findViewById(R.id.label_degree);
        nameTextField = (EditText)findViewById(R.id.textfield_name);

        record = new Record();
        // get points and degree
        Intent intent = getIntent();
        record.setPoints(intent.getExtras().getInt(MainActivity.EXTRA_POINTS, 0));
        record.setDegree(intent.getExtras().getInt(MainActivity.EXTRA_DEGREE, 0));
        // set values
        pointsLabel.setText(String.valueOf(record.getPoints()));
        degreeLabel.setText(String.valueOf(record.getDegree()));

        if (RecordsManager.isRecord(record.getPoints())) {
            // RECORD !

            // ... print message

            nameTextField.setVisibility(View.VISIBLE);
        }

        //
//        SoundManager.playSound(SoundManager.MenuBackSound);
    }

    public String getUserName() {
        String name = nameTextField.getText().toString();
        return (name.isEmpty()) ? DEFAULT_USER_NAME : name;
    }

    public void onClickReplayButton(View view) {
        //
        toMainActivity();
        finish();
    }

    public void onClickMenuButton(View view) {
        //
        toMenuActivity();
        finish();
    }

    @Override
    public void onBackPressed(){
        toMenuActivity();
        super.onBackPressed();
//        finish();
    }

    private void toMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void toMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
//        menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        menuIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // send pause state value
        menuIntent.putExtra(MenuActivity.EXTRA_STATE, MenuActivity.EXTRA_STATE_FINISH_GAME);
        startActivity(menuIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // record
        record.setName(getUserName());
        RecordsManager.onNewRecord(record);
        // sound
        SoundManager.SnoreSound.stop();
    }
}
