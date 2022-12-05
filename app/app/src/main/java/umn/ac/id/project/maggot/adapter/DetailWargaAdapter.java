package umn.ac.id.project.maggot.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class DetailWargaAdapter extends RecyclerView.Adapter<DetailWargaAdapter.DetailWargaViewHolder> {
    Context context;
    private ArrayList<UserModel.User> peternak;
    private OnItemClickListener listener;

    public DetailWargaAdapter(Context context, ArrayList<UserModel.User> peternak) {
        this.context = context;
        this.peternak = peternak;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public DetailWargaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_detail_warga, parent, false);
        return new DetailWargaViewHolder(view, listener);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        listener = clickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWargaViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.namaPeternak.setText(peternak.get(position).getFull_name());
        holder.alamatPeternak.setText(String.valueOf(peternak.get(position).getAddress()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuild = new AlertDialog.Builder(context);
                View myView = ((Activity)context).getLayoutInflater().inflate(R.layout.modal_approve, null);
                TextView tvNama = myView.findViewById(R.id.tvPanjang);
                tvNama.setText("Apakah Anda yakin ingin menghapus "+peternak.get(position).getFull_name()+" sebagai warga penerima manfaat di bank sampah Anda?");
                Button btnSubmit = myView.findViewById(R.id.btnkonf);
                Button btnBatal = myView.findViewById(R.id.btnbatal);

                myBuild.setView(myView);
                AlertDialog dialog = myBuild.create();
                dialog.show();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(peternak.get(position).getBalance() == 0) {
                            ApiService.endpoint().deleteUser(peternak.get(position).getId()).enqueue(new Callback<UserModel>() {
                                @Override
                                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                    String message = response.body().deleteUser();
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    listener.onItemClick(position);
                                }

                                @Override
                                public void onFailure(Call<UserModel> call, Throwable t) {
                                    Toast.makeText(context, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(context.getApplicationContext(), "Saldo warga belum 0!", Toast.LENGTH_SHORT).show();
                        }
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
        return peternak.size();
    }

    public class DetailWargaViewHolder extends RecyclerView.ViewHolder {
        TextView namaPeternak, alamatPeternak;
        ImageView profilePicture;
        CardView delete;
        public DetailWargaViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            namaPeternak = itemView.findViewById(R.id.namaPeternak);
            alamatPeternak = itemView.findViewById(R.id.alamatPeternak);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            delete = itemView.findViewById(R.id.deletewarga);
        }
    }

    public void upToDate(ArrayList<UserModel.User> newList){
        peternak = new ArrayList<>();
        peternak.addAll(newList);
        notifyDataSetChanged();
    }

}
