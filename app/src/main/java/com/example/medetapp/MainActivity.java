package com.example.medetapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView nicknameDisplay = findViewById(R.id.device_nickname_display);

        //Get and display the current device's nickname
        nicknameDisplay.setText(AppState.getAppState().getCurrentDevice().getDeviceNickname());

        monitorDeviceStatus(AppState.getAppState().getCurrentDevice().getDeviceID());
    }

    //Method to check if a notification needs to be sent
    private void monitorDeviceStatus(String deviceId) {
        DatabaseReference deviceStatusRef = FirebaseDatabase.getInstance()
                .getReference("devices")
                .child(deviceId)
                .child("missedMedication");

        deviceStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean alertStatus = dataSnapshot.getValue(Boolean.class);
                if (alertStatus != null && alertStatus) {
                    sendLocalNotification(AppState.getAppState().getCurrentDevice().getDeviceNickname() + " Missed Their Medication",
                            AppState.getAppState().getCurrentDevice().getDeviceNickname() + " has missed their medication.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DeviceList", "Failed to read device status: " + databaseError.getMessage());
            }
        });
    }

    //Method to create and push the notification to the user
    private void sendLocalNotification(String title, String message) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "device_alerts",
                    "Device Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "device_alerts")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("DeviceList", "Notification permissions denied");
            return;
        }
        notificationManager.notify(1001, builder.build());
    }

    public void onclickBackToDeviceList(View view){
        Intent intent = new Intent(this, DeviceList.class);
        startActivity(intent);
    }

    public void deviceSettingsButtonClick(View view){

        Intent intent = new Intent(this, DeviceSettingsActivity.class);
        startActivity(intent);
    }

    public void dosagePlanButtonClick(View view){
        Intent intent = new Intent(this, SetDosesActivity.class);
        startActivity(intent);
    }

    public void userSettingsButtonClick(View view){
        Intent intent = new Intent(this, ManageUsersActivity.class);
        startActivity(intent);
    }
}