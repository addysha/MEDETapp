package com.example.medetapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//This Class implements the Manage Users Activity
public class ManageUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        recyclerView = findViewById(R.id.users_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        users = AppState.getAppState().getUsers();

        Log.d("help",Integer.toString(users.size()));

        userListAdapter = new UserListAdapter(users);
        recyclerView.setAdapter(userListAdapter);
    }

    public void onSaveButtonClick(View view){

    }

    public void onAddButtonClick(View view){

    }
}
