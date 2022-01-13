package edu.hzuapps.salary2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDBOpenHelper extends SQLiteOpenHelper {

    public MyDBOpenHelper(@Nullable Context context, @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE style(" +
                "styleid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "stylename VARCHAR(20)," +
                "price DECIMAL(8, 2))");
        db.execSQL("CREATE TABLE record(" +
                "recordid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "styleid INTEGER," +
                "number INTEGER," +
                "time DATE," +
                "isdel INTEGER," +
                "remark VARCHAR(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
