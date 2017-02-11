package com.sunqiang.bluetooth.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by sunqiang on 2017/2/11.
 */

public class CustomLinearManager extends LinearLayoutManager {

    public boolean isScrollable = true;

    public CustomLinearManager(Context context) {
        super(context);
    }

    public void setScrollEnable(boolean scrollEnable){
        this.isScrollable=scrollEnable;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollable&&super.canScrollVertically();
    }
}
