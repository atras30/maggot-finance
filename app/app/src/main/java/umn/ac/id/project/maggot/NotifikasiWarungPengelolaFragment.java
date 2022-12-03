package umn.ac.id.project.maggot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.FarmerTransactionAdapter;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.TransactionModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class NotifikasiWarungPengelolaFragment extends Fragment {

    private Context context;

    public NotifikasiWarungPengelolaFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifikasi_warung_pengelola, container, false);
        String email = new UserSharedPreference(context).getUser().getEmail();
        ApiService.endpoint().getTransactions(email).enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                if(response.isSuccessful()) {
                    List<TransactionModel.Transaction> transactions = response.body().getTransactions();

                    try {
                        FarmerTransactionAdapter farmerTransactionAdapter = new FarmerTransactionAdapter(context, transactions);
                        RecyclerView recyclerView = ((Activity)context).findViewById(R.id.warung_confirmation_recycler_view);
                        recyclerView.setAdapter(farmerTransactionAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        SearchView searchtransaction = view.findViewById(R.id.searchtransaksi);
                        searchtransaction.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                String userInput = newText.toLowerCase();
                                List<TransactionModel.Transaction> newList = new ArrayList<>();
                                for (TransactionModel.Transaction transaction : transactions) {
                                    if (transaction.getDescription().toLowerCase().contains(userInput)) {
                                        newList.add(transaction);
                                    }
                                }
                                farmerTransactionAdapter.upToDate(newList);
                                return true;
                            }
                        });
                    } catch (Exception e) {
                        call.cancel();
                    }
                } else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.i("Failed", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                Toast.makeText(context, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
        // Inflate the layout for this fragment
    }
}