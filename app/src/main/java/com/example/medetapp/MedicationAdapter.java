package com.example.medetapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {
    private List<Medication> medicationList;
    private Context context;
    private SetDosesActivity activity;

    public MedicationAdapter(List<Medication> medicationList, Context context, SetDosesActivity activity) {
        this.medicationList = medicationList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medication_item, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medication medication = medicationList.get(position);

        // Check if medication is null
        if (medication != null) {
            holder.medicationName.setText(medication.getName());
            holder.medicationInfo.setText(medication.getInformation());
            holder.medicationTime.setText(medication.getTime());

            // Make item clickable
            holder.itemView.setOnClickListener(v -> {
                Log.d("MedicationAdapter", "Item clicked: " + medication.getName());
                context = holder.itemView.getContext();
                Intent intent = new Intent(context, AddMedicationActivity.class);
                intent.putExtra("medication", medication);
                context.startActivity(intent);
            });

            // Handle delete button click
            holder.deleteButton.setOnClickListener(v -> {
                //Get reference to medications from current device looking at
                DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference("devices")
                        .child(AppState.getAppState().getCurrentDevice().getDeviceID())
                        .child("medications");

                //Delete the medication and recall to fetch and display
                deviceRef.child(medication.getDBKey()).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        activity.fetchMedications();
                        Log.d("MedicationAdapter", "Medication deleted successfully: " + medication.getName());
                    } else {
                        Log.e("MedicationAdapter", "Failed to delete medication: " + task.getException());
                    }
                });
            });
        } else {
            Log.w("MedicationAdapter", "Medication is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        public TextView medicationName;
        public TextView medicationInfo;
        public TextView medicationTime;
        public ImageButton deleteButton;

        public MedicationViewHolder(View itemView) {
            super(itemView);
            medicationName = itemView.findViewById(R.id.medication_name);
            medicationInfo = itemView.findViewById(R.id.medication_info);
            medicationTime = itemView.findViewById(R.id.medication_time);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
