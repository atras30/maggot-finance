package umn.ac.id.project.maggot;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.ApprovalRejectionAdapter;
import umn.ac.id.project.maggot.adapter.WarungNotificationAdapter;
import umn.ac.id.project.maggot.adapter.WarungSearchDropDownAdapter;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.NotificationUserModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiEndpoint;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class ShopNotificationFragment extends Fragment {
    Context context;

    public ShopNotificationFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_notification, container, false);

        ApiService.endpoint().getAllNotifications("Bearer " + new UserSharedPreference(context).getToken()).enqueue(new Callback<NotificationUserModel>() {
            @Override
            public void onResponse(Call<NotificationUserModel> call, Response<NotificationUserModel> response) {
                if(response.isSuccessful()) {
                    ArrayList<NotificationUserModel.Notification> results = response.body().getAllNotifications();

                    Log.i("Notifications", results.toString());

                    RecyclerView recyclerView = view.findViewById(R.id.rvNotifWarung);
                    WarungNotificationAdapter warungNotificationAdapter = new WarungNotificationAdapter(context, results);
                    warungNotificationAdapter.setOnItemClickListener(new WarungNotificationAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            warungNotificationAdapter.notifyItemRemoved(position);
                            results.remove(position);
                            warungNotificationAdapter.upToDate(results);
                        }
                    });
                    recyclerView.setAdapter(warungNotificationAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    try {
                        Log.i("Error 2", response.errorBody().string());
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.i("Error 2", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationUserModel> call, Throwable t) {
                Log.i("Error 3", "Error 3");
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}