package com.example.medetapp;

import java.util.ArrayList;

public class Device {
    private final String deviceNickname;
    private final String deviceID;

    private boolean lightsOn;
    private boolean soundOn;
    private int soundVolume;

    private ArrayList<Medication> medications;

    Device(String deviceID, String deviceNickname){
        this.deviceID = deviceID;
        this.deviceNickname = deviceNickname;

        //default values
        lightsOn = false;
        soundOn = true;
        soundVolume = 100;
    }

    public String getDeviceID(){
        return this.deviceID;
    }

    public String getDeviceNickname(){
        return this.deviceNickname;
    }

    /////// getters ///////
    public int getSoundVolume() {return soundVolume;}
    public boolean isSoundOn() {return soundOn;}
    public boolean isLightsOn() {return lightsOn;}


    /////// setters /////////
    public void setLightsOn(boolean lightsOn) {this.lightsOn = lightsOn;}
    public void setSoundOn(boolean soundOn) {this.soundOn = soundOn;}
    public void setSoundVolume(int soundVolume) {this.soundVolume = soundVolume;}
}
