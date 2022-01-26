package com.example.android.cs519_pms.user_pharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.android.cs519_pms.R;
import com.example.android.cs519_pms.database.SharedPrefManager;
import com.example.android.cs519_pms.registration.RegistrationActivity;
import com.google.android.material.navigation.NavigationView;


public class PharmacyDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SharedPrefManager.getInstance(this).isLogin()) {
            finish();
            startActivity(new Intent(this, RegistrationActivity.class));
            return;
        }
        setContentView(R.layout.activity_custdashboard);
        Toast.makeText(this, SharedPrefManager.getInstance(this).getUserName(), Toast.LENGTH_LONG).show();

        Toolbar toolbar = findViewById(R.id.toolbar_customer);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.main_drawer_layout_customer);

        NavigationView navigationView = findViewById(R.id.nav_view_customer);
        View headerView = navigationView.getHeaderView(0);
        // get user name and email textViews
        TextView userName = headerView.findViewById(R.id.dashboard_fullName_cust);
        TextView userEmail = headerView.findViewById(R.id.dashboard_emailID_cust);
        // set user name and email
        userName.setText(SharedPrefManager.getInstance(this).getUserName());
        userEmail.setText(SharedPrefManager.getInstance(this).getUserLocation());

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_Container_customer, new Fragment_PharmaProfile()).commit();
            navigationView.setCheckedItem(R.id.nav_search_medicine_cust);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;
        if (id == R.id.nav_search_medicine_cust) {
            fragment = new Fragment_PharmaProfile();
        } else if (id == R.id.nav_cart_cust) {
            fragment = new Fragment_OrderReceived();
        } else if (id == R.id.nav_profile_cust) {
            fragment = new Fragment_UpdateInventory();
        } else if (id == R.id.nav_signOut_cust) {
            //Handle sign out Here:
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            startActivity(new Intent(this, RegistrationActivity.class));
            finish();
            return true;
        } else {
            return false;
        }
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_Container_customer, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}