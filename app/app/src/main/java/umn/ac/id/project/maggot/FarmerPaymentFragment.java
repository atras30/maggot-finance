package umn.ac.id.project.maggot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.TransactionModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class FarmerPaymentFragment extends Fragment {
    private Context context;
    ArrayAdapter<String> nameAdapter;
    List<WarungModel.Warung> results;
    //QR Scanner
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    });

    public FarmerPaymentFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmer_payment, container, false);

        //QR Code scanner button logic
        MaterialButton scanQrCodeButton = view.findViewById(R.id.scan_qr_code_button);
        scanQrCodeButton.setOnClickListener(v -> {
            //Open QR Scanner activity
            scanCode();
        });

        //Payment Logic
        MaterialButton paymentButton = view.findViewById(R.id.payment_button);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = new UserSharedPreference(context).getToken();
                Toast.makeText(context, token, Toast.LENGTH_SHORT).show();
//                pay();
            }
        });

        ApiService.endpoint().getWarung().enqueue(new Callback<WarungModel>() {
            @Override
            public void onResponse(@NonNull Call<WarungModel> call, @NonNull Response<WarungModel> response) {
                if(response.isSuccessful()) {
                    results = response.body().getWarung();
                    String name[] = new String[results.size()];
                    for (int i=0; i<results.size(); i++) {
                        name[i] = results.get(i).getFull_name();
                    }
                    nameAdapter = new ArrayAdapter<String>(context , android.R.layout.simple_list_item_1, name);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) view.findViewById(R.id.namawarung);
                    textView.setAdapter(nameAdapter);
                    textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                textView.showDropDown();
                            }else {
                                textView.dismissDropDown();
                            }

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<WarungModel> call, Throwable t) {
                Log.d("Failure : ", t.toString());
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void pay() {
        Toast.makeText(context, "berhasil kok", Toast.LENGTH_SHORT).show();
        ApiService.endpoint().farmerBuyFromShop(new UserSharedPreference(context).getToken(),24.500, 1, "Coba dari android studio").enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                if(response.isSuccessful()) {
                    String message = response.body().farmerBuyFromShop();

                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Tekan Volume atas untuk menyalakan flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
}