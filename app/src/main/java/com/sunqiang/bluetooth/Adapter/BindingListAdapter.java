package com.sunqiang.bluetooth.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;

/**
 * Created by sunqiang on 2017/2/13.
 */

public class BindingListAdapter extends RecyclerView.Adapter<BindingListAdapter.BindingHolder> {

    private ArrayList bindingData;
    private int layoutId;
    private BindingListAdapter.OnItemClickListener mOnItemClickListener;
    private int[] viewIDs;
    private View.OnClickListener[] listeners;

    public BindingListAdapter(int layoutId) {
        this.bindingData = new ArrayList<>();
        this.layoutId = layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public ArrayList getBindingData() {
        return bindingData;
    }

    public void clearData(){
        bindingData.clear();
        notifyDataSetChanged();
    }

    public Object getData(int index) {
        if (index < bindingData.size()) {
            return bindingData.get(index);
        }
        return null;
    }

    public void setBindingData(ArrayList bindingData) {
        this.bindingData = bindingData;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, int position) {
        holder.getBinding().setVariable(BR.item, bindingData.get(position));
        holder.getBinding().executePendingBindings();
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(v, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bindingData.size();
    }

    class BindingHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        public BindingHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
