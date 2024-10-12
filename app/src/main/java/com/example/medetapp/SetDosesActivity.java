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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SetDosesActivity extends AppCompatActivity {
    private RecyclerView recyclerDevices;
    private MedicationAdapter medicationAdapter;
    private List<Medication> medicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_doses);

        medicationList = new ArrayList<>();
        recyclerDevices = findViewById(R.id.medications);
        recyclerDevices.setLayoutManager(new LinearLayoutManager(this));

        //Call for all devices associated with this user from the DB
        fetchMedications();
    }

    private void fetchMedications() {
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
                //Clear to prevent duplicates on re-loading
                medicationList.clear();

                //For each node retrieved, create a device and add it to a list of devices
                for (DataSnapshot medicationSnapshot : dataSnapshot.getChildren()) {
                    String medicationName = medicationSnapshot.child("name").getValue(String.class);
                    String medicationInfo = medicationSnapshot.child("information").getValue(String.class);
                    String medicationTime = medicationSnapshot.child("time").getValue(String.class);
                    medicationList.add(new Medication(medicationName, medicationInfo, medicationTime));
                }

                //Sort the list of medications by date
                Collections.sort(medicationList, new Comparator<Medication>() {
                    @Override
                    public int compare(Medication med1, Medication med2) {
                        return med1.getTime().compareTo(med2.getTime());
                    }
                });

                medicationAdapter = new MedicationAdapter(medicationList, SetDosesActivity.this);
                recyclerDevices.setAdapter(medicationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MedicationList", "Failed to read medications: " + databaseError.getMessage());
            }
        });

    }

    public void onAddButtonClick(View view){
        Intent intent = new Intent(this, AddMedicationActivity.class);
        startActivity(intent);
    }

    public void onclickBackToMainActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}




