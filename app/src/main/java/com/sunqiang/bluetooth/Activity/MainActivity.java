package com.sunqiang.bluetooth.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sunqiang.bluetooth.adapter.BindingListAdapter;
import com.sunqiang.bluetooth.adapter.CustomLinearManager;
import com.sunqiang.bluetooth.ble.BleWrapper;
import com.sunqiang.bluetooth.ble.BleWrapperUiCallbacks;
import com.sunqiang.bluetooth.BluetoothApplication;
import com.sunqiang.bluetooth.database.Device;
import com.sunqiang.bluetooth.database.DeviceDB;
import com.sunqiang.bluetooth.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Device.OnClickListener {

    //view
    private Button btnScan;
    private RecyclerView lvDevice, lvUnknow;
    private SwipeRefreshLayout normalLayout;

    private static final long SCANNING_TIMEOUT = 5 * 1000; /* 5 seconds */
    private static final int ENABLE_BT_REQUEST_ID = 1;
    private static final int STATUS_NORMAL = 0;
    private static final int STATUS_BLE_NON = 1;
    private static final int STATUS_BT_OFF = 2;
    private int view_status = STATUS_NORMAL;
    private int isFirstInit = 0;

    private boolean mScanning = false;
    private Handler mHandler = new Handler();
    private BleWrapper mBleWrapper = null;
    private BindingListAdapter mUnknowListAdapter = null, mCacheListAdapter = null;
    private ArrayList mUnknowList = new ArrayList<>(), mDeviceList = new ArrayList<>();
    private DeviceDB mDeviceDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Drawable icon = getResources().getDrawable(R.drawable.back_f);
//        icon.setBounds(20,20,230,230);
//        toolbar.setNavigationIcon(icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        BluetoothApplication.getViewManager().setContext(this);
        viewInit();
        componentInit();
    }

    private void viewInit() {
        normalLayout = (SwipeRefreshLayout) findViewById(R.id.status_normal);
        normalLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setScaning(true);
                addScanningTimeout();
                mBleWrapper.startScanning();
                mCacheListAdapter.clearData();
                mUnknowListAdapter.clearData();
                mDeviceList = mDeviceDB.getAllDevice();
                mCacheListAdapter.setBindingData(mDeviceList);
                mCacheListAdapter.notifyDataSetChanged();
            }
        });
        normalLayout.setColorScheme(R.color.colorTheme, R.color.colorAccent);
        btnScan = (Button) findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        lvDevice = (RecyclerView) findViewById(R.id.list_device);
        lvDevice.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        lvUnknow = (RecyclerView) findViewById(R.id.list_device_unknow);
        lvUnknow.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void componentInit() {
        mDeviceDB = new DeviceDB(this);
        mDeviceList = mDeviceDB.getAllDevice();

        CustomLinearManager layoutManager = new CustomLinearManager(this);
        layoutManager.setScrollEnable(false);

//        mDevicesListAdapter = new DeviceListAdapter();
//        mDevicesListAdapter.setOnItemClickLitener(new DeviceListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, final int position) {
//
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        });
//        lvDevice.setAdapter(mDevicesListAdapter);
        lvDevice.setLayoutManager(layoutManager);

        mCacheListAdapter = new BindingListAdapter(R.layout.device_item);
        mCacheListAdapter.setBindingData(mDeviceList);
        lvDevice.setAdapter(mCacheListAdapter);

        mUnknowListAdapter = new BindingListAdapter(R.layout.unkonw_item);
