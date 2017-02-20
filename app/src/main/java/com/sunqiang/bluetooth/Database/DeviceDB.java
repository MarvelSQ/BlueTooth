package com.sunqiang.bluetooth.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sunqiang.bluetooth.DeviceUUID;

import java.util.ArrayList;

/**
 * Created by sunqiang on 2017/2/12.
 */

public class DeviceDB {

    private final String TAG = "DeviceDB";
    private final String DB_NAME = "ble_device";
    private final String Device_TABLE = "device_table";
    private String[] Device_COLS = {"id","name","mac"};
    private String[] Device_FORM = {"varchar(20)","varchar(20)","varchar(20)"};

    private SQLiteDatabase mDeviceDB;
    private DBHelper mDBHelper = null;

    public DeviceDB(Context context) {
        mDBHelper = new DBHelper(context, DB_NAME, null, 1);
    }

    public boolean addNewDevice(String address, String name) {
        long result = 0l;
        if (mDBHelper != null) {
            mDeviceDB = mDBHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Device_COLS[0], DeviceUUID.generateShortUuid());
            cv.put(Device_COLS[1], name);
            cv.put(Device_COLS[2], address);
            result = mDeviceDB.insert(Device_TABLE, null, cv);
        }
        return result > 0;
    }

    public boolean removeDeviceByMAC(String mac) {
        long result = 0l;
        if (mDBHelper != null) {
            mDeviceDB = mDBHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            mDeviceDB.delete(Device_TABLE, "mac=?", new String[]{mac});
        }
        return result > 0;
    }

    public boolean removeDeviceByID(String id) {
        long result = 0l;
        if (mDBHelper != null) {
            mDeviceDB = mDBHelper.getWritableDatabase();
            mDeviceDB.delete(Device_TABLE, "id=?", new String[]{id});
        }
        return result > 0;
    }

    public ArrayList<Device> getAllDevice() {
        ArrayList<Device> devices = new ArrayList();
        if (mDBHelper != null) {
            mDeviceDB = mDBHelper.getReadableDatabase();
            Cursor cursor = mDeviceDB.query(Device_TABLE, Device_COLS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while(cursor.moveToNext()){
                    devices.add(new Device(cursor.getString(cursor.getColumnIndex(Device_COLS[0])), cursor.getString(cursor.getColumnIndex(Device_COLS[1])), cursor.getString(cursor.getColumnIndex(Device_COLS[2]))));
                }
            }
        }
        return devices;
    }


}
