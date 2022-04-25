package com.example.quizgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.quizgame.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    FirebaseAuth auth;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static final String EMAIL_AND_PASSWORD = "EMAIL_AND_PASSWORD";
    public static final String REMEMBER_CHECK = "rememberCheck";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();

        preferences = getSharedPreferences(EMAIL_AND_PASSWORD, MODE_PRIVATE);
        editor = preferences.edit();
        boolean rememberPass = preferences.getBoolean(REMEMBER_CHECK, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(auth.getCurrentUser() != null && rememberPass == true){
//                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
//                    finish();
//                }else {
//                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
//                    finish();
//                }
                if(rememberPass == true){
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}