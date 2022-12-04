package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.ApprovalRejectionModel;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class InputWargaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_warga);

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputWargaActivity.this, ListPeternakActivity.class);
                startActivity(intent);
            }
        });
        Button dw = findViewById(R.id.daftarkanWarga);
        dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNW = findViewById(R.id.etNamaWarga);
                String fullName = etNW.getText().toString();
                EditText etEW = findViewById(R.id.etEmailWarga);
                String email = etEW.getText().toString();
                EditText etAW = findViewById(R.id.etAlamatWarga);
                String address = etAW.getText().toString();
                EditText etHW = findViewById(R.id.etNoHPWarga);
                String phoneNumber = etHW.getText().toString();
                int trashManagerId = new TrashManagerSharedPreference(InputWargaActivity.this).getTrashManager().getId();

                ApiService.endpoint().registerUser(fullName, email, "farmer", trashManagerId, address, phoneNumber).enqueue(new Callback<AuthenticationModel>() {
                    @Override
                    public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                        if (response.isSuccessful()) {
                            String responseMessage = response.body().registerUser();
                            Log.i("Message", responseMessage);
                            approveRegisteredUser(email);
                            Toast.makeText(InputWargaActivity.this, responseMessage, Toast.LENGTH_SHORT).show();
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

                Intent intent = new Intent(InputWargaActivity.this, ListPeternakActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    public void approveRegisteredUser(String email) {
        ApiService.endpoint().approvalUserRegistration(email).enqueue(new Callback<ApprovalRejectionModel>() {
            @Override
            public void onResponse(Call<ApprovalRejectionModel> call, Response<ApprovalRejectionModel> response) {
                String message = response.body().approvalUserRegistration();
                Toast.makeText(InputWargaActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ApprovalRejectionModel> call, Throwable t) {
                Toast.makeText(InputWargaActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}