package umn.ac.id.project.maggot;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.FarmerNotificationAdapter;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.NotificationUserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class FarmerNotificationFragment extends Fragment {
    Context context;

    public FarmerNotificationFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmer_notification, container, false);

        ApiService.endpoint().getAllNotifications("Bearer " + new UserSharedPreference(context).getToken()).enqueue(new Callback<NotificationUserModel>() {
            @Override
            public void onResponse(Call<NotificationUserModel> call, Response<NotificationUserModel> response) {
                if(response.isSuccessful()) {
                    ArrayList<NotificationUserModel.Notification> results = response.body().getAllNotifications();

                    Log.i("Notifications", results.toString());

                    RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
                    FarmerNotificationAdapter farmerNotificationAdapter = new FarmerNotificationAdapter(context, results);
                    farmerNotificationAdapter.setOnItemClickListener(new FarmerNotificationAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            farmerNotificationAdapter.notifyItemRemoved(position);
                            results.remove(position);
                            farmerNotificationAdapter.upToDate(results);
                        }
                    });
                    recyclerView.setAdapter(farmerNotificationAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    try {
                        Toast.makeText(context, "Masalah: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationUserModel> call, Throwable t) {
                Log.i("Error 3", "Error 3");
                Toast.makeText(context, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}