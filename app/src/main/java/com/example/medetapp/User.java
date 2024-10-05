package com.example.medetapp;
import com.google.firebase.auth.FirebaseUser;

//This Class encapsulates all user information includinng FirebaseUser account
public class User {

    private String firstName;
    private String lastName;
    private FirebaseUser firebaseUser;

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }


}
