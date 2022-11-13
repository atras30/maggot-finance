package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.DetailWargaAdapter;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class ListPeternakActivity extends AppCompatActivity {
    private final String TAG = "Peternak Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_peternak);
        getDataPeternak();
        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListPeternakActivity.this, HomePagePengelolaBankSampah.class);
                startActivity(intent);
            }
        });
    }

    private void getDataPeternak() {
        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(@NonNull Call<PeternakModel> call, @NonNull Response<PeternakModel> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    FloatingActionButton fab = findViewById(R.id.fab);
                    List<PeternakModel.Peternak> results = response.body().getPeternak();
                    Log.d(TAG, results.toString());
                    DetailWargaAdapter detailWargaAdapter = new DetailWargaAdapter(ListPeternakActivity.this, results);
                    RecyclerView recyclerView = findViewById(R.id.listPeternakRecyclerView);
                    recyclerView.setAdapter(detailWargaAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListPeternakActivity.this));

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar.make(view, "INI BUAT FAB TAMBAHNYA!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }
}