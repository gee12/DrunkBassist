package com.gee12.drunkbassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 *
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView versionLabel = (TextView)findViewById(R.id.label_version);
        versionLabel.setText(getString(R.string.version) + Utils.getAppVersionName());
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        toMenuActivity();
    }


    private void toMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        menuIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(menuIntent);
    }

}
