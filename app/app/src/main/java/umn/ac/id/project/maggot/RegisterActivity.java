package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

import java.io.IOException;
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
    private TextView tvKembali;
    private TextView tvSnK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvKembali = findViewById(R.id.tvBack);
        tvSnK = findViewById(R.id.syaratdanketentuan);
        tvSnK.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString ss = new SpannableString("Dengan mendaftar, saya menyetujui syarat dan ketentuan yang berlaku pada aplikasi ini.");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "https://www.termsfeed.com/live/9f36a58b-8b91-4538-ba03-581d69156513";
                Intent goToSyarat = new Intent(Intent.ACTION_VIEW);
                goToSyarat.setData(Uri.parse(url));
                startActivity(goToSyarat);
            }
        };

        ss.setSpan(clickableSpan1, 34, 54, Spanned.SPAN_COMPOSING);
        tvSnK.setText(ss);

        tvKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

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

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        EditText name = findViewById(R.id.editText);
        String capitalizedName = convertToCapitalizeWord(acct.getDisplayName());
        name.setText(capitalizedName);

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

    public String convertToCapitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";

        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }

        return capitalizeWord.trim();
    }
}