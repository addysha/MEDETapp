package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SetDosesActivity extends AppCompatActivity {
    private RecyclerView recyclerDevices;
    private MedicationAdapter medicationAdapter;
    private List<Medication> medicationList;
    private List<String> medicationKeys; // List to hold medication keys

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_doses);

        medicationList = new ArrayList<>();
        medicationKeys = new ArrayList<>();
        recyclerDevices = findViewById(R.id.medications);
        recyclerDevices.setLayoutManager(new LinearLayoutManager(this));

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
                medicationList.clear();

                // For each node retrieved, create a Medication and set its key
                for (DataSnapshot medicationSnapshot : dataSnapshot.getChildren()) {
                    String key = medicationSnapshot.getKey(); // Get the key
                    String medicationName = medicationSnapshot.child("name").getValue(String.class);
                    String medicationInfo = medicationSnapshot.child("information").getValue(String.class);
                    String medicationTime = medicationSnapshot.child("time").getValue(String.class);

                    // Create the Medication object
                    Medication medication = new Medication(medicationName, medicationInfo, medicationTime);
                    medication.setDBKey(key); // Set the key

                    medicationList.add(medication);
                }

                // Sort the list of medications by time
                medicationList.sort(Comparator.comparing(Medication::getTime));

                Log.w("SET MED", "SET MED : " + medicationList.get(0).getName());

                medicationAdapter = new MedicationAdapter(medicationList, SetDosesActivity.this, SetDosesActivity.this);
                recyclerDevices.setAdapter(medicationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MedicationList", "Failed to read medications: " + databaseError.getMessage());
            }
        });
    }

    public void onAddButtonClick(View view) {
        Intent intent = new Intent(this, AddMedicationActivity.class);
        startActivity(intent);
    }

    public void onclickBackToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}





