package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class FarmerBuyToShopInvoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_buy_to_shop_invoice);

        MaterialButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        TextView date = findViewById(R.id.transaction_date);
        TextView shopEmail = findViewById(R.id.shop_email);
        TextView description = findViewById(R.id.transaction_description);
        TextView amount = findViewById(R.id.transaction_amount);

        Bundle extras = getIntent().getExtras();

        date.setText(extras.getString("date"));
        shopEmail.setText(extras.getString("email"));
        description.setText("Pembayaran " + extras.getDouble("total_amount") + " ke " + extras.getString("shop_name"));
        amount.setText(String.valueOf(extras.getDouble("total_amount")));
    }
}