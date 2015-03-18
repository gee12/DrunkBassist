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

import com.gee12.drunkbassist.struct.AnimTextView;

/**
 *
 */
public class RecordsActivity extends Activity/*extends ListActivity*/ {

    public static final int ROW_TEXT_SIZE = 20;
    public static final int COL_MIN_WIDTH = 50;

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

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

        tr.addView(createTextView(String.format("%d  %s", num, rec.getName()), res, Gravity.LEFT, 5));
        tr.addView(createTextView(String.valueOf(rec.getPoints()), res, Gravity.CENTER_HORIZONTAL, 1));
        tr.addView(createTextView(String.valueOf(rec.getDegree()), res, Gravity.CENTER_HORIZONTAL, 1));

        tableLayout.addView(tr);
    }

    private TextView createTextView(String text, Resources res, int gravity, int weight) {
        AnimTextView view = new AnimTextView(this);
        view.setText(String.valueOf(text));
        view.setTextSize(ROW_TEXT_SIZE);
        view.setGravity(gravity);
        view.setMinWidth(COL_MIN_WIDTH);
        view.setTypeface(null, Typeface.BOLD_ITALIC);
        view.setCustomTextColor(res.getColor(R.color.text_color));
        view.setStrokeColor(res.getColor(R.color.indicators_stroke_color));
        view.setStrokeWidth(2);
        view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, weight));
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
