package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class AddMedicationActivity extends AppCompatActivity {
    private String medicationName;
    private String medicationInfo;
    private String medicationKey = null;
    private TimePicker medicationTimePicker;
    private Medication medication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medication);

        medicationTimePicker = findViewById(R.id.medication_time);
        medication = (Medication) getIntent().getSerializableExtra("medication");

        //If we are editing a medication instead of a new one
        if (medication != null) {
            TextView title = findViewById(R.id.medication_title);
            title.setText("Edit Medication");
            medicationKey = medication.getDBKey();
            ((EditText) findViewById(R.id.medication_name)).setText(medication.getName());
            ((EditText) findViewById(R.id.medication_info)).setText(medication.getInformation());
            String[] timeParts = medication.getTime().split(":");
            medicationTimePicker.setHour(Integer.parseInt(timeParts[0]));
            medicationTimePicker.setMinute(Integer.parseInt(timeParts[1]));
        }
    }

    public void onSaveMedication(View view) {
        // Get medication details from EditText and TimePicker
        medicationName = ((EditText)findViewById(R.id.medication_name)).getText().toString();
        medicationInfo = ((EditText)findViewById(R.id.medication_info)).getText().toString();

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
            medication = new Medication(medicationName, medicationInfo, medicationTime);

            // Generate a unique key for the medication
            if(medicationKey == null){
                medicationKey = databaseReference.child("devices")
                        .child(AppState.getAppState().getCurrentDevice().getDeviceID()) //getting device ID
                        .child("medications").push().getKey();
            }

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
