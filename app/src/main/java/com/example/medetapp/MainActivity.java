package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView nicknameDisplay = findViewById(R.id.device_nickname_display);

        //Get and display the current device's nickname
        nicknameDisplay.setText(AppState.getAppState().getCurrentDevice().getDeviceNickname());

    }

    public void onclickBackToDeviceList(View view){
        Intent intent = new Intent(this, DeviceList.class);
        startActivity(intent);
    }

    public void deviceSettingsButtonClick(View view){

        Intent intent = new Intent(this, DeviceSettingsActivity.class);
        startActivity(intent);
    }

    public void dosagePlanButtonClick(View view){
        Intent intent = new Intent(this, SetDosesActivity.class);
        startActivity(intent);
    }

    public void userSettingsButtonClick(View view){
        Intent intent = new Intent(this, ManageUsersActivity.class);
        startActivity(intent);
    }
}