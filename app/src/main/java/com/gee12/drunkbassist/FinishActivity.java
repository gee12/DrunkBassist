package com.gee12.drunkbassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 *
 */
public class FinishActivity extends Activity {

    public static String DEFAULT_USER_NAME = "Неизвестный";

    private boolean isRecord = false;
    int points;
    EditText nameTextField;
    TextView pointsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finish);

        nameTextField = (EditText)findViewById(R.id.textfield_name);
        pointsLabel = (TextView)findViewById(R.id.label_points);

        // get points
        Intent intent = getIntent();
        points = intent.getExtras().getInt(MainActivity.POINTS, 0);
        pointsLabel.setText(String.valueOf(points));

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
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public void onClickMenuButton(View view) {
        if (isRecord) {
            addNewRecord(getUserName(), points);
        }
        //
        toMenuActivity();
        finish();
    }

    @Override
    public void onBackPressed(){
        toMenuActivity();
        super.onBackPressed();
    }

    private void toMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        menuIntent.putExtra("finishApplication", true);
        startActivity(menuIntent);
//        finish();
    }

}
