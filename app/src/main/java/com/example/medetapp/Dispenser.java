package com.example.medetapp;

import java.util.ArrayList;

//This class encapsulates all data relating to dispensers
public class Dispenser {

    private final String deviceId;
    private ArrayList<User> users;
    private boolean lightsOn;
    private boolean soundOn;
    private int soundVolume;


    public Dispenser(String deviceId, ArrayList<User> users, boolean lightsOn, boolean soundOn, int soundVolume){

        this.deviceId = deviceId;
        this.users = users;
        this.lightsOn = lightsOn;
        this.soundOn = soundOn;
        this.soundVolume = soundVolume;
    }

    /////// getters ///////
    public int getSoundVolume() {return soundVolume;}
    public String getDevice_id() {return deviceId;}
    public ArrayList<User> getUsers() {return users;}
    public boolean isSoundOn() {return soundOn;}
    public boolean isLightsOn() {return lightsOn;}

    /////// setters /////////


    public void setLightsOn(boolean lightsOn) {this.lightsOn = lightsOn;}
    public void setSoundOn(boolean soundOn) {this.soundOn = soundOn;}
    public void setSoundVolume(int soundVolume) {this.soundVolume = soundVolume;}

    public void removeUser(User userToRemove) {
        users.removeIf(user -> user.getUserId().equals(userToRemove.getUserId()));
    }
    public void addUser(User user){
        users.add(user);
    }
}



