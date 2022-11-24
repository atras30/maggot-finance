package umn.ac.id.project.maggot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.WarungSearchDropDownAdapter;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.TransactionModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class FarmerPaymentFragment extends Fragment {
    private Context context;
    ArrayAdapter<WarungModel.Warung> DropDownAdapter;
    List<WarungModel.Warung> results;
    String selectedEmail = "";
    View layoutView;

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
        layoutView = inflater.inflate(R.layout.fragment_farmer_payment, container, false);

        //QR Code scanner button logic
        MaterialButton scanQrCodeButton = layoutView.findViewById(R.id.scan_qr_code_button);
        scanQrCodeButton.setOnClickListener(v -> {
            //Open QR Scanner activity
            scanCode();
        });

        //Payment Logic
        MaterialButton paymentButton = layoutView.findViewById(R.id.payment_button);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText totalAmount = layoutView.findViewById(R.id.editText2);

                if(selectedEmail.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Anda harus memilih warung terlebih dahulu.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(totalAmount.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Jumlah Pembayaran tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                pay(selectedEmail, Double.parseDouble(totalAmount.getText().toString()));
            }
        });

        ApiService.endpoint().getWarung().enqueue(new Callback<WarungModel>() {
            @Override
            public void onResponse(@NonNull Call<WarungModel> call, @NonNull Response<WarungModel> response) {
                if(response.isSuccessful()) {
                    results = response.body().getWarung();
                    DropDownAdapter = new WarungSearchDropDownAdapter(context, (ArrayList<WarungModel.Warung>) results);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) layoutView.findViewById(R.id.namawarung);
                    textView.setAdapter(DropDownAdapter);

                    textView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            selectedEmail = "";
                            ((TextView) layoutView.findViewById(R.id.edittext_email_warga)).setText("");
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                            Object item = parent.getItemAtPosition(position);
                            if (item instanceof WarungModel.Warung){
                                WarungModel.Warung warung =(WarungModel.Warung) item;
                                textView.setText(warung.getFull_name());
                                selectedEmail = warung.getEmail();
                                ((TextView) layoutView.findViewById(R.id.edittext_email_warga)).setText(selectedEmail);
                            }
                        }
                    });

                    textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {

                                textView.showDropDown();
                            } else {
                                textView.dismissDropDown();

                            }
                        }
                    });
                    textView.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            textView.showDropDown();
                            return false;
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
        return layoutView;
    }

    private void pay(String email, double totalAmount) {
        String token = "Bearer " + new UserSharedPreference(context).getToken();
        ApiService.endpoint().farmerBuyFromShop(token, totalAmount, email, "-").enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                if(response.isSuccessful()) {
                    String message = response.body().farmerBuyFromShop();

                    if(message.equalsIgnoreCase("Transaction created.")) {
                        Toast.makeText(context, "Pembayaran Berhasil!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        TransactionModel.ErrorHandler error = new Gson().fromJson(response.errorBody().string(), TransactionModel.ErrorHandler.class);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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