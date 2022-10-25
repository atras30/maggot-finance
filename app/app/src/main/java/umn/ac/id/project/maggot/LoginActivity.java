package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.AuthenticatedUser;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class LoginActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    Button loginButton;
    TextView email, password;
    ImageView googleIcon, facebookIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        loginButton = findViewById(R.id.loginButton);
        googleIcon = findViewById(R.id.google_icon);
        facebookIcon = findViewById(R.id.facebook_icon);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Login Button has been Clicked!", Toast.LENGTH_LONG).show();
                login(email.getText().toString(), password.getText().toString());
            }
        });

        googleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Log.i("Logout", "Logout Complete!");
                    }
                });
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
                    } else {
                        AuthenticatedUser.setUser(result.getUser(), result.getToken());
                        Toast.makeText(LoginActivity.this, "Login Success, Hello " + AuthenticatedUser.getUser().getFull_name(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, PengepulActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                Log.d("Error response : ", t.toString());
            }
        });
    }

    public void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void navigateToSecondActivity() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        Log.i("name", acct.getDisplayName());
        Log.i("email", acct.getEmail());
        Intent intent = new Intent(LoginActivity.this, PengepulActivity.class);
        startActivity(intent);
    }
}