package com.gee12.drunkbassist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gee12.drunkbassist.R;
import com.gee12.drunkbassist.mng.RecordsManager;
import com.gee12.drunkbassist.struct.Record;

/**
 *
 */
public class RecordsActivity extends Activity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        tableLayout = (TableLayout) findViewById(R.id.table_records);

        int i = 1;
        for(Record rec : RecordsManager.getRecords()) {
            setRowText(rec, i++);
        }
    }

    protected void setRowText(Record rec, int num) {
        TableRow row = (TableRow)tableLayout.getChildAt(num + 1);
        ((TextView)row.getChildAt(0)).setText(String.format("%d  %s", num, rec.getName()));
        ((TextView)row.getChildAt(1)).setText(String.valueOf(rec.getPoints()));
        ((TextView)row.getChildAt(2)).setText(String.valueOf(rec.getDegree()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_records_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_records) {

            // CLEAR records !
            tableLayout.removeViews(2, RecordsManager.getRecords().size());
            RecordsManager.clearRecords();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
