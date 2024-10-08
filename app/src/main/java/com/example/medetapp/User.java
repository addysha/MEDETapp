package com.example.medetapp;
import com.google.firebase.auth.FirebaseUser;

//This Class encapsulates all user information including FirebaseUser account
public class User {

    private String firstName;
    private String lastName;
    private final String userId;
    private FirebaseUser firebaseUser;

    public User(String firstName, String lastName,String userId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getUserId() {return userId;}
}
