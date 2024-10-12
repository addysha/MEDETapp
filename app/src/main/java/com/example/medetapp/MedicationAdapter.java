package com.example.medetapp;

import android.content.Context;
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
        holder.medicationName.setText(medication.getName());
        holder.medicationInfo.setText(medication.getInformation());
        holder.medicationTime.setText(medication.getTime());

        holder.deleteButton.setOnClickListener(v -> {
            //TODO: Handle delete
        });
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