//        mUnknowListAdapter.setOnItemClickListener(new BindingListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, final int position) {
////                view.findViewById(R.id.btn_auth).setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Device device = (Device) mUnknowList.get(position);
////                        Intent detail = new Intent(MainActivity.this, DetailActivity.class);
////                        detail.putExtra(DetailActivity.EXTRAS_DEVICE_NAME, device.getName());
////                        detail.putExtra(DetailActivity.EXTRAS_DEVICE_ADDRESS, device.getMAC());
////                        detail.putExtra(DetailActivity.EXTRAS_DEVICE_RSSI, device.getID());
////                        startActivity(detail);
////                    }
////                });
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        });
        mUnknowListAdapter.setBindingData(mUnknowList);
        lvUnknow.setAdapter(mUnknowListAdapter);
        //lvUnknow.setLayoutManager(layoutManager);

        mBleWrapper = new BleWrapper(this, new BleWrapperUiCallbacks.Null() {
            @Override
            public void uiDeviceFound(BluetoothDevice device, int rssi, byte[] record) {
                handleFoundDevice(device, rssi, record);
            }
        });

        // check if we have BT and BLE on board
        if (!mBleWrapper.checkBleHardwareAvailable()) {
            bleMissing();
        }

        if (!mBleWrapper.isBtEnabled()) {
            btDisabled();
        }

        // on every Resume check if BT is enabled (user could turn it off while app was in background etc.)
    }

    @Override
    protected void onResume() {
        super.onResume();

        // initialize BleWrapper object
        if (view_status != STATUS_BLE_NON) {
            mBleWrapper.initialize();
            if (isFirstInit == 0) {
                isFirstInit = 1;
                setScaning(true);
            }
        }
    }

    /* check if user agreed to enable BT */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // user didn't want to turn on BT
        if (requestCode == ENABLE_BT_REQUEST_ID) {
            if (resultCode == Activity.RESULT_CANCELED) {
                btDisabled();
            } else if (resultCode == Activity.RESULT_OK) {
                setStatus(STATUS_NORMAL);
                setScaning(true);
            }
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    /* make sure that potential scanning will take no longer
     * than <SCANNING_TIMEOUT> seconds from now on */
    private void addScanningTimeout() {
        Runnable timeout = new Runnable() {
            @Override
            public void run() {
                if (mBleWrapper == null) return;
                mScanning = false;
                mBleWrapper.stopScanning();
                setScaning(false);
            }
        };
        mHandler.postDelayed(timeout, SCANNING_TIMEOUT);
    }

    /* add device to the current list of devices */
    private void handleFoundDevice(final BluetoothDevice device,
                                   final int rssi,
                                   final byte[] scanRecord) {
        // adding to the UI have to happen in UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Device n = new Device("", device.getName(), device.getAddress()), l;
                if (mDeviceList.contains(n)) {
                    l = (Device) mDeviceList.get(mDeviceList.indexOf(n));
                    mDeviceList.remove(l);
                    mDeviceList.add(0, l);
                    l.setEnable(true);
                    l.setListener(MainActivity.this);
                    mCacheListAdapter.notifyDataSetChanged();
                } else {
                    if (!mUnknowList.contains(n)) {
                        n.setEnable(true);
                        n.setListener(MainActivity.this);
                        mUnknowList.add(n);
                        mUnknowListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setStatus(int status) {
        if (normalLayout != null) {
            if (status == STATUS_BLE_NON) {
                normalLayout.setVisibility(View.GONE);
            } else if (status == STATUS_BT_OFF) {
                normalLayout.setVisibility(View.GONE);
            } else if (status == STATUS_NORMAL) {
                normalLayout.setVisibility(View.VISIBLE);
            }
            view_status = status;
        }
    }

    private void setScaning(boolean scaning) {
        this.mScanning = scaning;
        normalLayout.setRefreshing(scaning);
        btnScan.setEnabled(mScanning);
        btnScan.setText(!mScanning ? "扫描" : "扫描中...");
        if (mBleWrapper.isBtEnabled()) {
            if (scaning) {
                addScanningTimeout();
                mBleWrapper.startScanning();
            }
        }
        if (scaning && !mBleWrapper.isBtEnabled()) {
            // BT is not turned on - ask user to make it enabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
            // see onActivityResult to check what is the status of our request
        }
    }

    private void btDisabled() {
        Toast.makeText(this, "Sorry, BT has to be turned ON for us to work!", Toast.LENGTH_LONG).show();
        setStatus(STATUS_BT_OFF);
        setScaning(false);
    }

    private void bleMissing() {
        Toast.makeText(this, "BLE Hardware is required but not available!", Toast.LENGTH_LONG).show();
        setStatus(STATUS_BLE_NON);
    }

    @Override
    public void auth(Device device) {
        mDeviceList.add(0, device);
        mDeviceDB.addNewDevice(device.getMAC(), device.getName());
        mUnknowList.remove(device);
        mUnknowListAdapter.notifyDataSetChanged();
        mCacheListAdapter.notifyDataSetChanged();
    }

    @Override
    public void detail(Device device) {
        Intent detail = new Intent(MainActivity.this, DetailActivity.class);
        detail.putExtra(DetailActivity.EXTRAS_DEVICE_NAME, device.getName());
        detail.putExtra(DetailActivity.EXTRAS_DEVICE_ADDRESS, device.getMAC());
        detail.putExtra(DetailActivity.EXTRAS_DEVICE_RSSI, device.getID());
        startActivity(detail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
