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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.model.WarungModel;

public class DetailWarungAdapter extends RecyclerView.Adapter<DetailWarungAdapter.DetailWarungViewHolder> {
    Context context;
    private ArrayList<UserModel.User> warung;

    public DetailWarungAdapter(Context context, ArrayList<UserModel.User> warung) {
        this.context = context;
        this.warung = warung;
    }

    @NonNull
    @Override
    public DetailWarungViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_detail_warung, parent, false);
        return new DetailWarungViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWarungViewHolder holder, int position) {
        holder.namaWarung.setText(warung.get(position).getFull_name());
        holder.alamatWarung.setText(String.valueOf(warung.get(position).getAddress()));
//        holder.profilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }

    @Override
    public int getItemCount() {
        return warung.size();
    }

    public class DetailWarungViewHolder extends RecyclerView.ViewHolder {
        TextView namaWarung, alamatWarung;
        ImageView profilePicture;
        CardView delete;
        public DetailWarungViewHolder(@NonNull View itemView) {
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
