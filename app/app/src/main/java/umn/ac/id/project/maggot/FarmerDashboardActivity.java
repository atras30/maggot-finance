package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import umn.ac.id.project.maggot.global.Helper;

public class FarmerDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_dashboard);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FarmerDashboardFragment(this)).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation_container);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch(item.getItemId()) {
                    case R.id.beranda:
                        selectedFragment = new FarmerDashboardFragment(FarmerDashboardActivity.this);
                        break;
                    case R.id.transaksi:
                        selectedFragment = new FarmerTransactionFragment(FarmerDashboardActivity.this);
                        break;
                    case R.id.bayar:
                        selectedFragment = new FarmerPaymentFragment(FarmerDashboardActivity.this);
                        break;
                    case R.id.notif:
                        selectedFragment = new FarmerNotificationFragment(FarmerDashboardActivity.this);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                return true;
            }
        });
    }
}