package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.AuthenticatedTrashManager;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class LoginActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount acct = null;

    MaterialButton googleSignInButton;
    UserSharedPreference userSharedPreference;
    TrashManagerSharedPreference trashManagerSharedPreference;

    Toast toast = null;

    @Override
    protected void onResume() {
        super.onResume();

        //Guest Middleware
        if(userSharedPreference.getUser() != null) {
            navigateRegisteredUser();
            return;
        } else if(trashManagerSharedPreference.getTrashManager() != null) {
            navigateRegisteredTrashManager();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userSharedPreference = new UserSharedPreference(this);//Guest Middleware
        trashManagerSharedPreference = new TrashManagerSharedPreference(this);

        if(userSharedPreference.getUser() != null) {
            navigateRegisteredUser();
            return;
        }

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleSignInButton = findViewById(R.id.login_with_google_button);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
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
            showToastMessage("Checking account on database, please wait...");

            try {
                task.getResult(ApiException.class);
                acct = GoogleSignIn.getLastSignedInAccount(this);

                //login with email from retrieved email from gmail account
                loginWithGmail(acct.getEmail());
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Tag", e.toString());
            }
        }
    }

    private void loginWithGmail(String email) {
        ApiService.endpoint().login(email).enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                if(response.isSuccessful()) {
                    AuthenticationModel.Result result = response.body().login();

                    if(result.getUser() == null) {
                        loginTrashManager(result);

                        return;
                    }

                    userSharedPreference.setUser(result);

                    navigateRegisteredUser();
                } else {
                    //Error handling for user was not found, etc...
                    try {
                        Gson gson = new Gson();
                        assert response.errorBody() != null;
                        String errorMessage = gson.fromJson(response.errorBody().string(), AuthenticationModel.ErrorHandler.class).getMessage();

                        if(errorMessage.equalsIgnoreCase("User was not found.")) {
                            Toast.makeText(LoginActivity.this, "User was not found.", Toast.LENGTH_SHORT).show();
                        } else if(errorMessage.equalsIgnoreCase("")) {
                            //do something
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginTrashManager(AuthenticationModel.Result result) {
        ApiService.endpoint().loginTrashManager(acct.getEmail()).enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                if(response.isSuccessful()) {
//                    Log.i("Tag", response.body().loginTrashManager().toString());
                    AuthenticationModel.ResultTrashManager result = response.body().loginTrashManager();
                    trashManagerSharedPreference.setTrashManager(result);
                    navigateRegisteredTrashManager();
//                    Log.i("Setting Trash Manager", trashManagerSharedPreference.getTrashManager() + " " + trashManagerSharedPreference.getToken());
//                    Log.i("Getting Trash Manager", trashManagerSharedPreference.getTrashManager() + " " + trashManagerSharedPreference.getToken());
                }
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateRegisteredTrashManager() {
        Intent navigateToDashboardTrashManagerActivity = new Intent(LoginActivity.this, HomePagePengelolaBankSampah.class);
        startActivity(navigateToDashboardTrashManagerActivity);
        finish();
    }


    private void navigateRegisteredUser() {
        if(userSharedPreference.getUser().is_verified() == 1) {
            if(userSharedPreference.getUser().getRole().equals("farmer")) {
                Intent navigateToFarmerDashboardActivity = new Intent(LoginActivity.this, FarmerDashboardActivity.class);
                startActivity(navigateToFarmerDashboardActivity);
                finish();
            } else if (userSharedPreference.getUser().getRole().equals("shop")) {
                Intent navigateToDashboardShopActivity = new Intent(LoginActivity.this, DashboardWarungActivity.class);
                startActivity(navigateToDashboardShopActivity);
                finish();
            }
        } else {
            Intent navigateToRegistrationSuccessActivity = new Intent(LoginActivity.this, RegistrationSuccess.class);
            startActivity(navigateToRegistrationSuccessActivity);
        }
    }

    public void showToastMessage(String message) {
        if(toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}