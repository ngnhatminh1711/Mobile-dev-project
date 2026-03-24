package com.lttbdd.hrmsystem;

import android.app.Application;

import com.lttbdd.hrmsystem.data.local.DatabaseHelper;

public class MyApplication extends Application {
    private static DatabaseHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DatabaseHelper(this);
    }

    public static DatabaseHelper getDbHelper() {
        return dbHelper;
    }
}
