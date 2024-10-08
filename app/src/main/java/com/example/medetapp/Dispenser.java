package com.example.medetapp;

import java.util.ArrayList;

//This class encapsulates all data relating to dispensers
public class Dispenser {

    private final String deviceId;
    private ArrayList<User> users;
    private boolean lightsOn;
    private boolean soundOn;
    private int soundVolume;
    private ArrayList<DoseTime> doseTimes;


    public Dispenser(String deviceId, ArrayList<User> users, boolean lightsOn, boolean soundOn, int soundVolume,ArrayList<DoseTime> doseTimes){

        this.deviceId = deviceId;
        this.users = users;
        this.lightsOn = lightsOn;
        this.soundOn = soundOn;
        this.soundVolume = soundVolume;
        this.doseTimes = doseTimes;
    }

    /////// getters ///////
    public int getSoundVolume() {return soundVolume;}
    public String getDevice_id() {return deviceId;}
    public ArrayList<User> getUsers() {return users;}
    public boolean isSoundOn() {return soundOn;}
    public boolean isLightsOn() {return lightsOn;}
    public ArrayList<DoseTime> getDoseTimes() {return doseTimes;}


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

    public void removeDoseTime(DoseTime doseTimeToRemove){
        doseTimes.removeIf(doseTime -> doseTime.getTime().equals(doseTimeToRemove.getTime()));
    }

    //this method adds a DoseTime to the list making sure to keep it sorted
    public void addDoseTime(DoseTime doseTimeToAdd) throws Exception{

        for (DoseTime doseTime : doseTimes){
            if (doseTime.getTime().equals(doseTimeToAdd.getTime())){
                throw new Exception("This Time Already Has A Dose");
            }
        }

        for (int i = 0; i <= doseTimes.size(); i++ ){
            if (doseTimeToAdd.before(doseTimes.get(i))){
                doseTimes.add(i,doseTimeToAdd);
            }
        }


    }


}



