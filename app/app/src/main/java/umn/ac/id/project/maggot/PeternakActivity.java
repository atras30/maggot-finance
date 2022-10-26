package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import umn.ac.id.project.maggot.QrCodeActivity;

public class PeternakActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peternak);

        Button qrCodeGeneratorButton = findViewById(R.id.qr_code_generator_button);
        qrCodeGeneratorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeternakActivity.this, QrCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}