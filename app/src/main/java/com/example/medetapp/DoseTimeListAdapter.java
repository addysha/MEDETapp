package com.example.medetapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

    public class DoseTimeListAdapter extends RecyclerView.Adapter<DoseTimeListAdapter.ViewHolder> {

        private List<DoseTime> doseTimeList;

        public DoseTimeListAdapter(List<DoseTime> DoesTimeList) {
            this.doseTimeList = DoesTimeList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dose_time_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DoseTime doseTime = doseTimeList.get(position);
            holder.textViewItem.setText(doseTime.getName());
        }

        @Override
        public int getItemCount() {
            return doseTimeList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewItem;

            public ViewHolder(View itemView) {
                super(itemView);
                textViewItem = itemView.findViewById(R.id.textViewItem);
            }
        }
    }

