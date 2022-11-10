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
import umn.ac.id.project.maggot.model.TransactionModel;

public class FarmerTransactionAdapter extends RecyclerView.Adapter<FarmerTransactionAdapter.FarmerTransactionViewHolder> {
    Context context;
    private List<TransactionModel> transactions;

    public FarmerTransactionAdapter(Context context, List<TransactionModel> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public FarmerTransactionAdapter.FarmerTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_row, parent, false);
        return new FarmerTransactionAdapter.FarmerTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerTransactionAdapter.FarmerTransactionViewHolder holder, int position) {
        String type = transactions.get(position).getType();

        holder.description.setText(transactions.get(position).getDescription());
        holder.date.setText(transactions.get(position).getCreated_at());
        holder.amount.setText(String.valueOf(transactions.get(position).getAmount()));

        if(type.equalsIgnoreCase("income")) {
            holder.type.setText("Dana Masuk");
//            holder.logo.setImageResource(R.drawable.farmer);
        } else if(type.equalsIgnoreCase("expense")) {
            holder.type.setText("Dana Keluar");
            holder.logo.setImageResource(R.drawable.bayar);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class FarmerTransactionViewHolder extends RecyclerView.ViewHolder {
        TextView type, description, date, amount;
        ImageView logo;
        public FarmerTransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.transaction_type);
            description = itemView.findViewById(R.id.transaction_description);
            date = itemView.findViewById(R.id.transaction_date);
            amount = itemView.findViewById(R.id.transaction_amount);
            logo = itemView.findViewById(R.id.transaction_icon);
        }
    }
}
