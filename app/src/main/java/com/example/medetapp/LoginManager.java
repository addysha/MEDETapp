package com.example.medetapp;

import java.util.ArrayList;

/* This class is responsible for checking if a user is logged in, logging in, validating pins and signing up
* the user
*/
public class LoginManager {


    //This method checks if there is a user logged into this device
    // in the event that there is the method populates the user field of appstate
    public boolean isUserStored(){

        User user = new User("test","user");

        //returning true for now
        AppState.getAppState().setUser(user);
        return true;
    }

    // this method verifies pins for the pin page.
    public boolean verifyPin(ArrayList<Integer> pin){

        // currently returning true always
        return  true;

    }


}
