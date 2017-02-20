package com.sunqiang.bluetooth;

import android.app.Application;
import android.content.Context;

import com.sunqiang.bluetooth.activity.ViewManager;

/**
 * Created by sunqiang on 2017/2/14.
 */

public class BluetoothApplication extends Application {

    private static Context mContext;
    private static ViewManager viewManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static ViewManager getViewManager(){
        if(viewManager==null){
            viewManager = new ViewManager(mContext);
        }
        return viewManager;
    }
}
