package umn.ac.id.project.maggot.global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.LoginActivity;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class GoogleAccount {
    private Context context;
    private GoogleSignInClient gsc;

    public GoogleAccount(Context context) {
        this.context = context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();;
        gsc = GoogleSignIn.getClient(context, gso);
    }

    public void signOut() {
        UserSharedPreference userSharedPreference = new UserSharedPreference(context);
        TrashManagerSharedPreference trashManagerSharedPreference = new TrashManagerSharedPreference(context);

        Log.i("Logout Token user", "Bearer " + userSharedPreference.getToken());
        Log.i("Logout Token Trash.M", "Bearer " + trashManagerSharedPreference.getToken());

        if(userSharedPreference.getToken() != null) {
            ApiService.endpoint().logout("Bearer " + userSharedPreference.getToken(), "").enqueue(new Callback<AuthenticationModel>() {
                @Override
                public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                    if(response.isSuccessful()) {
                        Log.i("Status", response.body().logout());

                        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                Toast.makeText(context, "Anda telah keluar!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        try {
                            Log.i("Error 2", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                    Log.i("Error 3", t.getMessage());
                }
            });

            new UserSharedPreference(context).logout();
            context.startActivity(new Intent(context, LoginActivity.class));
            ((Activity)context).finish();
        }

        if(trashManagerSharedPreference.getToken() != null) {
            ApiService.endpoint().logout("Bearer " + trashManagerSharedPreference.getToken(), "").enqueue(new Callback<AuthenticationModel>() {
                @Override
                public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                    if(response.isSuccessful()) {
                        Log.i("Status", response.body().logout());

                        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                Toast.makeText(context, "Anda telah keluar!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        try {
                            Log.i("Error 4", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                    Log.i("Error 5", t.getMessage());
                }
            });

            new TrashManagerSharedPreference(context).logout();
            context.startActivity(new Intent(context, LoginActivity.class));
            ((Activity)context).finish();
        }
    }
}
