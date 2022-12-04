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

public class ListWargaBinaanAdapter extends RecyclerView.Adapter<ListWargaBinaanAdapter.ListWargaBinaanViewHolder> {
    Context context;
    private ArrayList<UserModel.User> peternak;

    public ListWargaBinaanAdapter(Context context, ArrayList<UserModel.User> peternak) {
        this.context = context;
        this.peternak = peternak;
    }

    @NonNull
    @Override
    public ListWargaBinaanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_warga_binaan, parent, false);
        return new ListWargaBinaanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWargaBinaanViewHolder holder, int position) {
        holder.namaPeternak.setText(peternak.get(position).getFull_name());
        holder.alamatPeternak.setText(String.valueOf(peternak.get(position).getAddress()));
//        holder.profilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }

    @Override
    public int getItemCount() {
        return peternak.size();
    }

    public class ListWargaBinaanViewHolder extends RecyclerView.ViewHolder {
        TextView namaPeternak, alamatPeternak;
        ImageView profilePicture;
        public ListWargaBinaanViewHolder(@NonNull View itemView) {
            super(itemView);

            namaPeternak = itemView.findViewById(R.id.namaPeternak);
            alamatPeternak = itemView.findViewById(R.id.alamatPeternak);
            profilePicture = itemView.findViewById(R.id.profilePicture);
        }
    }

}
