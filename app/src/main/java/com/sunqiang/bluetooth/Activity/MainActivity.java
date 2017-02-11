package com.sunqiang.bluetooth.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sunqiang.bluetooth.Adapter.CustomLinearManager;
import com.sunqiang.bluetooth.Ble.BleWrapper;
import com.sunqiang.bluetooth.Ble.BleWrapperUiCallbacks;
import com.sunqiang.bluetooth.Adapter.DeviceListAdapter;
import com.sunqiang.bluetooth.R;

public class MainActivity extends AppCompatActivity {

    private Button btnScan;
    private RecyclerView listDevice;
    private CustomLinearManager layoutManager;

    private static final long SCANNING_TIMEOUT = 5 * 1000; /* 5 seconds */
    private static final int ENABLE_BT_REQUEST_ID = 1;

    private boolean mScanning = false;
    private Handler mHandler = new Handler();
    private BleWrapper mBleWrapper = null;
    private DeviceListAdapter mDevicesListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan= (Button) findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDevicesListAdapter.clearList();
                mDevicesListAdapter.notifyDataSetChanged();
                for(BluetoothDevice device:mBleWrapper.getAdapter().getBondedDevices()){
                    mDevicesListAdapter.addDevice(device,0,null);
                    Log.i("BondedDevice",device.getName()+";MAC="+device.getAddress());
                }
                btnScan.setText("Scaning");
                btnScan.setEnabled(false);
                addScanningTimeout();
                mBleWrapper.startScanning();
            }
        });
        btnScan.setLongClickable(true);
        btnScan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDevicesListAdapter.clearList();
                mDevicesListAdapter.notifyDataSetChanged();
                return true;
            }
        });
        listDevice= (RecyclerView) findViewById(R.id.list_device);
        mDevicesListAdapter = new DeviceListAdapter(this);
        mDevicesListAdapter.setOnItemClickLitener(new DeviceListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent detail = new Intent(MainActivity.this,DetailActivity.class);
                detail.putExtra(DetailActivity.EXTRAS_DEVICE_NAME,mDevicesListAdapter.getDevice(position).getName());
                detail.putExtra(DetailActivity.EXTRAS_DEVICE_ADDRESS,mDevicesListAdapter.getDevice(position).getAddress());
                detail.putExtra(DetailActivity.EXTRAS_DEVICE_RSSI,mDevicesListAdapter.getRssi(position));
                startActivity(detail);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        listDevice.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
        listDevice.setAdapter(mDevicesListAdapter);
        layoutManager = new CustomLinearManager(this);
        layoutManager.setScrollEnable(false);
        listDevice.setLayoutManager(layoutManager);
        mBleWrapper = new BleWrapper(this,new BleWrapperUiCallbacks.Null(){
            @Override
            public void uiDeviceFound(BluetoothDevice device, int rssi, byte[] record) {
                handleFoundDevice(device,rssi,record);
            }
        });

        // check if we have BT and BLE on board
        if(!mBleWrapper.checkBleHardwareAvailable()) {
            bleMissing();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on every Resume check if BT is enabled (user could turn it off while app was in background etc.)
        if(!mBleWrapper.isBtEnabled()) {
            // BT is not turned on - ask user to make it enabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
            // see onActivityResult to check what is the status of our request
        }

        // initialize BleWrapper object
        mBleWrapper.initialize();
        getSupportActionBar().setSubtitle("MAC:"+mBleWrapper.getAdapter().getAddress());
    }

    /* check if user agreed to enable BT */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // user didn't want to turn on BT
        if (requestCode == ENABLE_BT_REQUEST_ID) {
            if(resultCode == Activity.RESULT_CANCELED) {
                btDisabled();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* make sure that potential scanning will take no longer
     * than <SCANNING_TIMEOUT> seconds from now on */
    private void addScanningTimeout() {
        Runnable timeout = new Runnable() {
            @Override
            public void run() {
                if(mBleWrapper == null) return;
                mScanning = false;
                mBleWrapper.stopScanning();
                btnScan.setText("Scan");
                btnScan.setEnabled(true);
            }
        };
        mHandler.postDelayed(timeout, SCANNING_TIMEOUT);
    }

    /* add device to the current list of devices */
    private void handleFoundDevice(final BluetoothDevice device,
                                   final int rssi,
                                   final byte[] scanRecord)
    {
        // adding to the UI have to happen in UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDevicesListAdapter.addDevice(device, rssi, scanRecord);
                mDevicesListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void btDisabled() {
        Toast.makeText(this, "Sorry, BT has to be turned ON for us to work!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void bleMissing() {
        Toast.makeText(this, "BLE Hardware is required but not available!", Toast.LENGTH_LONG).show();
        finish();
    }
}
