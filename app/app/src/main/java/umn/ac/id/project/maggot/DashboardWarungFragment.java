package umn.ac.id.project.maggot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.GoogleAccount;
import umn.ac.id.project.maggot.global.Helper;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class DashboardWarungFragment extends Fragment {
    private final Context context;
    UserSharedPreference userSharedPreference;
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

        UserSharedPreference userSharedPreference = new UserSharedPreference(context);
        String authorizationToken = "Bearer " + userSharedPreference.getToken();

        ApiService.endpoint().getUser(authorizationToken).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()) {
                    UserModel.User user = response.body().getUser();
                    userSharedPreference.setUser(new AuthenticationModel.Result(userSharedPreference.getToken(), "", user, null));

                    try {
                        TextView name = view.findViewById(R.id.namaWarung);
                        TextView address = view.findViewById(R.id.alamatWarung);
                        name.setText(user.getFull_name());
                        address.setText(user.getAddress());
                        ImageButton btnSecret = view.findViewById(R.id.buttonSecret);
                        btnSecret.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView tvSaldo = view.findViewById(R.id.saldoWarung);

                                if(tvSaldo.getText().toString().contains("*")) {
                                    tvSaldo.setText(Helper.formatRupiah(new UserSharedPreference(context).getUser().getBalance()));
                                } else {
                                    tvSaldo.setText("**********");
                                }
                            }
                        });
                    } catch (Exception e) {
                        call.cancel();
                    }
                } else {
                    try {
                        Toast.makeText(context, "Masalah: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(context, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_LONG).show();
            }
        });

        ImageView barcodeImage = view.findViewById(R.id.barcode_image);

        tvSaldo = view.findViewById(R.id.saldoWarung);
        tvSaldo.setText("**********");

        MaterialButton logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            new GoogleAccount(context).signOut();
        });

        try {
            String email = new UserSharedPreference(context).getUser().getEmail();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(email, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);
            barcodeImage.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            Toast.makeText(context, "Masalah: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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