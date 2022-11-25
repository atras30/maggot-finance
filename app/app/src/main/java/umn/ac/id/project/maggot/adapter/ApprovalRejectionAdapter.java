package umn.ac.id.project.maggot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.ApprovalRejectionModel;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class ApprovalRejectionAdapter extends RecyclerView.Adapter<ApprovalRejectionAdapter.ListApprovalRejectionViewHolder> {
    Context context;
    private List<UserModel.User> users;
    public OnItemClickListener listener;

    public ApprovalRejectionAdapter(Context context, List<UserModel.User> users) {
        this.context = context;
        this.users = users;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        listener = clickListener;
    }

    @NonNull
    @Override
    public ListApprovalRejectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_rejection_approval_row, parent, false);

        return new ListApprovalRejectionViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListApprovalRejectionViewHolder holder, int position) {
        holder.namaUser.setText(users.get(position).getFull_name());
        holder.alamatUser.setText(users.get(position).getAddress());
    }

    private void sendRejectRequest(String email) {
        ApiService.endpoint().rejectionUserRegistration(email).enqueue(new Callback<ApprovalRejectionModel>() {
            @Override
            public void onResponse(Call<ApprovalRejectionModel> call, Response<ApprovalRejectionModel> response) {
                String message = response.body().rejectionUserRegistration();
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ApprovalRejectionModel> call, Throwable t) {
                Toast.makeText(context, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendApproveRequest(String email) {
        ApiService.endpoint().approvalUserRegistration(email).enqueue(new Callback<ApprovalRejectionModel>() {
            @Override
            public void onResponse(Call<ApprovalRejectionModel> call, Response<ApprovalRejectionModel> response) {
                String message = response.body().approvalUserRegistration();
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ApprovalRejectionModel> call, Throwable t) {
                Toast.makeText(context, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ListApprovalRejectionViewHolder extends RecyclerView.ViewHolder {
        TextView namaUser, alamatUser;
        ImageView approveButton, rejectButton;

        public ListApprovalRejectionViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            namaUser = itemView.findViewById(R.id.nama_user);
            alamatUser = itemView.findViewById(R.id.alamat_user);
            approveButton = itemView.findViewById(R.id.approve);
            rejectButton = itemView.findViewById(R.id.reject);

            approveButton.setOnClickListener(v -> {
                sendApproveRequest(users.get(getAdapterPosition()).getEmail());
                listener.onItemClick(getAdapterPosition());
            });

            rejectButton.setOnClickListener(v -> {
                sendRejectRequest(users.get(getAdapterPosition()).getEmail());
                listener.onItemClick(getAdapterPosition());
            });
        }
    }

    public void upToDate(List<UserModel.User> newList){
        users = new ArrayList<>();
        users.addAll(newList);
        notifyDataSetChanged();
    }

}
