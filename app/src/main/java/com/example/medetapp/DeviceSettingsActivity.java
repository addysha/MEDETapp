package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//This Class Implements the Device Settings Activity
public class DeviceSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.device_settings);
    }

    public void onclickCancelDeviceSettings(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onclickSaveDeviceSettings(View view){
        //TODO: save settings to DB

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
