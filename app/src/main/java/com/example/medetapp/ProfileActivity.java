package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
    }

    public void onclickLogout(View view){
        AppState.getAppState().getLoginManager().getFirebaseAuth().signOut();

        Intent intent = new Intent(this, PinActivity.class);
        startActivity(intent);
        finish();
    }

    public void onclickBackToDeviceList(View view){
        Intent intent = new Intent(this, DeviceList.class);
        startActivity(intent);
    }
}
