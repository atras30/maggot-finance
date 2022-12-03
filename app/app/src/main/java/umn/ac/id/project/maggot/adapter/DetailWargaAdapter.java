package umn.ac.id.project.maggot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.UserModel;

public class DetailWargaAdapter extends RecyclerView.Adapter<DetailWargaAdapter.DetailWargaViewHolder> {
    Context context;
    private List<PeternakModel.Peternak> peternak;

    public DetailWargaAdapter(Context context, List<PeternakModel.Peternak> peternak) {
        this.context = context;
        this.peternak = peternak;
    }

    @NonNull
    @Override
    public DetailWargaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_detail_warga, parent, false);
        return new DetailWargaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWargaViewHolder holder, int position) {
        holder.namaPeternak.setText(peternak.get(position).getFull_name());
        holder.alamatPeternak.setText(String.valueOf(peternak.get(position).getAddress()));
//        holder.profilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }

    @Override
    public int getItemCount() {
        return peternak.size();
    }

    public class DetailWargaViewHolder extends RecyclerView.ViewHolder {
        TextView namaPeternak, alamatPeternak;
        ImageView profilePicture;
        CardView delete;
        public DetailWargaViewHolder(@NonNull View itemView) {
            super(itemView);

            namaPeternak = itemView.findViewById(R.id.namaPeternak);
            alamatPeternak = itemView.findViewById(R.id.alamatPeternak);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            delete = itemView.findViewById(R.id.deletewarga);
        }
    }

    public void upToDate(List<PeternakModel.Peternak> newList){
        peternak = new ArrayList<>();
        peternak.addAll(newList);
        notifyDataSetChanged();
    }

}
