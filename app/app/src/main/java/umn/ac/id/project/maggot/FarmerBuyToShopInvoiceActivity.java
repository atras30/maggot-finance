package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import umn.ac.id.project.maggot.global.Helper;
import umn.ac.id.project.maggot.global.UserSharedPreference;

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
        TextView description = findViewById(R.id.transaction_description);
        TextView amount = findViewById(R.id.transaction_amount);

        Bundle extras = getIntent().getExtras();

        date.setText(extras.getString("date"));
        description.setText("Pembayaran sembako ke" + extras.getString("shop_name"));
        amount.setText(Helper.formatRupiah(extras.getDouble("total_amount")));

        ImageButton btnSecret = findViewById(R.id.buttonSecret);
        btnSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvSaldo = findViewById(R.id.transaction_amount);

                if(tvSaldo.getText().toString().contains("*")) {
                    tvSaldo.setText(Helper.formatRupiah(extras.getDouble("total_amount")));
                } else {
                    tvSaldo.setText("**********");
                }
            }
        });
    }
}