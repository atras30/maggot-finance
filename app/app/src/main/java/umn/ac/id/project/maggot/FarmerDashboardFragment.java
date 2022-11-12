package umn.ac.id.project.maggot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class FarmerDashboardFragment extends Fragment {
    private Context context;
    MaterialButton logoutButton;
    UserSharedPreference userSharedPreference;
    Toast toast = null;

    public FarmerDashboardFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSharedPreference = new UserSharedPreference(context);

        String authorization = "Bearer " + userSharedPreference.getToken();
        ApiService.endpoint().getUser(authorization).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()) {
                    UserModel.User user = response.body().getUser();
                    try {
                        TextView name = requireView().findViewById(R.id.name);
                        TextView address = getView().findViewById(R.id.address);
                        name.setText(user.getFull_name());
                        address.setText(user.getAddress());
                    } catch (Exception e) {
                        call.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farmer_dashboard, container, false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(context, gso);
        logoutButton = view.findViewById(R.id.farmer_logout_button);
        logoutButton.setOnClickListener(v -> {
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    userSharedPreference.logout();
                    showToastMessage("Logout Complete!");
                    navigateToLoginPage();
                    ((Activity)context).finish();
                }
            });
        });
        return view;
    }

    public void showToastMessage(String message) {
        if(toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void navigateToLoginPage() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
}