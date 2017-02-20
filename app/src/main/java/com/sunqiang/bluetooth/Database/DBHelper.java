package com.sunqiang.bluetooth.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sunqiang on 2017/2/12.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static final String TAG = "DBHelper";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table device_table(id varchar(20),name varchar(20),mac varchar(20));create table record_table(id varchar(20),name varchar(20),pose int,position int,record_time timestamp,result varchar(20),mark varchar(100),advise varchar(50),score int,file blob);";
        Log.i(TAG,"create database ..........");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"update database ..........");
    }
}
