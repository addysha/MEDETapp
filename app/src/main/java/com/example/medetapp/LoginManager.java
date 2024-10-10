package com.example.medetapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/* This class is responsible for checking if a user is logged in, logging in, validating pins and signing up
* the user
*/
public class LoginManager {


    FirebaseAuth auth;


    public LoginManager(){
        auth = FirebaseAuth.getInstance();
    }


    //This method checks if there is a user logged into this device
    // in the event that there is, the method populates the user field of appstate
    public boolean isUserLoggedIn(){
        FirebaseUser currentUser = auth.getCurrentUser();
        return (currentUser != null);
    }

    // this method verifies pins for the pin page.
    public boolean verifyPin(ArrayList<Integer> pin){

        // currently returning true always
        return  true;

    }


    public FirebaseAuth getFirebaseAuth() {return auth;}
}
