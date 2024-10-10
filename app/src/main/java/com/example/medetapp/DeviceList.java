package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DeviceList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.device_list);

        //Call for all devices associated with this user from the DB
        

    }

    public void onAddDevice(View view){
        Intent intent = new Intent(this, AddDevice.class);
        startActivity(intent);
    }
}
