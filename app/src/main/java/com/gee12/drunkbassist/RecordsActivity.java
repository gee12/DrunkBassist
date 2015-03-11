package com.gee12.drunkbassist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 *
 */
public class RecordsActivity extends Activity/*extends ListActivity*/ {

    public static final int ROW_TEXT_SIZE = 22;
//    private ArrayAdapter<Record> arrayAdapter;

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

//        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item_record, RecordsManager.getRecords());
//        setListAdapter(arrayAdapter);

        tableLayout = (TableLayout) findViewById(R.id.table_records);

        int i = 1;
        for(Record rec : RecordsManager.getRecords()) {
            addRow(rec, i++);
        }
    }

    protected void addRow(Record rec, int num) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        // name
        TextView name = new TextView(this);
        name.setText(String.format("%d  %s", num ,rec.getName()));
        name.setTextColor(Color.BLACK);
        name.setTextSize(ROW_TEXT_SIZE);
        name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 4));
        tr.addView(name);
        // points
        TextView points = new TextView(this);
        points.setText(String.valueOf(rec.getPoints()));
        points.setTextColor(Color.BLACK);
        points.setTextSize(ROW_TEXT_SIZE);
        points.setGravity(Gravity.CENTER_HORIZONTAL);
        points.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tr.addView(points);
        // degree
        TextView degree = new TextView(this);
        degree.setText(String.valueOf(rec.getDegree()));
        degree.setTextColor(Color.BLACK);
        degree.setTextSize(ROW_TEXT_SIZE);
        degree.setGravity(Gravity.CENTER_HORIZONTAL);
        degree.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
        tr.addView(degree);

        tableLayout.addView(tr);
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
            tableLayout.removeViews(1, RecordsManager.getRecords().size());
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
