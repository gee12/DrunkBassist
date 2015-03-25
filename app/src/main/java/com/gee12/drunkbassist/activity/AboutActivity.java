package com.gee12.drunkbassist.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.Utils;

/**
 *
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView versionLabel = (TextView)findViewById(R.id.label_version);
        versionLabel.setText(getString(R.string.version) + Utils.getAppVersionName() + " ");
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

    public void onClickMarkButton(View view) {
        Intent googlePlayIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mark_link2)));
        startActivity(googlePlayIntent);
    }
}
