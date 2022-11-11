package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.ListPeternakAdapter;
import umn.ac.id.project.maggot.adapter.ListWargaBinaanAdapter;
import umn.ac.id.project.maggot.adapter.ListWarungBinaanAdapter;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class HomePagePengelolaBankSampah extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_pengelola_bank_sampah);
        getDataPeternak();
        getDataWarung();
    }

    private void getDataWarung() {
        ApiService.endpoint().getWarung().enqueue(new Callback<WarungModel>() {
            @Override
            public void onResponse(@NonNull Call<WarungModel> call, @NonNull Response<WarungModel> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    List<WarungModel.Warung> results = response.body().getWarung();
                    Log.d("Success", results.toString());
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
                    Log.d("Success", results.toString());
                    ListWargaBinaanAdapter listwargabinaanadapter = new ListWargaBinaanAdapter(HomePagePengelolaBankSampah.this, results);
                    RecyclerView recyclerView1 = findViewById(R.id.listWargaBinaanRecyclerView);
                    RecyclerView recyclerView2 = findViewById(R.id.listWarungBinaanRecyclerView);
                    recyclerView1.setAdapter(listwargabinaanadapter);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(HomePagePengelolaBankSampah.this));
                    recyclerView2.setAdapter(listwargabinaanadapter);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(HomePagePengelolaBankSampah.this));


                }
            }

            @Override
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d("Fail", t.toString());
            }
        });
    }
}