package com.example.medetapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<Device> deviceList;
    private OnDeviceClickListener onDeviceClickListener;

    public DeviceAdapter(List<Device> deviceList, OnDeviceClickListener onDeviceClickListener) {
        this.deviceList = deviceList;
        this.onDeviceClickListener = onDeviceClickListener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.deviceNickname.setText(device.getDeviceNickname());

        // Set the device ID in the TextView
        holder.deviceId.setText(device.getDeviceID());

        // Set the click listener for the delete button
        holder.buttonDelete.setOnClickListener(v -> {
            onDeviceClickListener.onDeviceDelete(device);
        });

        holder.itemView.setOnClickListener(v -> onDeviceClickListener.onDeviceClick(device));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView deviceNickname;
        TextView deviceId;
        ImageButton buttonDelete;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceNickname = itemView.findViewById(R.id.device_nickname);
            deviceId = itemView.findViewById(R.id.device_id);
            buttonDelete = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnDeviceClickListener {
        void onDeviceClick(Device device);
        void onDeviceDelete(Device device);
    }
}

