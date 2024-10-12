package com.example.medetapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {
    private List<Medication> medicationList;
    private Context context;

    public MedicationAdapter(List<Medication> medicationList, Context context) {
        this.medicationList = medicationList;
        this.context = context;

        // Log the size of the medication list
        Log.d("MedicationAdapter", "List size: " + medicationList.size());
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

            // Log the medication details
            Log.d("MedicationAdapter", "Binding medication: " + medication.getName());

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
                Log.d("MedicationAdapter", "Delete clicked for: " + medication.getName());
                // TODO: Implement delete functionality
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
