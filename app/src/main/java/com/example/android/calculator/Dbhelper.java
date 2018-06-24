package com.example.android.calculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper extends SQLiteOpenHelper {
    private static final int DATABACE_VERSION = 1;
    public static final String DATABACE_NAME = "story listDB";
    public static final String TABLE_CONTACTS = "story";

    public static final String KEY_FORMULA = "formula";
    public static final String KEY_RESULT = "result";
    public Dbhelper(Context context) {
        super(context, DATABACE_NAME, null, DATABACE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_FORMULA +" text," + KEY_RESULT + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_CONTACTS);

        onCreate(sqLiteDatabase);
    }
}
