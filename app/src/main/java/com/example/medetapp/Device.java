package com.example.medetapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Optional;

public class Device {
    private final String deviceNickname;
    private final String deviceID;

    private boolean medicationTaken = true;
    private boolean alertLights;
    private boolean missedMedication;
    private boolean sendAlerts;
    private boolean alarm;
    private float timeUntilAlert;
    private float alarmVolume;

    private ArrayList<Medication> medications;

    Device(String deviceID, String deviceNickname){
        this.deviceID = deviceID;
        this.deviceNickname = deviceNickname;
    }

    /* Method to fetch all the device settings from the db and update in real time */
    public void fetchDeviceSettings() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("devices")
                .child(deviceID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alertLights = Optional.ofNullable(dataSnapshot.child("alertLights").getValue(Boolean.class)).orElse(true);
                missedMedication = Optional.ofNullable(dataSnapshot.child("missedMedication").getValue(Boolean.class)).orElse(false);
                sendAlerts = Optional.ofNullable(dataSnapshot.child("sendAlerts").getValue(Boolean.class)).orElse(true);
                timeUntilAlert = Optional.ofNullable(dataSnapshot.child("timeBeforeAlert").getValue(Float.class)).orElse(30.0f);
                alarm = Optional.ofNullable(dataSnapshot.child("alarm").getValue(Boolean.class)).orElse(true);
                alarmVolume = Optional.ofNullable(dataSnapshot.child("alarmVolume").getValue(Float.class)).orElse(0.5f);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DeviceState", "Failed to fetch device settings: " + databaseError.getMessage());
            }
        });
    }

    public String getDeviceID(){
        return this.deviceID;
    }

    public String getDeviceNickname(){
        return this.deviceNickname;
    }

    public boolean getMedicationTaken(){
        return medicationTaken;
    }

    public boolean isAlertLights() {
        return alertLights;
    }

    public boolean isMissedMedication() {
        return missedMedication;
    }

    public boolean isSendAlerts() {
        return sendAlerts;
    }

    public float getTimeUntilAlert() {
        return timeUntilAlert;
    }

    public float getAlarmVolume() {
        return alarmVolume;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlertLights(boolean alertLights) {
        this.alertLights = alertLights;
    }

    public void setMissedMedication(boolean missedMedication) {
        this.missedMedication = missedMedication;
    }

    public void setSendAlerts(boolean sendAlerts) {
        this.sendAlerts = sendAlerts;
    }

    public void setTimeUntilAlert(float timeUntilAlert) {
        this.timeUntilAlert = timeUntilAlert;
    }

    public void setAlarmVolume(float alarmVolume) {
        this.alarmVolume = alarmVolume;
    }

    public void setAlarm(Boolean alarm) {
        this.alarm = alarm;
    }
}
