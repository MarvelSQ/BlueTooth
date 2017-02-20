package com.sunqiang.bluetooth;

import com.sunqiang.bluetooth.database.Device;

/**
 * Created by sunqiang on 2017/2/14.
 */

public class FullDevice implements Cloneable {
    private String name;
    private Device device;
    private String[] services = {"A", "B", "C", "D"};

    public FullDevice(Device device) {
        this.name = device.getName();
        this.device = device;
    }

    public FullDevice(Device device, String[] services) {
        this.name = device.getName();
        this.device = device;
        this.services = services;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        FullDevice stu = null;
        try{
                stu = (FullDevice) super.clone();
            }catch(CloneNotSupportedException e) {
                e.printStackTrace();
            }
        return stu;
    }
}
