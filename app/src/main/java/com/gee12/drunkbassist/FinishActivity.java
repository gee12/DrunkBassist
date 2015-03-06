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

    public static String DEFAULT_USER_NAME = "Неизвестный";

    private boolean isRecord = false;
    private int points;
    private int degree;
    EditText nameTextField;
    TextView pointsLabel;
    TextView degreeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        pointsLabel = (TextView)findViewById(R.id.label_points);
        degreeLabel = (TextView)findViewById(R.id.label_degree);
        nameTextField = (EditText)findViewById(R.id.textfield_name);

        // get points and degree
        Intent intent = getIntent();
        points = intent.getExtras().getInt(MainActivity.EXTRA_POINTS, 0);
        degree = intent.getExtras().getInt(MainActivity.EXTRA_DEGREE, 0);
        // set values
        pointsLabel.setText(String.valueOf(points));
        degreeLabel.setText(String.valueOf(degree));

        // define minPoints
        int minPoints = 0;

        if (points > minPoints) {
            isRecord = true;
            nameTextField.setVisibility(View.VISIBLE);
        }

        //
//        SoundManager.playSound(SoundManager.MenuBackSound);
    }

    protected void onRecord() {
        if (isRecord) {
            addNewRecord(getUserName(), points);
        }
    }

    public String getUserName() {
        String name = nameTextField.getText().toString();
        return (name.isEmpty()) ? DEFAULT_USER_NAME : name;
    }

    public void addNewRecord(String name, int points) {

    }

    public void onClickReplayButton(View view) {
        onRecord();
        //
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();

        // sound
        SoundManager.SnoreSound.stop();
    }

    public void onClickMenuButton(View view) {
        onRecord();
        //
        toMenuActivity();
        finish();

        // sound
        SoundManager.SnoreSound.stop();
    }

    @Override
    public void onBackPressed(){
//        super.onBackPressed();
        toMenuActivity();
        finish();
    }

    private void toMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
//        menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        menuIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(menuIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
