package umn.ac.id.project.maggot;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import umn.ac.id.project.maggot.adapter.ApprovalRejectionAdapter;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class ApprovalRejectionFragment extends Fragment {
    private Context context;
    private View view;

    public ApprovalRejectionFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        ApiService.endpoint().updateTrashManagerData("Bearer " + new TrashManagerSharedPreference(context).getToken()).enqueue(new Callback<TrashManagerModel>() {
            @Override
            public void onResponse(Call<TrashManagerModel> call, Response<TrashManagerModel> response) {
                if(response.isSuccessful()) {
                    Log.i("tag", response.body().updateTrashManagerData().toString());
                    TrashManagerModel.TrashManagers trashManager = response.body().updateTrashManagerData();

                    List<UserModel.User> users = trashManager.getUsers();

                    ArrayList<UserModel.User> notApprovedYetUsers = new ArrayList<>();

                    for(UserModel.User user : users) {
                        if(user.is_verified() == 0) {
                            notApprovedYetUsers.add(user);
                        }
                    }

                    ApprovalRejectionAdapter approvalRejectionAdapter = new ApprovalRejectionAdapter(context, notApprovedYetUsers);
                    RecyclerView recyclerView = view.findViewById(R.id.approval_rejection_recycler_view);
                    recyclerView.setAdapter(approvalRejectionAdapter);
                    approvalRejectionAdapter.setOnItemClickListener(new ApprovalRejectionAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            approvalRejectionAdapter.notifyItemRemoved(position);
                            notApprovedYetUsers.remove(position);
                            approvalRejectionAdapter.upToDate(notApprovedYetUsers);
                        }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    SearchView searchnamawarung = view.findViewById(R.id.searchwarung);
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
                } else {
                    try {
                        Log.i("Error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TrashManagerModel> call, Throwable t) {
                Toast.makeText(context, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_approval_rejection, container, false);

        return view;
    }
}