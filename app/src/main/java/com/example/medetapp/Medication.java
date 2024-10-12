package com.example.medetapp;

public class Medication {
    private String name;
    private String information;
    private String time;

    public Medication(String name, String information, String time){
        this.name = name;
        this.information = information;
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public String getInformation() {
        return this.information;
    }

    public String getTime() {
        return this.time;
    }
}
