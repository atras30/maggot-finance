package umn.ac.id.project.maggot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.PeternakModel;

public class ListPeternakAdapter extends RecyclerView.Adapter<ListPeternakAdapter.ListPeternakViewHolder> {
    Context context;
    private List<PeternakModel.Peternak> peternak;

    public ListPeternakAdapter(Context context, List<PeternakModel.Peternak> peternak) {
        this.context = context;
        this.peternak = peternak;
    }

    @NonNull
    @Override
    public ListPeternakViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_peternak_row, parent, false);
        return new ListPeternakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPeternakViewHolder holder, int position) {
        holder.namaPeternak.setText(peternak.get(position).getFull_name());
        holder.balancePeternak.setText(String.valueOf(peternak.get(position).getBalance()));
//        holder.profilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }

    @Override
    public int getItemCount() {
        return peternak.size();
    }

    public class ListPeternakViewHolder extends RecyclerView.ViewHolder {
        TextView namaPeternak, balancePeternak;
        ImageView profilePicture;
        public ListPeternakViewHolder(@NonNull View itemView) {
            super(itemView);

            namaPeternak = itemView.findViewById(R.id.namaPeternak);
            balancePeternak = itemView.findViewById(R.id.balancePeternak);
            profilePicture = itemView.findViewById(R.id.profilePicture);
        }
    }

}
