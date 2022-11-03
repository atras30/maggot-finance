package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.SpinnerRegisterAdapter;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<TrashManagerModel.TrashManagers> results;
    private TrashManagerModel.TrashManagers selectedTrashManagerOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ApiService.endpoint().getTrashManager().enqueue(new Callback<TrashManagerModel>() {
            @Override
            public void onResponse(@NonNull Call<TrashManagerModel> call, @NonNull Response<TrashManagerModel> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    results = response.body().getTrashManager();
                    Spinner daerahSpinner = findViewById(R.id.daerah_spinner);
                    daerahSpinner.setOnItemSelectedListener(RegisterActivity.this);
                    SpinnerRegisterAdapter customAdapter = new SpinnerRegisterAdapter(getApplicationContext(),results);
                    daerahSpinner.setAdapter(customAdapter);
                    Log.d("Register activity", results.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrashManagerModel> call, @NonNull Throwable t) {
                Log.d("Reigster Activity", t.toString());
            }
        });

        MaterialButton registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterButtonClick();
            }
        });
    }

    private void onRegisterButtonClick() {
        int trashManagerId = selectedTrashManagerOption.getId();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        String email = acct.getEmail();
        String fullName = ((EditText) findViewById(R.id.editText)).getText().toString();
        String address = ((EditText) findViewById(R.id.address)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.phone_number)).getText().toString();
        CheckBox agreementCheckbox = findViewById(R.id.registration_agreement_checkbox);

        if(!agreementCheckbox.isChecked()) {
            Toast.makeText(this, "Oops, anda harus menekan tombol setuju terhadap ketentuan yang berlaku terlebih dahulu.", Toast.LENGTH_LONG).show();
            return;
        }

        ApiService.endpoint().registerUser(fullName, email, "farmer",  trashManagerId, address, phoneNumber).enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                if(response.isSuccessful()) {
                    String responseMessage = response.body().registerUser();
                    Log.i("Message", responseMessage);
                    Toast.makeText(RegisterActivity.this, responseMessage, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, RegistrationSuccess.class);
                    startActivity(intent);

                    finish();
                }

                Log.i("Message", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                Log.i("Failure", t.getMessage().toString());
                Log.i("Failure", "Gagal Cuk");
            }
        });
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String namaPengelola = ((TextView) view.findViewById(R.id.nama_pengelola)).getText().toString();
        String namaTempat = ((TextView) view.findViewById(R.id.nama_tempat)).getText().toString();

        Log.i("Nama Pengelola : ", namaPengelola);
        Log.i("Nama Tempat : ", namaTempat);

        for(TrashManagerModel.TrashManagers trashManager : results) {
            if(trashManager.getNama_pengelola().equals(namaPengelola)) {
                selectedTrashManagerOption = trashManager;
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}