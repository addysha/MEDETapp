package com.example.medetapp;

public class Device {
    private final String deviceNickname;
    private final String deviceID;

    Device(String deviceID, String deviceNickname){
        this.deviceID = deviceID;
        this.deviceNickname = deviceNickname;
    }

    public String getDeviceID(){
        return this.deviceID;
    }

    public String getDeviceNickname(){
        return this.deviceNickname;
    }
}
