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
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.ApprovalRejectionAdapter;
import umn.ac.id.project.maggot.adapter.DetailWargaAdapter;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.UserModel;
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
        FloatingActionButton fab = findViewById(R.id.fab);
        ArrayList<UserModel.User> results = new TrashManagerSharedPreference(ListPeternakActivity.this).getTrashManager().getUsers();
        ArrayList<UserModel.User> daftarPeternak = new ArrayList<>();

        for (UserModel.User user : results) {
            if(user.getRole().equalsIgnoreCase("farmer") && user.is_verified() == 1) {
                daftarPeternak.add(user);
            }
        }

        DetailWargaAdapter detailWargaAdapter = new DetailWargaAdapter(ListPeternakActivity.this, daftarPeternak);
        RecyclerView recyclerView = findViewById(R.id.listPeternakRecyclerView);
        recyclerView.setAdapter(detailWargaAdapter);
        detailWargaAdapter.setOnItemClickListener(new DetailWargaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                detailWargaAdapter.notifyItemRemoved(position);
                results.remove(position);
                detailWargaAdapter.upToDate(results);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(ListPeternakActivity.this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListPeternakActivity.this, InputWargaActivity.class);
                startActivity(intent);
            }
        });

        SearchView searchnamawarga = findViewById(R.id.searchwarga);
        searchnamawarga.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                ArrayList<UserModel.User> newList = new ArrayList<>();
                for (UserModel.User peternak : results) {
                    if (peternak.getFull_name().toLowerCase().contains(userInput) || peternak.getAddress().toLowerCase().contains(userInput) || peternak.getEmail().toLowerCase().contains(userInput)) {
                        newList.add(peternak);
                    }
                }
                detailWargaAdapter.upToDate(newList);
                return true;
            }
        });
    }
}