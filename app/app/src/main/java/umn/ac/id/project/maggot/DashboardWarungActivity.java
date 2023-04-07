package umn.ac.id.project.maggot;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardWarungActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_warung);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardWarungFragment(this)).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation_container);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch(item.getItemId()) {
                    case R.id.dashboardwarung:
                        selectedFragment = new DashboardWarungFragment(DashboardWarungActivity.this);
                        break;
                    case R.id.transaksiwarung:
                        selectedFragment = new ShopTransactionFragment(DashboardWarungActivity.this);
                        break;
                    case R.id.notification:
                        selectedFragment = new ShopNotificationFragment(DashboardWarungActivity.this);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                return true;
            }
        });
    }
}