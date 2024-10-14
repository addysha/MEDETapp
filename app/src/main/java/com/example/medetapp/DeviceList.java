package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeviceList extends AppCompatActivity implements DeviceAdapter.OnDeviceClickListener{
    private RecyclerView recyclerDevices;
    private DeviceAdapter deviceAdapter;
    private List<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.device_list);


        deviceList = new ArrayList<>();
        recyclerDevices = findViewById(R.id.recycler_devices);
        recyclerDevices.setLayoutManager(new LinearLayoutManager(this));

        //Call for all devices associated with this user from the DB
        fetchUserDevices();

    }

    private void fetchUserDevices() {
        FirebaseUser currentUser = AppState.getAppState().getLoginManager().getFirebaseAuth().getCurrentUser();
        if (currentUser == null) {
            Log.e("DeviceList", "User not authenticated");
            return;
        }

        DatabaseReference userDevicesRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("devices");

        userDevicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Clear to prevent duplicates on re-loading
                deviceList.clear();

                //For each node retrieved, create a device and add it to a list of devices
                for (DataSnapshot deviceSnapshot : dataSnapshot.getChildren()) {
                    String deviceId = deviceSnapshot.getKey();
                    String deviceNickname = deviceSnapshot.child("nickname").getValue(String.class);
                    deviceList.add(new Device(deviceId, deviceNickname));
                }

                deviceAdapter = new DeviceAdapter(deviceList, DeviceList.this);
                recyclerDevices.setAdapter(deviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DeviceList", "Failed to read devices: " + databaseError.getMessage());
            }
        });

    }

    @Override
    public void onDeviceClick(Device device) {
        AppState.getAppState().setCurrentDevice(device);
        //Fetch the devices settings
        AppState.getAppState().getCurrentDevice().fetchDeviceSettings();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeviceDelete(Device device) {
        //Get reference to the devices from currently logged in user
        DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference("users")
                .child(Objects.requireNonNull(AppState.getAppState().getLoginManager().getFirebaseAuth().getUid()))
                .child("devices");

        //Delete the device and recall to fetch and display
        deviceRef.child(device.getDeviceID()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                fetchUserDevices();
                Log.d("DeviceList", "Device deleted successfully: " + device.getDeviceID());
            } else {
                Log.e("DeviceList", "Failed to delete device: " + task.getException());
            }
        });
    }

    public void onAddDevice(View view){
        Intent intent = new Intent(this, AddDevice.class);
        startActivity(intent);
    }

    public void onclickProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
