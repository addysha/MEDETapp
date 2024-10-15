package com.example.medetapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.os.CountDownTimer;

import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;




public class MainActivity extends AppCompatActivity {

    CountDownTimer countDownTimer;
    Device currentDevice;
    ArrayList<Medication> medications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView nicknameDisplay = findViewById(R.id.device_nickname_display);

        // Get and display the current device's nickname
        nicknameDisplay.setText(AppState.getAppState().getCurrentDevice().getDeviceNickname());



       fetchMedications();
    }

    public void fetchMedications() {
        FirebaseUser currentUser = AppState.getAppState().getLoginManager().getFirebaseAuth().getCurrentUser();
        if (currentUser == null) {
            Log.e("MedicationList", "User not authenticated");
            return;
        }

        DatabaseReference userDevicesRef = FirebaseDatabase.getInstance().getReference("devices")
                .child(AppState.getAppState().getCurrentDevice().getDeviceID()) // get device id
                .child("medications");

        userDevicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear to prevent duplicates on re-loading

                medications.clear();

                // For each node retrieved, create a Medication and set its key
                for (DataSnapshot medicationSnapshot : dataSnapshot.getChildren()) {
                    String key = medicationSnapshot.getKey(); // Get the key
                    String medicationName = medicationSnapshot.child("name").getValue(String.class);
                    String medicationInfo = medicationSnapshot.child("information").getValue(String.class);
                    String medicationTime = medicationSnapshot.child("time").getValue(String.class);

                    // Create the Medication object
                    Medication medication = new Medication(medicationName, medicationInfo, medicationTime);
                    medication.setDBKey(key); // Set the key

                    medications.add(medication);
                }

                // Sort the list of medications by time
                medications.sort(Comparator.comparing(Medication::getTime));
                setCountdown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MedicationList", "Failed to read medications: " + databaseError.getMessage());
            }
        });
    }

    private void setCountdown(){


        //make sure there are actually medications
        if(medications.isEmpty()){
            TextView countdownText = findViewById(R.id.countdown_text);
            countdownText.setText("No Medication Set");
            return;
        }

        Medication nextMedication = getNextMedication(medications);
        long timeLeftInMillis = calculateTimeUntilMedication(nextMedication);

        // Start the countdown timer
        startCountdown(timeLeftInMillis);

        Log.d("Main", "Medications fetched, countdown started");


        Log.d("onCreate", "Fetching medications asynchronously");
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

    /* Method to begin the countdown once the app is launched
    * And restarting the timer after it finishes
    * */
    private void startCountdown(long timeLeftInMillis) {


        TextView countdownText = findViewById(R.id.countdown_text);


        // Start the countdown timer
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) { // Count down every second
            @Override
            public void onTick(long millisUntilFinished) {
                // Calculate hours, minutes, and seconds
                int hours = (int) (millisUntilFinished / (1000 * 60 * 60)) % 24;
                int minutes = (int) (millisUntilFinished / (1000 * 60)) % 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                // Format and display the time
                countdownText.setText(String.format(new Locale("en", "NZ"), "%02d:%02d:%02d", hours, minutes, seconds));
            }

            //Once timer has finished assume that pill was taken on time and start next timer
            @Override
            public void onFinish() {

                //calculate time till next medication and start the timer
                ArrayList<Medication> medications = currentDevice.getMedications();
                Medication nextMedication = getNextMedication(medications);
                long timeLeftInMillis = calculateTimeUntilMedication(nextMedication);

                startCountdown(timeLeftInMillis);


            }
        }.start();
    }


    // Helper method to calculate time until the next medication
    private long calculateTimeUntilMedication(Medication medication) {
        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Parse medication time
        String[] timeParts = medication.getTime().split(":");
        int medHour = Integer.parseInt(timeParts[0]);
        int medMinute = Integer.parseInt(timeParts[1]);

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, medHour);
        calendar.set(Calendar.MINUTE, medMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long medicationTimeMillis = calendar.getTimeInMillis();

        // Calculate the time until the next medication in milliseconds
        long timeDifference = medicationTimeMillis - currentTimeMillis;

        return timeDifference >= 0 ? timeDifference : 0; // Return 0 if negative
    }

    //Returns a medication object and the time until the medication should be taken for the next medication that should be taken
    private Medication getNextMedication(ArrayList<Medication> medicationList) {
        long currentTimeMillis = System.currentTimeMillis();

        Medication nextMedication = null;
        long shortestTimeDifference = Long.MAX_VALUE;

        // Loop through medication list finding the next time for medication
        for (Medication medication : medicationList) {
            String[] timeParts = medication.getTime().split(":");
            int medHour = Integer.parseInt(timeParts[0]);
            int medMinute = Integer.parseInt(timeParts[1]);

            // Create a Calendar instance to set the medication time for today
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, medHour);
            calendar.set(Calendar.MINUTE, medMinute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long medicationTimeMillis = calendar.getTimeInMillis();

            // If the medication time is before the current time, add 24 hours to get the next occurrence
            if (medicationTimeMillis < currentTimeMillis) {
                medicationTimeMillis += 24 * 60 * 60 * 1000; // Add 24 hours
            }

            long timeDifference = medicationTimeMillis - currentTimeMillis;

            if (timeDifference >= 0 && timeDifference < shortestTimeDifference) {
                shortestTimeDifference = timeDifference;
                nextMedication = medication;
            }
        }
        return nextMedication;
    }



    public void onclickBackToDeviceList(View view){
        Intent intent = new Intent(this, DeviceList.class);
        startActivity(intent);
        finish();
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