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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class DetailWarungAdapter extends RecyclerView.Adapter<DetailWarungAdapter.DetailWarungViewHolder> {
    Context context;
    private ArrayList<UserModel.User> warung;
    private OnItemClickListener listener;
    
    public DetailWarungAdapter(Context context, ArrayList<UserModel.User> warung) {
        this.context = context;
        this.warung = warung;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public DetailWarungViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_detail_warung, parent, false);
        return new DetailWarungViewHolder(view, listener);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        listener = clickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWarungViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.namaWarung.setText(warung.get(position).getFull_name());
        holder.alamatWarung.setText(String.valueOf(warung.get(position).getAddress()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuild = new AlertDialog.Builder(context);
                View myView = ((Activity)context).getLayoutInflater().inflate(R.layout.modal_approve, null);
                TextView tvNama = myView.findViewById(R.id.tvPanjang);
                tvNama.setText("Apakah Anda yakin ingin menghapus "+warung.get(position).getFull_name()+" sebagai warung mitra di bank sampah Anda?");
                Button btnSubmit = myView.findViewById(R.id.btnkonf);
                Button btnBatal = myView.findViewById(R.id.btnbatal);

                myBuild.setView(myView);
                AlertDialog dialog = myBuild.create();
                dialog.show();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(warung.get(position).getBalance() == 0) {
                            ApiService.endpoint().deleteUser(warung.get(position).getId()).enqueue(new Callback<UserModel>() {
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
                            Toast.makeText(context.getApplicationContext(), "Saldo warung belum 0!", Toast.LENGTH_SHORT).show();
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
        return warung.size();
    }

    public class DetailWarungViewHolder extends RecyclerView.ViewHolder {
        TextView namaWarung, alamatWarung;
        ImageView profilePicture;
        CardView delete;
        public DetailWarungViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            namaWarung = itemView.findViewById(R.id.namaWarung);
            alamatWarung = itemView.findViewById(R.id.alamatWarung);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            delete = itemView.findViewById(R.id.deletewarung);
        }
    }

    public void upToDate(ArrayList<UserModel.User> newList){
        warung = new ArrayList<>();
        warung.addAll(newList);
        notifyDataSetChanged();
    }

}
