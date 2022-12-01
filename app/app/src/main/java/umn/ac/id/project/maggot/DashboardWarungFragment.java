package umn.ac.id.project.maggot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Formatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class DashboardWarungFragment extends Fragment {
    private Context context;
    UserSharedPreference userSharedPreference;
    ImageButton btnSecret;
    TextView tvSaldo;

    Toast toast = null;
    public DashboardWarungFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_warung, container, false);

        ImageView barcodeImage = view.findViewById(R.id.barcode_image);

        btnSecret = view.findViewById(R.id.buttonSecret);

        tvSaldo = view.findViewById(R.id.saldoWarung);
        tvSaldo.setText("**********");

        DecimalFormatSymbols formatid = new DecimalFormatSymbols();

        formatid.setMonetaryDecimalSeparator(',');
        formatid.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("#,###.00", formatid);

        MaterialButton logoutButton = view.findViewById(R.id.logout_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(context, gso);

        logoutButton.setOnClickListener(v -> {
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    new UserSharedPreference(context).logout();
                    showToastMessage("Logout Complete!");
                    navigateToLoginPage();
                    ((Activity)context).finish();
                }
            });
        });

        btnSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvSaldo.getText().toString().contains("*")) {
                    tvSaldo.setText(df.format(new UserSharedPreference(context).getUser().getBalance()));
                }
                else
                {
                    tvSaldo.setText("**********");
                }
            }
        });

        userSharedPreference = new UserSharedPreference(context);

        String authorization = "Bearer " + userSharedPreference.getToken();
        ApiService.endpoint().getUser(authorization).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()) {
                    UserModel.User user = response.body().getUser();
                    try {
                        TextView name = view.findViewById(R.id.namaWarung);
                        TextView address = view.findViewById(R.id.alamatWarung);
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
            String email = new UserSharedPreference(context).getUser().getEmail();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(email, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);
            barcodeImage.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

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