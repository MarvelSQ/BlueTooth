package com.sunqiang.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sunqiang on 2017/2/8.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {

    private ArrayList<BluetoothDevice> devices;
    private ArrayList<Integer> rssis;
    private ArrayList<byte[]> records;
    private LayoutInflater inflater;

    public DeviceListAdapter(Context context) {
        devices = new ArrayList<>();
        rssis = new ArrayList<>();
        records = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void addDevice(BluetoothDevice device, int rssi, byte[] record) {
        if (!devices.contains(device)) {
            devices.add(device);
            rssis.add(rssi);
            records.add(record);
        }
    }

    public BluetoothDevice getDevice(int index) {
        return devices.get(index);
    }

    public int getRssi(int index) {
        return rssis.get(index);
    }

    public void clearList() {
        devices.clear();
        records.clear();
        rssis.clear();
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DeviceViewHolder viewHolder = new DeviceViewHolder(inflater.inflate(R.layout.device_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        String name = devices.get(position).getName();
        name = name == null ? "N/A" : name.equals("")?"N/A":name;
        holder.name.setText(name);
        holder.address.setText(devices.get(position).getAddress());
        holder.rssi.setText(rssis.get(position) + "");
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
            address = (TextView) itemView.findViewById(R.id.device_address);
            rssi = (TextView) itemView.findViewById(R.id.device_rssi);
        }
    }
}
