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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.AuthenticatedTrashManager;
import umn.ac.id.project.maggot.global.AuthenticatedUser;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class LoginActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount acct = null;

    MaterialButton googleSignInButton;
    MaterialButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleSignInButton = findViewById(R.id.login_with_google_button);
        logoutButton = findViewById(R.id.logout_button);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
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

    public void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Toast.makeText(this, "Checking account on database, please wait...", Toast.LENGTH_SHORT).show();

            try {
                task.getResult(ApiException.class);
                acct = GoogleSignIn.getLastSignedInAccount(this);

                //get all users
                ApiService.endpoint().getUsers().enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if(response.isSuccessful()) {
                            List<UserModel.User> users = response.body().getUsers();

                            //check if user is registered in database
                            for(UserModel.User user : users) {
                                Log.i("Checking user", toString());
                                if(user.getEmail().equals(acct.getEmail())) {
                                    //user exists in database, navigate to either register success activity or dashboard activity depending on approval status.
                                    AuthenticatedUser.setUser(user, null);
                                    Toast.makeText(LoginActivity.this, "Welcome, " + user.getFull_name(), Toast.LENGTH_SHORT).show();
                                    navigateRegisteredUser();
                                    return;
                                }
                            }

                            checkForTrashManager();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkForTrashManager() {
        ApiService.endpoint().getTrashManager().enqueue(new Callback<TrashManagerModel>() {
            @Override
            public void onResponse(Call<TrashManagerModel> call, Response<TrashManagerModel> response) {
                if(response.isSuccessful()) {
                    List<TrashManagerModel.TrashManagers> trashManagers = response.body().getTrashManager();

                    //check if Trash Manager is registered in database
                    for(TrashManagerModel.TrashManagers trashManager : trashManagers) {
                        Log.i("Checking trash manager", toString());
                        if(trashManager.getEmail().equals(acct.getEmail())) {
                            //trash manager exists in database, navigate to either register success activity or dashboard activity depending on approval status.
                            AuthenticatedTrashManager.setTrashManager(trashManager, null);
                            Toast.makeText(LoginActivity.this, "Welcome, " + trashManager.getNama_pengelola(), Toast.LENGTH_SHORT).show();
                            navigateRegisteredTrashManager();
                            return;
                        }
                    }

                    //User Manager does not exists in database, navigate to register activity
                    Intent navigateToRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(navigateToRegisterActivity);
                }
            }

            @Override
            public void onFailure(Call<TrashManagerModel> call, Throwable t) {

            }
        });
    }

    private void navigateRegisteredTrashManager() {
//        if(AuthenticatedTrashManager.getTrashManager().is_verified() == 1) {
            Intent navigateToDashboardTrashManagerActivity = new Intent(LoginActivity.this, TrashManagerDashboardActivity.class);
            startActivity(navigateToDashboardTrashManagerActivity);
//        } else {
//            Intent navigateToRegistrationSuccessActivity = new Intent(LoginActivity.this, RegistrationSuccess.class);
//            startActivity(navigateToRegistrationSuccessActivity);
//        }
    }

    private void navigateRegisteredUser() {
        Log.i("Verified", String.valueOf(AuthenticatedUser.getUser().is_verified()));
        if(AuthenticatedUser.getUser().is_verified() == 1) {
            Intent navigateToDashboardUserActivity = new Intent(LoginActivity.this, UserDashboardActivity.class);
            startActivity(navigateToDashboardUserActivity);
        } else {
            Intent navigateToRegistrationSuccessActivity = new Intent(LoginActivity.this, RegistrationSuccess.class);
            startActivity(navigateToRegistrationSuccessActivity);
        }
    }

    public void navigateToSecondActivity(GoogleSignInAccount acct) {
        Log.i("name", acct.getDisplayName());
        Log.i("email", acct.getEmail());
        Intent intent = new Intent(LoginActivity.this, PengepulActivity.class);
        startActivity(intent);
    }

    public void navigateToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}