package com.zperkowski.yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import winterwell.jtwitter.Twitter;

/**
 * Created by zperkowski on 10.10.16.
 */

public class StatusData {
    static final String TAG = "StatusData";
    public static final String DB_NAME = "timeline.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "status";
    public static final String C_ID = BaseColumns._ID;
    public static final String C_CREATED_AT = "created_at";
    public static final String C_USER = "user_name";
    public static final String C_TEXT = "status_text";

    Context context;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    StatusData(Context context) {
        this.context = context;
        dbHelper = new DbHelper();
    }

    public void insert(Twitter.Status status) {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_ID, status.id);
        values.put(C_CREATED_AT, status.createdAt.getTime());
        values.put(C_USER, status.user.name);
        values.put(C_TEXT, status.text);
        sqLiteDatabase.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Cursor query() {
        sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE, null, null, null, null, null, C_CREATED_AT + " DESC");
        return cursor;
    }

    class DbHelper extends SQLiteOpenHelper {
        public DbHelper() {
            super(context, DB_NAME, null, DB_VERSION );
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql = String.format("create table %s " +
                    "(%s int primary key, %s int, %s text, %s text)",
                    TABLE, C_ID, C_CREATED_AT, C_USER, C_TEXT);
            Log.d(TAG, "onCreate with SQL: " + sql);
            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.d(TAG, "onUpgrade from: " + i + " to: " + i1);
            sqLiteDatabase.execSQL("drop table if exists " + TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}
