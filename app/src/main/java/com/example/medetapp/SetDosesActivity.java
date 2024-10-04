package com.example.medetapp;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SetDosesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoseTimeListAdapter doseTimeListAdapter;
    private List<DoseTime> doseTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_doses);


        //create list of dose times // change later to retrieve dose times
        doseTimes= new ArrayList<DoseTime>();

        // fill recycler view
        recyclerView = findViewById(R.id.pill_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        doseTimeListAdapter = new DoseTimeListAdapter(doseTimes);
        recyclerView.setAdapter(doseTimeListAdapter);
    }


    public void onAddButtonClick(View view){
        showTimePickerDialog();
    }

    public void showTimePickerDialog() {
        // Start the time at 12 AM (0:00)
        int startHour = 0; // 12 AM
        int startMinute = 0; // 00 minutes

        // define time picker and function called on choosing time
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {

                        //creat time string

                        String stringHour;
                        String stringMinute;
                        String amPm = "am";

                        //make sure time is formatted to 12 hour time
                        if (hour == 0) {
                            stringHour = "12";
                        } else if (hour > 12) {
                            hour = hour - 12;
                            amPm = "pm";
                            stringHour = Integer.toString(hour);
                        } else {
                            stringHour = Integer.toString(hour);
                        }

                        // make sure time is formatted with two digits
                        if (minute == 0) {
                            stringMinute = "00";
                        } else {
                            stringMinute = Integer.toString(minute);
                        }

                        // add time
                        addDoseTime(stringHour+":"+stringMinute+" "+amPm);
                    }


                }, startHour, startMinute, false);

        //show dialog
        timePickerDialog.show();
    }


    // This method adds a dose time to the list and updates the UI
    public void addDoseTime(String time){
        DoseTime doseTime = new DoseTime(time);
        doseTimes.add(doseTime);
        int position = doseTimes.size();
        doseTimeListAdapter.notifyItemInserted(position);
    }
}




