package com.example.medetapp;

import java.util.ArrayList;
import java.util.List;


//singleton class for accessing all relevant objects
public class AppState {

    private static AppState appState;
    private User user;
    private final LoginManager  loginManager;

    private AppState(){

        loginManager = new LoginManager();

    }

    // singleton access to AppState
    public static AppState getAppState() {
        if (appState == null){
            appState = new AppState();
        }
        return appState;
    }


    //method used for development delete later
    public List<User> getUsers(){

        List<User> users = new ArrayList<User>();

        users.add(new User("john","jobs","12"));
        users.add(new User("zuck","burg","14"));

        return  users;
    }



    //////// getters and setters ////////////////
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LoginManager getLoginManager(){
        return loginManager;
    }
}
