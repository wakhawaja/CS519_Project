package com.example.android.cs519_pms.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.android.cs519_pms.R;
import com.example.android.cs519_pms.customer.CustomerDashboardActivity;
import com.example.android.cs519_pms.database.SharedPrefManager;

public class RegistrationActivity extends AppCompatActivity {
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefManager.getInstance(this).isLogin()) dashboardActivity(this);
        setContentView(R.layout.activity_registration);
        frameLayout = findViewById(R.id.registration_frameLayout);
        if (savedInstanceState == null) {
            setFragment(new Fragment_SignIn());
        }
    }

    private void dashboardActivity(Context context) {
        if (SharedPrefManager.getInstance(this).getUserType() == 1) {
            Intent intent = new Intent(context, CustomerDashboardActivity.class);
            startActivity(intent);
            finish();
        } else if (SharedPrefManager.getInstance(this).getUserType() == 2) {
            Intent intent = new Intent(context, CustomerDashboardActivity.class);
            startActivity(intent);
            finish();
        } else if (SharedPrefManager.getInstance(this).getUserType() == 3) {
            Intent intent = new Intent(context, CustomerDashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}