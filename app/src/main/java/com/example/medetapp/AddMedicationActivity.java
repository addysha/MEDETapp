package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMedicationActivity extends AppCompatActivity {
    private String medicationName;
    private String medicationInfo;
    private TimePicker medicationTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medication);
    }

    public void onSaveMedication(View view) {
        // Get medication details from EditText and TimePicker
        String medicationName = ((EditText)findViewById(R.id.medication_name)).getText().toString();
        String medicationInfo = ((EditText)findViewById(R.id.medication_info)).getText().toString();
        TimePicker medicationTimePicker = findViewById(R.id.medication_time);

        // Get the hour and minute values from the timepicker
        int hour = medicationTimePicker.getHour();
        int minute = medicationTimePicker.getMinute();
        String medicationTime = String.format("%02d:%02d", hour, minute);

        // Save to DB
        FirebaseUser currentUser = AppState.getAppState().getLoginManager().getFirebaseAuth().getCurrentUser();

        if (currentUser != null) {
            // Get db ref
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            // Create a new Medication object with the retrieved info
            Medication medication = new Medication(medicationName, medicationInfo, medicationTime);

            // Generate a unique key for the medication
            String medicationKey = databaseReference.child("devices")
                    .child(AppState.getAppState().getCurrentDevice().getDeviceID()) //getting device ID
                    .child("medications").push().getKey();

            if (medicationKey != null) {
                // Save medication under the user's node
                databaseReference.child("devices")
                        .child(AppState.getAppState().getCurrentDevice().getDeviceID()) //getting device ID
                        .child("medications")
                        .child(medicationKey).setValue(medication)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Log success message
                                Log.d("SaveMedication", "Medication saved successfully: " + medicationKey);
                            } else {
                                // Log failure message
                                Log.e("SaveMedication", "Failed to save medication: " + task.getException());
                            }
                        });
            }
        } else {
            Log.w("SaveMedication", "User not authenticated");
        }

        Intent intent = new Intent(this, SetDosesActivity.class);
        startActivity(intent);
    }

    public void onCancelMedication(View view){
        Intent intent = new Intent(this, SetDosesActivity.class);
        startActivity(intent);
    }
}
