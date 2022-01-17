package com.example.android.cs519_pms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.android.cs519_pms.registration.RegistrationActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SystemClock.sleep(3000);
        Intent registrationIntent = new Intent(SplashActivity.this, RegistrationActivity.class);
        startActivity(registrationIntent);
        finish();
    }
}