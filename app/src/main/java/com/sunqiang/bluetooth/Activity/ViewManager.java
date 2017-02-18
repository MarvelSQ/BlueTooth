package com.sunqiang.bluetooth.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sunqiang.bluetooth.Database.Device;
import com.sunqiang.bluetooth.Database.DeviceDB;

/**
 * Created by sunqiang on 2017/2/14.
 */

public class ViewManager {

    private Context context;
    private DeviceDB deviceDB;

    public ViewManager(Context context) {
        this.context = context;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void gotoView(Class activityClass, Bundle bundle){
        Intent in = new Intent(context,activityClass);
        in.putExtra("view",bundle);
        context.startActivity(in);
    }

    public void addToDatabase(Device device){
        deviceDB = new DeviceDB(context);
        deviceDB.addNewDevice(device.getMAC(),device.getName());
    }
}
