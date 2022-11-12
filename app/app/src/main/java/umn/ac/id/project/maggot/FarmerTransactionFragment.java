package umn.ac.id.project.maggot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.FarmerTransactionAdapter;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.TransactionModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class FarmerTransactionFragment extends Fragment {
    private Context context;

    public FarmerTransactionFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String email = new UserSharedPreference(context).getUser().getEmail();
        ApiService.endpoint().getTransactions(email).enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                if(response.isSuccessful()) {
                    List<TransactionModel.Transaction> transactions = response.body().getTransactions();

                    try {
                        FarmerTransactionAdapter farmerTransactionAdapter = new FarmerTransactionAdapter(context, transactions);
                        RecyclerView recyclerView = ((Activity)context).findViewById(R.id.farmer_transaction_recycler_view);
                        recyclerView.setAdapter(farmerTransactionAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_farmer_transaction, container, false);
    }
}