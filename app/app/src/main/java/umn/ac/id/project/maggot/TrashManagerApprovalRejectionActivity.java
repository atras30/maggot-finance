package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.ApprovalRejectionAdapter;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class TrashManagerApprovalRejectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_manager_approval_rejection);

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

                ApprovalRejectionAdapter approvalRejectionAdapter = new ApprovalRejectionAdapter(TrashManagerApprovalRejectionActivity.this, notApprovedYetUsers);
                RecyclerView recyclerView = findViewById(R.id.approval_rejection_recycler_view);
                recyclerView.setAdapter(approvalRejectionAdapter);
                approvalRejectionAdapter.setOnItemClickListener(new ApprovalRejectionAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        notApprovedYetUsers.remove(position);
                        approvalRejectionAdapter.notifyItemRemoved(position);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(TrashManagerApprovalRejectionActivity.this));

                SearchView searchnamawarung = findViewById(R.id.searchwarung);
                searchnamawarung.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        String userInput = newText.toLowerCase();
                        List<UserModel.User> newList = new ArrayList<>();
                        for (UserModel.User user : notApprovedYetUsers) {
                            if (user.getFull_name().toLowerCase().contains(userInput) || user.getAddress().toLowerCase().contains(userInput) || user.getEmail().toLowerCase().contains(userInput)) {
                                newList.add(user);
                            }
                        }
                        approvalRejectionAdapter.upToDate(newList);
                        return true;
                    }
                });

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(TrashManagerApprovalRejectionActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}