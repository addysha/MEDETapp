package com.example.medetapp;

//This Class encapsulates all the data to do with  dosage times
public class DoseTime {
    private String time;
    private String description;

    public DoseTime(String time, String description) {
        this.time = time;
        this.description = description;
    }

    //// Getters /////////
    public String getTime() {return time;}
    public String getDescription(){return description;}

    //this method determines if the doseTime provided occurs before this doseTime
    public boolean before(DoseTime doseTime){
        int time1 = convertTo24Hour();
        int time2 = doseTime.convertTo24Hour();
        return (time2<time1);
    }

    //Converts the string text into 24 hour time in integer form
    private int convertTo24Hour(){
        String[] parts = time.split(" ");
        String ampm = parts[1];
        parts = parts[0].split(",");

        int intTime = Integer.parseInt(parts[0]+parts[1]);

        // if the time is in the pm and not in the 12pm hour add 1200 to the number
        if(ampm.equals("pm") && intTime < 1200){
            intTime += 1200;
        }
        // if 12 am in the morning want to convert number to 00am
        if (ampm.equals("am") && intTime>=1200){
            intTime -= 1200;
        }

        return intTime;
    }
}
