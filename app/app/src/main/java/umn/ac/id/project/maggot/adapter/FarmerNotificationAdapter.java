package umn.ac.id.project.maggot.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.global.Helper;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.NotificationUserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class FarmerNotificationAdapter extends RecyclerView.Adapter<FarmerNotificationAdapter.ListApprovalRejectionViewHolder> {
    Context context;
    private ArrayList<NotificationUserModel.Notification> notifications;
    public OnItemClickListener listener;

    public FarmerNotificationAdapter(Context context, ArrayList<NotificationUserModel.Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
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
        View view = inflater.inflate(R.layout.notification_row, parent, false);

        return new ListApprovalRejectionViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListApprovalRejectionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.type.setText("Penarikan dana");
        holder.totalAmount.setText("Rp " + Helper.formatRupiah(notifications.get(position).getWithdrawal_amount()));

//        Modal Dialog On CLick Listener
        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuild = new AlertDialog.Builder(context);
                View myView = ((Activity)context).getLayoutInflater().inflate(R.layout.modal_farmer_approve_notification, null);
                TextView tvNama = myView.findViewById(R.id.tvPanjang);
                tvNama.setText("Apakah Anda yakin ingin melakukan konfirmasi pengambilan uang sebesar Rp " + Helper.formatRupiah(notifications.get(position).getWithdrawal_amount()) + "?");
                Button btnSubmit = myView.findViewById(R.id.btnkonf);
                Button btnBatal = myView.findViewById(R.id.btnbatal);

                myBuild.setView(myView);
                AlertDialog dialog = myBuild.create();
                dialog.show();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiService.endpoint().approveWithdrawalRequest(notifications.get(position).getToken(), "Bearer " + new UserSharedPreference(context).getToken()).enqueue(new Callback<NotificationUserModel>() {
                            @Override
                            public void onResponse(Call<NotificationUserModel> call, Response<NotificationUserModel> response) {
                                if(response.isSuccessful()) {
                                    Toast.makeText(context, "Pencairan berhasil dikonfirmasi!", Toast.LENGTH_LONG).show();
                                    listener.onItemClick(position);
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
                                Toast.makeText(context, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_SHORT).show();
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
                View myView = ((Activity)context).getLayoutInflater().inflate(R.layout.modal_farmer_reject_notification, null);
                TextView tvNama = myView.findViewById(R.id.tvPanjang);
                tvNama.setText("Apakah Anda yakin ingin melakukan pembatalan pengambilan uang sebesar Rp "+ Helper.formatRupiah(notifications.get(position).getWithdrawal_amount()) +"?");
                Button btnSubmit = myView.findViewById(R.id.btnkonf);
                Button btnBatal = myView.findViewById(R.id.btnbatal);

                myBuild.setView(myView);
                AlertDialog dialog = myBuild.create();
                dialog.show();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiService.endpoint().rejectFarmerWithdrawalRequest(notifications.get(position).getToken(), "Bearer " + new UserSharedPreference(context).getToken()).enqueue(new Callback<NotificationUserModel>() {
                            @Override
                            public void onResponse(Call<NotificationUserModel> call, Response<NotificationUserModel> response) {
                                if(response.isSuccessful()) {
                                    Toast.makeText(context, "Pencairan berhasil dibatalkan!", Toast.LENGTH_LONG).show();
                                    listener.onItemClick(position);
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
                                Toast.makeText(context, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_SHORT).show();
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
        return notifications.size();
    }

    public class ListApprovalRejectionViewHolder extends RecyclerView.ViewHolder {
        TextView type, totalAmount;
        MaterialButton approveButton, rejectButton;

        public ListApprovalRejectionViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            type = itemView.findViewById(R.id.type);
            totalAmount = itemView.findViewById(R.id.total_amount);
            approveButton = itemView.findViewById(R.id.confirm_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }
    }

    public void upToDate(ArrayList<NotificationUserModel.Notification> newList){
        notifications = new ArrayList<>();
        notifications.addAll(newList);
        notifyDataSetChanged();
    }
}
