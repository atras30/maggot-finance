package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.material.button.MaterialButton;

public class RegisterActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);

        MaterialButton registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            Log.i("Notification : ", "Registration button has been clicked.");
        });
    }

    public void registerNewAccount(View view) {
        MaterialButton registerButton = findViewById(R.id.register_button);

        if(registerButton.isChecked()) {
            Log.i("Request", "KIRIM REQUEST KE DATABASE");
        }
    }
}