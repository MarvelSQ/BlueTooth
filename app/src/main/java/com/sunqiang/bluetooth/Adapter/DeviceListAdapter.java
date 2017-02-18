package com.sunqiang.bluetooth.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunqiang.bluetooth.R;

import java.util.ArrayList;

/**
 * Created by sunqiang on 2017/2/8.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {

    private ArrayList<BluetoothDevice> devices;
    private ArrayList<Integer> rssis;
    private ArrayList<byte[]> records;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickListener = mOnItemClickLitener;
    }

    public DeviceListAdapter() {
        devices = new ArrayList<>();
        rssis = new ArrayList<>();
        records = new ArrayList<>();
    }

    public void addDevice(BluetoothDevice device, int rssi, byte[] record) {
        if (!devices.contains(device)) {
            devices.add(device);
            rssis.add(rssi);
            records.add(record);
        }else {
            int pos=devices.indexOf(device);
            rssis.remove(pos);
            records.remove(pos);
            rssis.add(pos,rssi);
            records.add(pos,record);
        }
    }

    public BluetoothDevice getDevice(int index) {
        return devices.get(index);
    }

    public int getRssi(int index) {
        return rssis.get(index);
    }

    public ArrayList<BluetoothDevice> getDevices(){
        return devices;
    }

    public void clearList() {
        devices.clear();
        records.clear();
        rssis.clear();
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DeviceViewHolder viewHolder = new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, final int position) {
        String name = devices.get(position).getName();
        name = name == null ? "N/A" : name.equals("")?"N/A":name;
        holder.name.setText(name);
        holder.address.setText(devices.get(position).getAddress());
        holder.rssi.setText(rssis.get(position) + "");
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v,pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(v,pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {

        TextView name, address, rssi;

        DeviceViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.device_name);
            address = (TextView) itemView.findViewById(R.id.device_battery);
            rssi = (TextView) itemView.findViewById(R.id.device_code);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
}
