package umn.ac.id.project.maggot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.retrofit.ApiErrorHandler;
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
        if (userSharedPreference.getUser() != null) {
            navigateRegisteredUser();
            return;
        } else if (trashManagerSharedPreference.getTrashManager() != null) {
            navigateRegisteredTrashManager();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userSharedPreference = new UserSharedPreference(this);//Guest Middleware
        trashManagerSharedPreference = new TrashManagerSharedPreference(this);

        if (userSharedPreference.getUser() != null) {
            navigateRegisteredUser();
            return;
        }

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.server_client_id)).requestEmail().build();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            showToastMessage("Mencari data pengguna...");
            Toasty.success(getApplicationContext(), "Mencari data pengguna...", Toast.LENGTH_SHORT, true).show();
            Toasty.success(getApplicationContext(), "Data ditemukan", Toast.LENGTH_SHORT, true).show();

            try {
                task.getResult(ApiException.class);
                acct = GoogleSignIn.getLastSignedInAccount(this);

                //login with email from retrieved email from gmail account
                loginWithGmail(acct.getIdToken());
            } catch (ApiException e) {
                String errorMessage = e.toString();
                Log.i("Api Exception", errorMessage);
                Toast.makeText(getApplicationContext(), "Masalah: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginWithGmail(String googleToken) {
        Log.i("Google Token", googleToken);

        ApiService.endpoint().login(googleToken).enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                if (response.isSuccessful()) {
                    AuthenticationModel.Result result = response.body().login();
                    Log.i("Success", result.toString());
                    if (result.getUser() != null) {
                        userSharedPreference.setUser(result);
                        navigateRegisteredUser();
                        return;
                    } else if (result.getTrash_manager() != null) {
                        trashManagerSharedPreference.setTrashManager(result);
                        startActivity(new Intent(getApplicationContext(), HomePagePengelolaBankSampah.class));
                        finish();
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.i("Error", errorMessage);

                        if (ApiErrorHandler.getErrorMessage(errorMessage).equalsIgnoreCase("User was not found.")) {
//                            showToastMessage("Silakan registrasi di aplikasi kami.");
                            Toasty.success(getApplicationContext(), "Silakan registrasi di aplikasi kami.", Toast.LENGTH_SHORT, true).show();
                            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                Log.i("Error", t.getMessage());
                Toast.makeText(LoginActivity.this, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateRegisteredTrashManager() {
        Intent navigateToDashboardTrashManagerActivity = new Intent(LoginActivity.this, HomePagePengelolaBankSampah.class);
        startActivity(navigateToDashboardTrashManagerActivity);
        finish();
    }


    private void navigateRegisteredUser() {
        if (userSharedPreference.getUser().is_verified() == 1) {
            if (userSharedPreference.getUser().getDeleted_at_date_time() == null) {
                if (userSharedPreference.getUser().getRole().equals("farmer")) {
                    Intent navigateToFarmerDashboardActivity = new Intent(LoginActivity.this, FarmerDashboardActivity.class);
                    startActivity(navigateToFarmerDashboardActivity);
                    finish();
                } else if (userSharedPreference.getUser().getRole().equals("shop")) {
                    Intent navigateToDashboardShopActivity = new Intent(LoginActivity.this, DashboardWarungActivity.class);
                    startActivity(navigateToDashboardShopActivity);
                    finish();
                }
            } else {
                Intent navigateToInactive = new Intent(LoginActivity.this, InactiveActivity.class);
                startActivity(navigateToInactive);
                finish();
            }
        } else {
            Intent navigateToRegistrationSuccessActivity = new Intent(LoginActivity.this, RegistrationSuccess.class);
            startActivity(navigateToRegistrationSuccessActivity);
        }
    }

    public void showToastMessage(String message) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}