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

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation_container);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.beranda:
                        return true;
                    case R.id.transaksi:
                        Intent intent = new Intent(FarmerDashboardActivity.this, FarmerTransactionActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.bayar:
                        Toast.makeText(FarmerDashboardActivity.this, "Bayar", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(FarmerDashboardActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }
}