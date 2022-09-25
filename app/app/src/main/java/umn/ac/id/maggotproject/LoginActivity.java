package umn.ac.id.maggotproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.maggotproject.global.AuthenticatedUser;
import umn.ac.id.maggotproject.model.AuthenticationModel;
import umn.ac.id.maggotproject.retrofit.ApiService;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    TextView email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(email.getText().toString(), password.getText().toString());
            }
        });
    }

    private void login(String email, String password) {
        ApiService.endpoint().login(email, password).enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                if(response.isSuccessful()) {
                    AuthenticationModel.Result result = response.body().login();

                    if(result.getMessage() != null) {
                        Toast.makeText(LoginActivity.this, "Wrong email or Password!", Toast.LENGTH_LONG).show();
                        AuthenticatedUser.logout();
                    } else {
                        AuthenticatedUser.setUser(result.getUser());
                        Toast.makeText(LoginActivity.this, "Login Success, Hello " + AuthenticatedUser.getUser().getFull_name(), Toast.LENGTH_LONG).show();
                    }

                    Log.d("Login Response : ", result.toString());
                }
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                Log.d("Error response : ", t.toString());
            }
        });
    }
}