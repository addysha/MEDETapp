package com.example.medetapp;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//This Class Implements the Pin Activity
public class PinActivity extends AppCompatActivity {

    ArrayList<Integer> pin = new ArrayList<>();
    View[] pinDots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        //if not logged in we want to direct to the greeting page
        if (!(AppState.getAppState().getLoginManager().isUserLoggedIn())){
            Intent intent = new Intent(this, GreetingPageActivity.class);
            startActivity(intent);
        }

        // Initialize PIN dots
        pinDots = new View[] {
                findViewById(R.id.pinDot1),
                findViewById(R.id.pinDot2),
                findViewById(R.id.pinDot3),
                findViewById(R.id.pinDot4)
        };

        // create onclick methods for all of the buttons
        for (int i = 0; i < 10; i++) {
            final int index = i;
            int resId = getResources().getIdentifier("button" + i, "id", getPackageName());
            Button button = findViewById(resId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNumberClick(index);
                }
            });
        }

        // Set up onclick method for delete button
         TextView deleteButton =  findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                onDeleteClick();
            }

        });

    }

    // adds number to pin. verifies pin if 4 numbers picked
    private void onNumberClick(int i ){

        pin.add(i);
        updatePinDots();
        if(pin.size()==4){
            verifyPin();
        }

    }

    // removes last added pin number from pin and updates pindots
    private void onDeleteClick(){
        if(!(pin.isEmpty())){
            pin.remove(pin.size()-1);
            updatePinDots();
        }
    }

    // proceeds to Main activity if pin is valid otherwise clears pin
    private void verifyPin(){

        if (AppState.getAppState().getLoginManager().verifyPin(pin)){
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("message");
//
//            myRef.setValue("Hello, World!").addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Log.d("TAG", "Data written successfully");
//                } else {
//                    Log.e("TAG", "Data write failed: " + task.getException());
//                }
//            });

            Intent intent = new Intent(this, DeviceList.class);
            startActivity(intent);
            finish();
        }
        else{
            pin = new ArrayList<Integer>();
            updatePinDots();
        }
    }

    // updates the visual pin dot display to reflect number of numbers picked
    private void updatePinDots(){

        for(int i = 0; i  < 4; i++ ){
            View pinDot = pinDots[i];
            if (i < pin.size()){
                pinDot.setBackgroundResource(R.drawable.filled_pin_dot);
            }
            else {
                pinDot.setBackgroundResource(R.drawable.pin_dot);
            }

        }


    }

}
