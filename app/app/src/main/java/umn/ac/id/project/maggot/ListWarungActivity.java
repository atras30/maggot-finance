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
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.UserModel;
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

    @Override
    protected void onResume() {
        super.onResume();
        getDataWarung();
    }

    private void getDataWarung() {
        FloatingActionButton fab = findViewById(R.id.fab);
        ArrayList<UserModel.User> results = new TrashManagerSharedPreference(ListWarungActivity.this).getTrashManager().getUsers();
        ArrayList<UserModel.User> daftarWarungBinaan = new ArrayList<>();

        for (UserModel.User user : results) {
            if(user.getRole().equalsIgnoreCase("shop") && user.is_verified() == 1) {
                daftarWarungBinaan.add(user);
            }
        }

        DetailWarungAdapter detailWarungAdapter = new DetailWarungAdapter(ListWarungActivity.this, daftarWarungBinaan);
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
                ArrayList<UserModel.User> newList = new ArrayList<>();
                for (UserModel.User warung : results) {
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

