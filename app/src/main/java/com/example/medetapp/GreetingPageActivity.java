package com.example.medetapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GreetingPageActivity extends AppCompatActivity {
    private static final String TAG = "GreetingPageActivity";
    private SignInClient signInClient;
    private FirebaseAuth auth;
    private ActivityResultLauncher<IntentSenderRequest> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting_page);

        // Initialize SignInClient
        signInClient = Identity.getSignInClient(this);

        // Initialize Firebase Auth
        auth = AppState.getAppState().getLoginManager().getFirebaseAuth();

        // Set up ActivityResultLauncher
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> handleSignInResult(result.getData())
        );

        // Set up sign-in request
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        //Trigger sign in on button click
        findViewById(R.id.sign_in_button).setOnClickListener(v -> beginSignIn(signInRequest));
    }

    //this method begins the sign in activity/process
    private void beginSignIn(BeginSignInRequest signInRequest) {
        signInClient.beginSignIn(signInRequest)
                .addOnSuccessListener(result -> {
                    try {
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                        signInLauncher.launch(intentSenderRequest);
                    } catch (Exception e) {
                        Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "beginSignIn: Failed to start sign in " + e.getLocalizedMessage());
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
                });
    }

    //
    private void handleSignInResult(Intent data) {
        try {
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();
            if (idToken != null) {
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    updateUi(user);
                                } else {
                                    // If sign in fails, display a message to the user
                                    updateUi(null);
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());

                                }
                            }
                        });
            }
        } catch (ApiException e) {
            Log.e(TAG, "handleSignInResult: Failed to get credentials " + e.getLocalizedMessage());
        }

    }

    //this user
    private void updateUi(FirebaseUser user){
        if (user == null){
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
        }
        else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


}