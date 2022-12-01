package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.ListWargaBinaanAdapter;
import umn.ac.id.project.maggot.adapter.ListWarungBinaanAdapter;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

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