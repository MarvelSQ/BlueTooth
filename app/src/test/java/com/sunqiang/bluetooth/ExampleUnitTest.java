package com.sunqiang.bluetooth;

import com.sunqiang.bluetooth.Database.Device;
import com.sunqiang.bluetooth.Database.DeviceDB;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isStringContained(){
        String s="zhiquan's 小米手机";
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add(s);
        if(!arrayList.contains(s)){
            System.out.println("arraylist not contains s");
        }
    }

    @Test
    public void isEquals(){
        ArrayList a = new ArrayList();
        Device b = new Device("","","aa:bb:cc:dd:ee:44"),c = new Device("","","aa:bb:cc:dd:ee:44");
        a.add(b);
        if(a.contains(c)){
            System.out.println("包含");
            return;
        }
        System.out.println("bu包含");
    }

    @Test
    public void istheSameOne(){
        ArrayList a = new ArrayList(),n;
        a.add("1");
        a.add("2");
        n=a;
        a.clear();
        System.out.println(n.size());
    }

    @Test
    public void data(){
        FullDevice fullDevice = new FullDevice(new Device("haha","hhhh","ssss")),fullDevice1,fullDevice2 = null;
        fullDevice1=fullDevice;
        try {
            fullDevice2= (FullDevice) fullDevice.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        fullDevice.setName("ahah");
        fullDevice.getDevice().setName("xxxx");
        fullDevice2.getName();
    }
}