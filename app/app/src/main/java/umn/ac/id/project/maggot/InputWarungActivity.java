package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.model.ApprovalRejectionModel;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class InputWarungActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_warung);

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputWarungActivity.this, ListWarungActivity.class);
                startActivity(intent);
            }
        });

        Button dw = findViewById(R.id.daftarkanWarung);
        dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNW = findViewById(R.id.etNamaWarung);
                String fullName = etNW.getText().toString();
                EditText etEW = findViewById(R.id.etEmailWarung);
                String email = etEW.getText().toString();
                EditText etAW = findViewById(R.id.etAlamatWarung);
                String address = etAW.getText().toString();
                EditText etHW = findViewById(R.id.etNoHPWarung);
                String phoneNumber = etHW.getText().toString();
                int trashManagerId = new TrashManagerSharedPreference(InputWarungActivity.this).getTrashManager().getId();

                ApiService.endpoint().registerUser(fullName, email, "shop", trashManagerId, address, phoneNumber).enqueue(new Callback<AuthenticationModel>() {
                    @Override
                    public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                        if (response.isSuccessful()) {
                            String responseMessage = response.body().registerUser();
                            Log.i("Message", responseMessage);
                            Toast.makeText(InputWarungActivity.this, responseMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Log.i("Message", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                        Log.i("Failure", t.getMessage().toString());
                    }
                });

                ApiService.endpoint().approvalUserRegistration(email).enqueue(new Callback<ApprovalRejectionModel>() {
                    @Override
                    public void onResponse(Call<ApprovalRejectionModel> call, Response<ApprovalRejectionModel> response) {
                        String message = response.body().approvalUserRegistration();
                        Toast.makeText(InputWarungActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ApprovalRejectionModel> call, Throwable t) {
                        Toast.makeText(InputWarungActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(InputWarungActivity.this, ListWarungActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}