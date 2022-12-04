package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.DetailWargaAdapter;
import umn.ac.id.project.maggot.adapter.DetailWarungAdapter;
import umn.ac.id.project.maggot.adapter.ListWarungBinaanAdapter;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class ListWarungActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_warung);
        getDataWarung();
        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListWarungActivity.this, HomePagePengelolaBankSampah.class);
                startActivity(intent);
            }
        });
    }

    private void getDataWarung() {
        ApiService.endpoint().getWarung().enqueue(new Callback<WarungModel>() {
            @Override
            public void onResponse(@NonNull Call<WarungModel> call, @NonNull Response<WarungModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    FloatingActionButton fab = findViewById(R.id.fab);
                    List<WarungModel.Warung> results = response.body().getWarung();
                    Log.d("Success", results.toString());
                    DetailWarungAdapter detailWarungAdapter = new DetailWarungAdapter(ListWarungActivity.this, results);
                    RecyclerView recyclerView2 = findViewById(R.id.listWarungRecyclerView);
                    recyclerView2.setAdapter(detailWarungAdapter);
                    detailWarungAdapter.setOnItemClickListener(new DetailWarungAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            detailWarungAdapter.notifyItemRemoved(position);
                            results.remove(position);
                            detailWarungAdapter.upToDate(results);
                        }
                    });
                    recyclerView2.setLayoutManager(new LinearLayoutManager(ListWarungActivity.this));

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ListWarungActivity.this, InputWarungActivity.class);
                            startActivity(intent);
                        }
                    });

                    SearchView searchnamawarung = findViewById(R.id.searchwarung);
                    searchnamawarung.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            String userInput = newText.toLowerCase();
                            List<WarungModel.Warung> newList = new ArrayList<>();
                            for (WarungModel.Warung warung : results) {
                                if (warung.getFull_name().toLowerCase().contains(userInput) || warung.getAddress().toLowerCase().contains(userInput) || warung.getEmail().toLowerCase().contains(userInput)) {
                                    newList.add(warung);
                                }
                            }
                            detailWarungAdapter.upToDate(newList);
                            return true;
                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<WarungModel> call, Throwable t) {
                Log.d("Fail", t.toString());
            }
        });
    }
}

