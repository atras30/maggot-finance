package umn.ac.id.project.maggot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.GoogleAccount;
import umn.ac.id.project.maggot.global.Helper;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class FarmerDashboardFragment extends Fragment {
    private Context context;
    MaterialButton logoutButton;
    ImageView qrCodeImage;
    UserSharedPreference userSharedPreference;
    Toast toast = null;
    ImageButton btnSecret;
    TextView tvSaldo;

    public FarmerDashboardFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farmer_dashboard, container, false);

//        Helper.refreshToken(context, view);

        btnSecret = view.findViewById(R.id.buttonSecret);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(context, gso);

        qrCodeImage = view.findViewById(R.id.barcode_image);
        logoutButton = view.findViewById(R.id.farmer_logout_button);
        btnSecret = view.findViewById(R.id.buttonSecret);

        populateLastData(view);

        tvSaldo = view.findViewById(R.id.angkaSaldo);
        tvSaldo.setText("**********");

        DecimalFormatSymbols formatid = new DecimalFormatSymbols();

        formatid.setMonetaryDecimalSeparator(',');
        formatid.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("#,###.00", formatid);

        df.setDecimalFormatSymbols(formatid);

        logoutButton.setOnClickListener(v -> {
            GoogleAccount account = new GoogleAccount(context);
            account.signOut();
            navigateToLoginPage();
            ((Activity)context).finish();
        });

        btnSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSaldo = view.findViewById(R.id.angkaSaldo);
                if (tvSaldo.getText().toString().contains("*")) {
                    tvSaldo.setText(df.format(new UserSharedPreference(context).getUser().getBalance()));
                } else {
                    tvSaldo.setText("**********");
                }
            }
        });

        userSharedPreference = new UserSharedPreference(context);

        String authorization = "Bearer " + userSharedPreference.getToken();
        ApiService.endpoint().getUser(authorization).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel.User user = response.body().getUser();
                    try {
                        TextView name = view.findViewById(R.id.name);
                        TextView address = view.findViewById(R.id.address);
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

        try {
            Log.i("Shared Preference", new UserSharedPreference(context).toString());
            String email = new UserSharedPreference(context).getUser().getEmail();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(email, BarcodeFormat.QR_CODE, 700, 700);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImage.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void populateLastData(View view) {
        TextView name = view.findViewById(R.id.name);
        TextView address = view.findViewById(R.id.address);
        tvSaldo = view.findViewById(R.id.angkaSaldo);
        UserSharedPreference userSharedPreference = new UserSharedPreference(context);
        if (userSharedPreference.getUser() != null) {
            name.setText(userSharedPreference.getUser().getFull_name());
            address.setText(userSharedPreference.getUser().getAddress());
            tvSaldo.setText("**********");
        }
    }

    public void showToastMessage(String message) {
        if (toast != null) {
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