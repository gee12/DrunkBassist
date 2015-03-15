package com.gee12.drunkbassist;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
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

    public static final int ROW_TEXT_SIZE = 26;
    public static final int COL_MIN_WIDTH = 50;
//    private ArrayAdapter<Record> arrayAdapter;

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

//        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item_record, RecordsManager.getRecords());
//        setListAdapter(arrayAdapter);

        tableLayout = (TableLayout) findViewById(R.id.table_records);
        Resources res = getResources();

        int i = 1;
        for(Record rec : RecordsManager.getRecords()) {
            addRow(rec, i++, res);
        }
    }

    protected void addRow(Record rec, int num, Resources res) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        // name
//        TextView name = new TextView(this);
//        name.setText(String.format("%d  %s", num, rec.getName()));
//        name.setTextColor(Color.BLACK);
//        name.setTextSize(ROW_TEXT_SIZE);
//        name.setTextColor(res.getColor(R.color.text_color));
//        name.setTypeface(null, Typeface.BOLD_ITALIC);
//        name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 4));
//        tr.addView(name);
//        // points
//        TextView points = new TextView(this);
//        points.setText(String.valueOf(rec.getPoints()));
//        points.setTextColor(Color.BLACK);
//        points.setTextSize(ROW_TEXT_SIZE);
//        points.setGravity(Gravity.CENTER_HORIZONTAL);
//        points.setMinWidth(COL_MIN_WIDTH);
//        points.setTextColor(res.getColor(R.color.text_color));
//        points.setTypeface(null, Typeface.BOLD_ITALIC);
//        points.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
//        tr.addView(points);
//        // degree
//        TextView degree = new TextView(this);
//        degree.setText(String.valueOf(rec.getDegree()));
//        degree.setTextColor(Color.BLACK);
//        degree.setTextSize(ROW_TEXT_SIZE);
//        degree.setGravity(Gravity.CENTER_HORIZONTAL);
//        degree.setMinWidth(COL_MIN_WIDTH);
//        degree.setTextColor(res.getColor(R.color.text_color));
//        degree.setTypeface(null, Typeface.BOLD_ITALIC);
//        degree.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
//        tr.addView(degree);


        tr.addView(createTextView(String.format("%d  %s", num, rec.getName()), res));
        tr.addView(createTextView(String.valueOf(rec.getPoints()), res));
        tr.addView(createTextView(String.valueOf(rec.getDegree()), res));

        tableLayout.addView(tr);
    }

    private TextView createTextView(String text, Resources res) {
        TextView view = new TextView(this);
        view.setText(String.valueOf(text));
        view.setTextSize(ROW_TEXT_SIZE);
        view.setGravity(Gravity.CENTER_HORIZONTAL);
        view.setMinWidth(COL_MIN_WIDTH);
        view.setTextColor(res.getColor(R.color.text_color));
        view.setTypeface(null, Typeface.BOLD_ITALIC);
        view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
        return view;
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
