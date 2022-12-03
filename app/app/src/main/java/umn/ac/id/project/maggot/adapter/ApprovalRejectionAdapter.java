package umn.ac.id.project.maggot.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.ApprovalRejectionModel;
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
    public void onBindViewHolder(@NonNull ListApprovalRejectionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.namaUser.setText(users.get(position).getFull_name());
        holder.alamatUser.setText(users.get(position).getAddress());

        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuild = new AlertDialog.Builder(context);
                View myView = ((Activity)context).getLayoutInflater().inflate(R.layout.modal_approve, null);
                TextView tvNama = myView.findViewById(R.id.tvPanjang);
                tvNama.setText("Apakah Anda yakin ingin menerima "+users.get(position).getFull_name()+" sebagai warga penerima manfaat di bank sampah Anda?");
                Button btnSubmit = myView.findViewById(R.id.btnkonf);
                Button btnBatal = myView.findViewById(R.id.btnbatal);

                myBuild.setView(myView);
                AlertDialog dialog = myBuild.create();
                dialog.show();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Debug", position+"");
                        ApiService.endpoint().approvalUserRegistration(users.get(position).getEmail()).enqueue(new Callback<ApprovalRejectionModel>() {
                            @Override
                            public void onResponse(Call<ApprovalRejectionModel> call, Response<ApprovalRejectionModel> response) {
                                Log.d("Debug2", position+"");
                                String message = response.body().approvalUserRegistration();
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                listener.onItemClick(position);
                            }

                            @Override
                            public void onFailure(Call<ApprovalRejectionModel> call, Throwable t) {
                                Toast.makeText(context, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.hide();
                    }
                });

                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuild = new AlertDialog.Builder(context);
                View myView = ((Activity)context).getLayoutInflater().inflate(R.layout.modal_rejection, null);
                TextView tvNama = myView.findViewById(R.id.tvPanjang);
                tvNama.setText("Apakah Anda yakin ingin menolak "+users.get(position).getFull_name()+" sebagai warga penerima manfaat di bank sampah Anda?");
                Button btnSubmit = myView.findViewById(R.id.btnkonf);
                Button btnBatal = myView.findViewById(R.id.btnbatal);

                myBuild.setView(myView);
                AlertDialog dialog = myBuild.create();
                dialog.show();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Debug", position+"");
                        ApiService.endpoint().rejectionUserRegistration(users.get(position).getEmail()).enqueue(new Callback<ApprovalRejectionModel>() {
                            @Override
                            public void onResponse(Call<ApprovalRejectionModel> call, Response<ApprovalRejectionModel> response) {
                                Log.d("Debug2", position+"");
                                String message = response.body().rejectionUserRegistration();
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                listener.onItemClick(position);
                            }

                            @Override
                            public void onFailure(Call<ApprovalRejectionModel> call, Throwable t) {
                                Toast.makeText(context, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.hide();
                    }
                });

                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ListApprovalRejectionViewHolder extends RecyclerView.ViewHolder {
        TextView namaUser, alamatUser;
        CardView approveButton, rejectButton;

        public ListApprovalRejectionViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            namaUser = itemView.findViewById(R.id.nama_user);
            alamatUser = itemView.findViewById(R.id.alamat_user);
            approveButton = itemView.findViewById(R.id.approve);
            rejectButton = itemView.findViewById(R.id.rejection);
        }
    }

    public void upToDate(List<UserModel.User> newList){
        users = new ArrayList<>();
        users.addAll(newList);
        notifyDataSetChanged();
    }

}
