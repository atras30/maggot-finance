package umn.ac.id.project.maggot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.ApprovalRejectionAdapter;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class ApprovalRejectionFragment extends Fragment {
    private Context context;
    private CardView cvApprove, cvReject;

    public ApprovalRejectionFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_approval_rejection, container, false);

            cvApprove = view.findViewById(R.id.approve);
            cvReject = view.findViewById(R.id.rejection);

            cvApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder myBuild = new AlertDialog.Builder(context);
                    View myView = getLayoutInflater().inflate(R.layout.modal_approve, null);
                    btnSubmit = myView.findViewById(R.id.submitButton);

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!name.getText().toString().isEmpty()) {
                                Intent gotoLibrary = new Intent(MainActivity.this, ActivityLibrary.class);
                                gotoLibrary.putExtra("7030202", name.getText().toString());
                                startActivity(gotoLibrary);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Nama harus diisi!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    myBuild.setView(myView);
                    AlertDialog dialog = myBuild.create();
                    dialog.show();
                }
            });

            cvReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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

                    ApprovalRejectionAdapter approvalRejectionAdapter = new ApprovalRejectionAdapter(context, notApprovedYetUsers);
                    RecyclerView recyclerView = view.findViewById(R.id.approval_rejection_recycler_view);
                    recyclerView.setAdapter(approvalRejectionAdapter);
                    approvalRejectionAdapter.setOnItemClickListener(new ApprovalRejectionAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            notApprovedYetUsers.remove(position);
                            approvalRejectionAdapter.notifyItemRemoved(position);
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

                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(context, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        return view;
    }
}