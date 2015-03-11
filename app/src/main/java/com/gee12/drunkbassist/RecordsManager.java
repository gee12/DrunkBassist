package com.gee12.drunkbassist;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Иван on 10.03.2015.
 */
public class RecordsManager {

    public static final String APP_RECORDS_STORE = "app_records_store";
    public static final String APP_RECORD = "Record";
    public static final int MAX_RECORD_NUM = 10;

    private static SharedPreferences prefs = null;
    private static List<Record> records = new ArrayList<>();
    private static int minPoints = 0;

    public static void load(SharedPreferences sPrefs) {
        prefs = sPrefs;
        records = new ArrayList<>();

        loadRecords();
    }

    protected static void loadRecords() {
        for(int i = 0; i < MAX_RECORD_NUM; i++) {
            String key = APP_RECORD + String.valueOf(i);
            if (prefs.contains(key)) {
                String value = prefs.getString(key, "");
                Record rec = Record.parse(value);
                if (rec != null) {
                    addRecord(rec);
                }
            }
        }
        resetMinPoints();
    }

    public static void saveRecords() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        int size = Math.min(MAX_RECORD_NUM, records.size());
        for(int i = 0; i < size; i++) {
            editor.putString(APP_RECORD + String.valueOf(i), records.get(i).toString());
        }
        editor.apply();
    }

    protected static void resetMinPoints() {
        minPoints = (records.size() > 0) ? records.get(0).getPoints() : 0;
        for (Record rec : records) {
            minPoints = Math.min(minPoints, rec.getPoints());
        }
    }

    public static boolean isRecord(int points) {
        return (records.size() < MAX_RECORD_NUM || points >= minPoints);
    }

    public static void onNewRecord(Record rec) {
        if (rec == null) return;

        if (isRecord(rec.getPoints())) {
            //
            addRecord(rec);
            Collections.sort(records);
            removeExtra();
            resetMinPoints();
            //
            saveRecords();
        }
    }

    protected static void addRecord(Record rec) {
        if (rec == null) return;

        records.add(rec);
    }

    protected static void removeExtra() {
        if (records.size() > MAX_RECORD_NUM) {
            records = records.subList(0, MAX_RECORD_NUM);
        }
    }

    public static void clearRecords() {
        records.clear();

        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public static List<Record> getRecords() {
        return records;
    }
}
