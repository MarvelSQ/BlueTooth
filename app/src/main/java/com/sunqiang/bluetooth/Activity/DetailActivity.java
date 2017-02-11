package com.sunqiang.bluetooth.Activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sunqiang.bluetooth.Ble.BleWrapper;
import com.sunqiang.bluetooth.Ble.BleWrapperUiCallbacks;
import com.sunqiang.bluetooth.R;
import com.sunqiang.bluetooth.Adapter.ServiceListAdapter;

import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements BleWrapperUiCallbacks {

    public static final String EXTRAS_DEVICE_NAME    = "BLE_DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "BLE_DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_RSSI    = "BLE_DEVICE_RSSI";

    private String deviceName;
    private String deviceAddress;
    private String deviceRssi;

    private BleWrapper mBleWrapper;

    private Button btnConnect;
    private RecyclerView listService;
    private ServiceListAdapter mServiceListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent i=getIntent();
        deviceName=i.getStringExtra(EXTRAS_DEVICE_NAME);
        deviceAddress=i.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        deviceRssi=i.getIntExtra(EXTRAS_DEVICE_RSSI,0)+"";
        getSupportActionBar().setSubtitle("MAC:"+deviceAddress);
        btnConnect= (Button) findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBleWrapper.connect(deviceAddress);
                btnConnect.setText("connecting"+mBleWrapper.isConnected()+":"+mBleWrapper.getAdapter().getBondedDevices().size());
            }
        });
        listService = (RecyclerView) findViewById(R.id.list_service);
        mServiceListAdapter = new ServiceListAdapter(this);
        listService.setAdapter(mServiceListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBleWrapper == null) mBleWrapper = new BleWrapper(this, this);

        if(!mBleWrapper.initialize()) {
            finish();
        }

        if(mBleWrapper.isConnected()){
            btnConnect.setText("connected");
        }else {
            btnConnect.setText("connect");
        }
    }

    @Override
    public void uiDeviceFound(BluetoothDevice device, int rssi, byte[] record) {

    }

    @Override
    public void uiDeviceConnected(BluetoothGatt gatt, BluetoothDevice device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnConnect.setText("connected");
            }
        });
    }

    @Override
    public void uiDeviceDisconnected(BluetoothGatt gatt, BluetoothDevice device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnConnect.setText("connect");
            }
        });
    }

    @Override
    public void uiAvailableServices(BluetoothGatt gatt, BluetoothDevice device, final List<BluetoothGattService> services) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mServiceListAdapter.addServices(services);
                for(BluetoothGattService service:services){
                    mBleWrapper.getCharacteristicsForService(service);
                }
                mServiceListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void uiCharacteristicForService(BluetoothGatt gatt, BluetoothDevice device, final BluetoothGattService service, final List<BluetoothGattCharacteristic> chars) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(BluetoothGattCharacteristic characteristic:chars){
                    if(characteristic.getUuid().toString().toLowerCase(Locale.getDefault()).equals("00002a19-0000-1000-8000-00805f9b34fb")){
                        mBleWrapper.setNotificationForCharacteristic(characteristic,true);
                    }
                }
                mServiceListAdapter.addCharacteristicForServcie(service,chars);
                mServiceListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void uiCharacteristicsDetails(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic characteristic) {

    }

    @Override
    public void uiNewValueForCharacteristic(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic ch, final String strValue, final int intValue, byte[] rawValue, String timestamp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DetailActivity.this,strValue,Toast.LENGTH_SHORT).show();
                Toast.makeText(DetailActivity.this,"间隔",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void uiGotNotification(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, final BluetoothGattCharacteristic characteristic) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mBleWrapper.getCharacteristicValue(characteristic);
            }
        });
    }

    @Override
    public void uiSuccessfulWrite(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic ch, String description) {

    }

    @Override
    public void uiFailedWrite(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic ch, String description) {

    }

    @Override
    public void uiNewRssiAvailable(BluetoothGatt gatt, BluetoothDevice device, int rssi) {

    }

    public static int byte2int(byte[] res) {

        int intValue = 0;
        for (int i = 0; i < res.length; i++) {
            intValue += (res[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }
}
