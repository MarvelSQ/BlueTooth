package com.sunqiang.bluetooth.Adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunqiang.bluetooth.Ble.BleNamesResolver;
import com.sunqiang.bluetooth.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sunqiang on 2017/2/9.
 */

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder> {

    private ArrayList<BluetoothGattService> services;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> serviceCharaters;

    private LayoutInflater inflater;

    private DeviceListAdapter.OnItemClickLitener mOnItemClickLitener;

    public ServiceListAdapter(Context context) {
        services = new ArrayList<>();
        serviceCharaters = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        inflater = LayoutInflater.from(context);
    }

    public void addCharacteristicForServcie(BluetoothGattService service,List<BluetoothGattCharacteristic> chars){
        if(services.contains(service)){
            int i = services.indexOf(service);
            serviceCharaters.add(i, (ArrayList<BluetoothGattCharacteristic>) chars);
        }
    }

    public void addService(BluetoothGattService service) {
        services.add(service);
    }

    public void addServices(List<BluetoothGattService> serviceList){
        services= (ArrayList<BluetoothGattService>) serviceList;
    }

    public BluetoothGattService getService(int index) {
        return services.get(index);
    }

    public void clearList() {
        services.clear();
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ServiceListAdapter.ServiceViewHolder viewHolder = new ServiceViewHolder(inflater.inflate(R.layout.service_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ServiceViewHolder holder, int position) {
        BluetoothGattService service = services.get(position);
        String uuid = service.getUuid().toString().toLowerCase(Locale.getDefault());
        String name = BleNamesResolver.resolveServiceName(uuid);
        holder.name.setText(name);
        holder.uuid.setText(uuid);
        if(serviceCharaters.get(position)!=null){
            for(BluetoothGattCharacteristic characteristic:serviceCharaters.get(position)){
                TextView tv=new TextView(holder.itemView.getContext());
                String cuuid = characteristic.getUuid().toString().toLowerCase(Locale.getDefault());
                name=BleNamesResolver.resolveCharacteristicName(cuuid);
                tv.setText(name);
                holder.container.addView(tv);
            }
        }
        if(mOnItemClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(v,pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(v,pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {

        TextView name, uuid;

        LinearLayout container;

        ServiceViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.service_name);
            uuid = (TextView) itemView.findViewById(R.id.service_uuid);
            container= (LinearLayout) itemView.findViewById(R.id.char_container);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
