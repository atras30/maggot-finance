package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    Button detailwarga, detailwarung, logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_pengelola_bank_sampah);
        getDataPeternak();
        getDataWarung();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_container);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.beli:
                        startActivity(new Intent(HomePagePengelolaBankSampah.this, BuyMaggotActivity2.class));
                        break;
                }

                return true;
            }
        });

        detailwarga = findViewById(R.id.lihatdaftarwarga);
        detailwarung = findViewById(R.id.lihatdaftarwarung);
        detailwarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePagePengelolaBankSampah.this, ListPeternakActivity.class);
                startActivity(intent);
            }
        });
        detailwarung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePagePengelolaBankSampah.this, ListWarungActivity.class);
                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            GoogleSignInClient gsc = GoogleSignIn.getClient(this, gso);

            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    new TrashManagerSharedPreference(HomePagePengelolaBankSampah.this).logout();
                    Toast.makeText(HomePagePengelolaBankSampah.this, "Logout Complete", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomePagePengelolaBankSampah.this, LoginActivity.class));
                    finish();
                }
            });
        });
    }

    private void getDataWarung() {
        ApiService.endpoint().getWarung().enqueue(new Callback<WarungModel>() {
            @Override
            public void onResponse(@NonNull Call<WarungModel> call, @NonNull Response<WarungModel> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    List<WarungModel.Warung> results = response.body().getWarung();
                    results.subList(3, results.size()).clear();

                    ListWarungBinaanAdapter listwarungbinaanadapter = new ListWarungBinaanAdapter(HomePagePengelolaBankSampah.this, results);
                    RecyclerView recyclerView2 = findViewById(R.id.listWarungBinaanRecyclerView);
                    recyclerView2.setAdapter(listwarungbinaanadapter);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(HomePagePengelolaBankSampah.this));
                }
            }

            @Override
            public void onFailure(Call<WarungModel> call, Throwable t) {
                Log.d("Fail", t.toString());
            }
        });
    }


    private void getDataPeternak() {
        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(@NonNull Call<PeternakModel> call, @NonNull Response<PeternakModel> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    List<PeternakModel.Peternak> results = response.body().getPeternak();
                    results.subList(3, results.size()).clear();
                    ListWargaBinaanAdapter listwargabinaanadapter = new ListWargaBinaanAdapter(HomePagePengelolaBankSampah.this, results);
                    RecyclerView recyclerView1 = findViewById(R.id.listWargaBinaanRecyclerView);
                    recyclerView1.setAdapter(listwargabinaanadapter);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(HomePagePengelolaBankSampah.this));
                }
            }

            @Override
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d("Fail", t.toString());
            }
        });
    }
}