package com.example.nikki.tringg;

/**
 * Created by Nikki on 06-11-2014.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nikki on 30-09-2014.
 */
public class MyDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "mydb";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "student";
    public static final String COL_NAME = "pName";
    public static final String COL_DATE = "pDate";
    public static final String STRING_CREATE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME + " TEXT, " + COL_DATE + " DATE);";

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(STRING_CREATE);
        ContentValues cv = new ContentValues(2);
        cv.put(COL_NAME, "New Entry");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put(COL_DATE, dateFormat.format(new Date()));
        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}