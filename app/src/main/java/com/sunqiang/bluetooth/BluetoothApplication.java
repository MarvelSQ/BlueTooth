package com.sunqiang.bluetooth;

import android.app.Application;
import android.content.Context;
import android.view.ViewManager;

import com.sunqiang.bluetooth.Activity.MainActivity;

/**
 * Created by sunqiang on 2017/2/14.
 */

public class BluetoothApplication extends Application {

    private static Context mContext;
    private static com.sunqiang.bluetooth.Activity.ViewManager viewManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static com.sunqiang.bluetooth.Activity.ViewManager getViewManager(){
        if(viewManager==null){
            viewManager = new com.sunqiang.bluetooth.Activity.ViewManager(mContext);
        }
        return viewManager;
    }
}
