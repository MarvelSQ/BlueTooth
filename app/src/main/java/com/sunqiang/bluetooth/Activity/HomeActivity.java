package com.sunqiang.bluetooth.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.sunqiang.bluetooth.ble.BleWrapper;
import com.sunqiang.bluetooth.ble.BleWrapperUiCallbacks;
import com.sunqiang.bluetooth.fragment.HomeFragment;
import com.sunqiang.bluetooth.R;

public class HomeActivity extends AppCompatActivity {

    private HomeFragment homeFragment=new HomeFragment();
    private BleWrapper mBleWrap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getFragmentManager().beginTransaction().add(R.id.frame_home,homeFragment).commit();
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bnb_home);
        bottomNavigationBar.setActiveColor(R.color.colorPrimary).setInActiveColor("#FFFFFF").setBarBackgroundColor("#00FFFF");
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.heart_filled, "Heart")).addItem(new BottomNavigationItem(R.drawable.heart_filled, "Heart")).addItem(new BottomNavigationItem(R.drawable.heart_filled, "Heart"));
        bottomNavigationBar.setFirstSelectedPosition(0).initialise();
        mBleWrap = new BleWrapper(this, new BleWrapperUiCallbacks.Null(){

        });
        mBleWrap.initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.device_list:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
