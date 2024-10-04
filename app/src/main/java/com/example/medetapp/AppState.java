package com.example.medetapp;

import java.util.ArrayList;
import java.util.List;

public class AppState {

    private static AppState appState;

    private AppState(){

    }

    // singleton access to appstate
    public static AppState getAppState() {
        if (appState == null){
            appState = new AppState();
        }
        return appState;
    }


    //method used for development delete later
    public List<User> getUsers(){

        List<User> users = new ArrayList<User>();

        users.add(new User("john","jobs"));
        users.add(new User("zuck","burg"));

        return  users;
    }

}
