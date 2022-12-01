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
import umn.ac.id.project.maggot.model.TransactionModel;

public class ShopTransactionAdapter extends RecyclerView.Adapter<ShopTransactionAdapter.ShopTransactionViewHolder> {
    Context context;
    private List<TransactionModel.Transaction> transactions;

    public ShopTransactionAdapter(Context context, List<TransactionModel.Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ShopTransactionAdapter.ShopTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_row, parent, false);
        return new ShopTransactionAdapter.ShopTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopTransactionAdapter.ShopTransactionViewHolder holder, int position) {
        String type = transactions.get(position).getType();

        holder.description.setText(transactions.get(position).getDescription());
        holder.date.setText(transactions.get(position).getCreated_at());

        if(type.equalsIgnoreCase("income")) {
            String totalAmount = "+Rp. " + String.valueOf(transactions.get(position).getTotal_amount());
            holder.type.setText("Dana Masuk");
            holder.amount.setText(totalAmount);
            holder.amount.setTextColor(context.getResources().getColor(R.color.success));
            holder.logo.setImageResource(R.drawable.farmer_wallet_icon);
        } else if(type.equalsIgnoreCase("expense")) {
            String totalAmount = "-Rp. " + String.valueOf(transactions.get(position).getTotal_amount());
            holder.type.setText("Dana Keluar");
            holder.amount.setText(totalAmount);
            holder.amount.setTextColor(context.getResources().getColor(R.color.danger));
            holder.logo.setImageResource(R.drawable.farmer_buy_icon);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ShopTransactionViewHolder extends RecyclerView.ViewHolder {
        TextView type, description, date, amount;
        ImageView logo;
        public ShopTransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.transaction_type);
            description = itemView.findViewById(R.id.transaction_description);
            date = itemView.findViewById(R.id.transaction_date);
            amount = itemView.findViewById(R.id.transaction_amount);
            logo = itemView.findViewById(R.id.transaction_icon);
        }
    }

    public void upToDate(List<TransactionModel.Transaction> newList){
        transactions = new ArrayList<>();
        transactions.addAll(newList);
        notifyDataSetChanged();
    }
}

