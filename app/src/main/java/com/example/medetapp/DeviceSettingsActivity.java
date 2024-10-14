package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//This Class Implements the Device Settings Activity
public class DeviceSettingsActivity extends AppCompatActivity {
    private Switch alarmSoundSwitch;
    private Switch alertLightsSwitch;
    private Switch sendAlertsSwitch;
    private Slider alarmVolumeSlider;
    private Slider timeBeforeAlertSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.device_settings);

        //initialise views
        alarmSoundSwitch = findViewById(R.id.alarm_sound_switch);
        alertLightsSwitch = findViewById(R.id.alert_lights_switch);
        sendAlertsSwitch = findViewById(R.id.send_alerts_switch);
        alarmVolumeSlider = findViewById(R.id.alarm_volume_slider);
        timeBeforeAlertSlider = findViewById(R.id.time_before_alert_slider);

        alarmSoundSwitch.setChecked(AppState.getAppState().getCurrentDevice().isAlarm());
        alertLightsSwitch.setChecked(AppState.getAppState().getCurrentDevice().isAlertLights());
        sendAlertsSwitch.setChecked(AppState.getAppState().getCurrentDevice().isSendAlerts());
        alarmVolumeSlider.setValue(AppState.getAppState().getCurrentDevice().getAlarmVolume());
        timeBeforeAlertSlider.setValue(AppState.getAppState().getCurrentDevice().getTimeUntilAlert());
    }

    public void onclickCancelDeviceSettings(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onclickSaveDeviceSettings(View view) {
        // Retrieve the current device from AppState
        Device currentDevice = AppState.getAppState().getCurrentDevice();

        if (currentDevice != null) {
            // Retrieve values from UI components
            boolean alarm = alarmSoundSwitch.isChecked();
            boolean alertLights = alertLightsSwitch.isChecked();
            boolean sendAlerts = sendAlertsSwitch.isChecked();
            float alarmVolume = alarmVolumeSlider.getValue();
            float timeBeforeAlert = timeBeforeAlertSlider.getValue();

            // Save the settings to the database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("devices")
                    .child(currentDevice.getDeviceID());

            HashMap<String, Object> settingsMap = new HashMap<>();
            settingsMap.put("alarm", alarm);
            settingsMap.put("alertLights", alertLights);
            settingsMap.put("sendAlerts", sendAlerts);
            settingsMap.put("alarmVolume", alarmVolume);
            settingsMap.put("timeBeforeAlert", timeBeforeAlert);

            databaseReference.updateChildren(settingsMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("DeviceSettings", "Settings successfully saved");
                        } else {
                            Log.e("DeviceSettings", "Failed to save device settings: " + task.getException());
                        }
                    });
        } else {
            Log.e("DeviceSettings", "No current device found");
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
