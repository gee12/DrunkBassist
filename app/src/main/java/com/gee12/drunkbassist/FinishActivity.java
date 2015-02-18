package com.gee12.drunkbassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 */
public class FinishActivity extends Activity {

    public static String DEFAULT_USER_NAME = "Неизвестный";

    private boolean isRecord = false;
    int points;
    TextView nameTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finish);

        nameTextField = (TextView)findViewById(R.id.textfield_name);

        // get points
        //Intent intent = getIntent();
        //points = intent.getExtras().getInt(MainActivity.POINTS, 0);

        // define minPoints
        int minPoints = 0;

        if (points > minPoints) {
            isRecord = true;
            nameTextField.setVisibility(View.VISIBLE);
        }
    }

    public String getUserName() {
        String name = nameTextField.getText().toString();
        return (name.isEmpty()) ? DEFAULT_USER_NAME : name;
    }

    public void addNewRecord(String name, int points) {

    }

    public void onClickReplayButton(View view) {
        if (isRecord) {
            addNewRecord(getUserName(), points);
        }
        //
        Intent mainIntent = new Intent(FinishActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    public void onClickMenuButton(View view) {
        if (isRecord) {
            addNewRecord(getUserName(), points);
        }
        //
        Intent menuIntent = new Intent(FinishActivity.this, MenuActivity.class);
        startActivity(menuIntent);
    }
}
