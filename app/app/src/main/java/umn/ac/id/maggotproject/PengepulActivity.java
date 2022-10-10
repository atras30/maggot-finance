package umn.ac.id.maggotproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PengepulActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengepul);

        Button daftarWargaButton = findViewById(R.id.daftarWargaButton);
        Button beliMaggotButton = findViewById(R.id.beliMaggotWargaButton);
        daftarWargaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PengepulActivity.this, ListPeternakActivity.class);
                startActivity(intent);
            }
        });
        beliMaggotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PengepulActivity.this, BuyMaggotActivity.class);
                startActivity(intent);
            }
        });
    }
}