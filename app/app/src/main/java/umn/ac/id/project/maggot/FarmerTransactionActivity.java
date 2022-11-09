package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import umn.ac.id.project.maggot.adapter.FarmerTransactionAdapter;
import umn.ac.id.project.maggot.adapter.ListPeternakAdapter;
import umn.ac.id.project.maggot.model.TransactionModel;

public class FarmerTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_transaction);

        ArrayList<TransactionModel> transactions = new ArrayList<>();
        transactions.add(new TransactionModel("income", "penjualan maggot 5 kg", new Date(), 20000));
        transactions.add(new TransactionModel("outcome", "Testing Deskripsi Doang", new Date(), 50000));

        FarmerTransactionAdapter farmerTransactionAdapter = new FarmerTransactionAdapter(FarmerTransactionActivity.this, transactions);
        RecyclerView recyclerView = findViewById(R.id.farmer_transaction_recycler_view);
        recyclerView.setAdapter(farmerTransactionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FarmerTransactionActivity.this));

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation_container);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.beranda:
                        Intent intent = new Intent(FarmerTransactionActivity.this, FarmerDashboardActivity
                                .class);
                        startActivity(intent);
                        finish();
                    case R.id.transaksi:
                        return true;
                    case R.id.bayar:
                        Toast.makeText(FarmerTransactionActivity.this, "Bayar", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(FarmerTransactionActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }
}