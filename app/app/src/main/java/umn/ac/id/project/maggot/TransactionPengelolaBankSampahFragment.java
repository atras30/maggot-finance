package umn.ac.id.project.maggot;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.PengelolaBankSampahTransactionAdapter;
import umn.ac.id.project.maggot.adapter.ShopTransactionAdapter;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.TransactionModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class TransactionPengelolaBankSampahFragment extends Fragment {
    private Context context;

    public TransactionPengelolaBankSampahFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_pengelolabanksampah_transaction, container, false);
        ApiService.endpoint().getTransactions(new TrashManagerSharedPreference(context).getTrashManager().getEmail()).enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(@NonNull Call<TransactionModel> call, @NonNull Response<TransactionModel> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    List<TransactionModel.Transaction> results = response.body().getTransactions();

                    try{
                    PengelolaBankSampahTransactionAdapter pengelolabanksampahtransactionadapter = new PengelolaBankSampahTransactionAdapter(context, results);
                    RecyclerView recyclerView = view.findViewById(R.id.listTransaction);
                    recyclerView.setAdapter(pengelolabanksampahtransactionadapter);
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
                            for (TransactionModel.Transaction transaction : results) {
                                if (transaction.getDescription().toLowerCase().contains(userInput)) {
                                    newList.add(transaction);
                                }
                            }
                            pengelolabanksampahtransactionadapter.upToDate(newList);
                            return true;
                        }
                    });

                }catch (Exception e) {
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
                Log.d("Gagal", t.toString());
            }
        });
        return view;
    }
}
