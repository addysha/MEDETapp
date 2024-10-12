package com.example.medetapp;

import java.io.Serializable;

public class Medication implements Serializable {
    private String key;
    private String name;
    private String information;
    private String time;

    public Medication(String name, String information, String time){
        this.name = name;
        this.information = information;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getInformation() {
        return information;
    }

    public String getTime() {
        return time;
    }

    public void setDBKey(String key){
        this.key = key;
    }

    public String getDBKey(){
        return key;
    }
}
