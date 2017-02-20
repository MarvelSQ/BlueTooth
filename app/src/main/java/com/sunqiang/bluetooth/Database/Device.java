package com.sunqiang.bluetooth.database;

import android.view.View;

/**
 * Created by sunqiang on 2017/2/14.
 */
public class Device{
    private String ID;
    private String name;
    private String MAC;
    private boolean enable = false;
    private OnClickListener mListener;

    public void setListener(OnClickListener listener) {
        this.mListener = listener;
    }

    public Device(String ID, String name, String MAC) {
        this.ID = ID;
        this.name = name;
        this.MAC = MAC;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Device) {
            if(((Device) obj).getMAC().equals(this.getMAC())){
                return true;
            }
        }
        return false;
    }

    public void goAuth(View v){
        if(mListener!=null){
            mListener.auth(this);
        }
    }

    public void goDetail(View v){
        if(mListener!=null){
            mListener.detail(this);
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public static Device getNew(Device device){
        return new Device(device.ID,device.name,device.MAC);
    }

    public interface OnClickListener{

        void auth(Device device);

        void detail(Device device);

    }
}
