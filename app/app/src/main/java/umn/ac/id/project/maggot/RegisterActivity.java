package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void navigateToRegistrationSuccessActivity(View view) {
        CheckBox agreementCheckbox = findViewById(R.id.registration_agreement_checkbox);

        if(!agreementCheckbox.isChecked()) {
            Toast.makeText(this, "Oops, anda harus menekan tombol setuju terhadap ketentuan yang berlaku terlebih dahulu.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(RegisterActivity.this, RegistrationSuccess.class);
        startActivity(intent);

        finish();
    }
}