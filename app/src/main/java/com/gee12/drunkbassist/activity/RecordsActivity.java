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

    public static final int COL_MIN_WIDTH = 50;

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        tableLayout = (TableLayout) findViewById(R.id.table_records);

//        Typeface tf = Utils.getTypeface(getBaseContext(), getString(R.string.ext_font_name), Typeface.BOLD_ITALIC);

        int i = 1;
        for(Record rec : RecordsManager.getRecords()) {
//            addRow(rec, i++, tf);
            setRowText(rec, i++);
        }
    }

    protected void setRowText(Record rec, int num) {
        TableRow row = (TableRow)tableLayout.getChildAt(num + 1);
        ((TextView)row.getChildAt(0)).setText(String.format("%d  %s", num, rec.getName()));
        ((TextView)row.getChildAt(1)).setText(String.valueOf(rec.getPoints()));
        ((TextView)row.getChildAt(2)).setText(String.valueOf(rec.getDegree()));
    }

//    protected void addRow(Record rec, int num, Typeface tf) {
//        TableRow tr = new TableRow(this);
//        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//
//        tr.addView(createTextView(0, String.format("%d  %s", num, rec.getName()), Gravity.LEFT, 5, tf));
//        tr.addView(createTextView(1, String.valueOf(rec.getPoints()), Gravity.CENTER_HORIZONTAL, 1, tf));
//        tr.addView(createTextView(2, String.valueOf(rec.getDegree()), Gravity.CENTER_HORIZONTAL, 1, tf));
//
//        tableLayout.addView(tr);
//    }

//    private TextView createTextView(int col, String text, int gravity, int weight, Typeface tf) {
//        TextViewOutline view = new TextViewOutline(new ContextThemeWrapper(this, R.style.ExtFontStyle), null, 0);
//        if (col == 1) {
//            view.setTextSize(view.getTextSize() + 5);
//        }
//        view.setText(String.valueOf(text));
//        view.setGravity(gravity);
//        view.setMinWidth(COL_MIN_WIDTH);
//        view.setTypeface(tf, Typeface.BOLD_ITALIC);
//        view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, weight));
//        return view;
//    }


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
            tableLayout.removeViews(2, RecordsManager.getRecords().size() + 1);
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
