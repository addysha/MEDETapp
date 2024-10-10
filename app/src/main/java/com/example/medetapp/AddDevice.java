package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDevice extends AppCompatActivity {
    String deviceNickname;
    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_device);
    }

    public void onCancelDevice(View view){
        Intent intent = new Intent(this, DeviceList.class);
        startActivity(intent);
    }

    public void onSaveDevice(View view){
        deviceNickname = ((EditText)findViewById(R.id.device_nickname)).getText().toString();
        deviceID = ((EditText)findViewById(R.id.device_ID)).getText().toString();

        //Save to DB
        FirebaseUser currentUser = AppState.getAppState().getLoginManager().getFirebaseAuth().getCurrentUser();

        if (currentUser != null) {
            // Get a reference to the Firebase Database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            // Use deviceID as the key and save deviceNickname under it
            databaseReference.child("users").child(currentUser.getUid()).child("devices").child(deviceID).child("nickname").setValue(deviceNickname)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Log success message
                            Log.d("SaveDevice", "Device saved successfully with ID: " + deviceID);
                        } else {
                            // Log failure message
                            Log.e("SaveDevice", "Failed to save device: " + task.getException().getMessage());
                        }
                    });
        } else {
            Log.w("SaveDevice", "User not authenticated");
        }

        Intent intent = new Intent(this, DeviceList.class);
        startActivity(intent);
    }
}
