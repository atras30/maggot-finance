package umn.ac.id.maggotproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.maggotproject.adapter.ListPeternakAdapter;
import umn.ac.id.maggotproject.model.PeternakModel;
import umn.ac.id.maggotproject.retrofit.ApiService;

public class ListPeternakActivity extends AppCompatActivity {
    private final String TAG = "Peternak Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_peternak);

        getDataPeternak();
    }

    private void getDataPeternak() {
        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(Call<PeternakModel> call, Response<PeternakModel> response) {
                if(response.isSuccessful()) {
                    List<PeternakModel.Peternak> results = response.body().getPeternak();
                    Log.d(TAG, results.toString());
                    ListPeternakAdapter listPeternakAdapter = new ListPeternakAdapter(ListPeternakActivity.this, results);
                    RecyclerView recyclerView = findViewById(R.id.listPeternakRecyclerView);
                    recyclerView.setAdapter(listPeternakAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListPeternakActivity.this));
                }
            }

            @Override
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }
}