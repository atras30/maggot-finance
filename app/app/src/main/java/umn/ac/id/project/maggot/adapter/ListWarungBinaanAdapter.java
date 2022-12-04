package umn.ac.id.project.maggot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.model.WarungModel;

public class ListWarungBinaanAdapter extends RecyclerView.Adapter<ListWarungBinaanAdapter.ListWarungBinaanViewHolder> {
    Context context;
    private ArrayList<UserModel.User> warung;

    public ListWarungBinaanAdapter(Context context, ArrayList<UserModel.User> warung) {
        this.context = context;
        this.warung = warung;
    }

    @NonNull
    @Override
    public ListWarungBinaanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_warung_binaan, parent, false);
        return new ListWarungBinaanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWarungBinaanViewHolder holder, int position) {
        holder.namaWarung.setText(warung.get(position).getFull_name());
        holder.alamatWarung.setText(String.valueOf(warung.get(position).getAddress()));
//        holder.profilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }

    @Override
    public int getItemCount() {
        return warung.size();
    }

    public class ListWarungBinaanViewHolder extends RecyclerView.ViewHolder {
        TextView namaWarung, alamatWarung;
        ImageView profilePicture;
        public ListWarungBinaanViewHolder(@NonNull View itemView) {
            super(itemView);

            namaWarung = itemView.findViewById(R.id.namaWarung);
            alamatWarung = itemView.findViewById(R.id.alamatWarung);
            profilePicture = itemView.findViewById(R.id.profilePicture);
        }
    }

}
