package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.ApprovalRejectionAdapter;
import umn.ac.id.project.maggot.adapter.ListPeternakAdapter;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class TrashManagerDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_manager_dashboard);

        ApiService.endpoint().getUsers().enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                List<UserModel.User> users = response.body().getUsers();

                ArrayList<UserModel.User> notApprovedYetUsers = new ArrayList<>();

                for(UserModel.User user : users) {
                    if(user.is_verified() == 0) {
                        notApprovedYetUsers.add(user);
                    }
                }

                ApprovalRejectionAdapter approvalRejectionAdapter = new ApprovalRejectionAdapter(TrashManagerDashboardActivity.this, notApprovedYetUsers);
                RecyclerView recyclerView = findViewById(R.id.approval_rejection_recycler_view);
                recyclerView.setAdapter(approvalRejectionAdapter);
                approvalRejectionAdapter.setOnItemClickListener(new ApprovalRejectionAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        notApprovedYetUsers.remove(position);
                        approvalRejectionAdapter.notifyItemRemoved(position);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(TrashManagerDashboardActivity.this));
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(TrashManagerDashboardActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}