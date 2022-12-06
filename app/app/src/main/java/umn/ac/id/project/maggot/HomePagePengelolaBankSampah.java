package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomePagePengelolaBankSampah extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_pengelola_bank_sampah);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardPengelolaBankSampahFragment(this)).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation_container);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.beranda:
                        selectedFragment = new DashboardPengelolaBankSampahFragment(HomePagePengelolaBankSampah.this);
                        break;
                    case R.id.transaksi:
                        selectedFragment = new TransactionPengelolaBankSampahFragment(HomePagePengelolaBankSampah.this);
                        break;
                    case R.id.beli:
                        selectedFragment = new BuyMaggotFragment(HomePagePengelolaBankSampah.this);
                        break;
                    case R.id.pencairan:
                        selectedFragment = new PencairanMaggotWargaFragment(HomePagePengelolaBankSampah.this);
                        break;
                    case R.id.warga:
                        selectedFragment = new ApprovalRejectionFragment(HomePagePengelolaBankSampah.this);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                return true;
            }
        });
    }
}