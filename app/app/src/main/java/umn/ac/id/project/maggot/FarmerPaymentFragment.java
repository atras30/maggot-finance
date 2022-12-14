package umn.ac.id.project.maggot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.WarungSearchDropDownAdapter;
import umn.ac.id.project.maggot.global.Helper;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.NotificationUserModel;
import umn.ac.id.project.maggot.model.TransactionModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class FarmerPaymentFragment extends Fragment {
    private Context context;
    ArrayAdapter<WarungModel.Warung> DropDownAdapter;
    List<WarungModel.Warung> res;
    ArrayList<WarungModel.Warung> results = new ArrayList<WarungModel.Warung>();
    String selectedEmail = "";
    View layoutView;
    UserModel.User user;

    //QR Scanner
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            String scannedEmail = result.getContents();
            InstantAutoComplete namaWarung = layoutView.findViewById(R.id.namawarung);
            namaWarung.setText("Fetching Data...");

            ApiService.endpoint().getUserByEmail(scannedEmail).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if(response.isSuccessful()) {
                        user = response.body().getUserByEmail();

                        InstantAutoComplete namaWarung = layoutView.findViewById(R.id.namawarung);
                        TextView emailWarung = layoutView.findViewById(R.id.edittext_email_warga);
                        namaWarung.setText(user.getFull_name());
                        emailWarung.setText(user.getEmail());
                        selectedEmail = user.getEmail();
                    } else {
                        try {
                            Toast.makeText(context, "Masalah: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            Log.i("Error", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(context, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_SHORT).show();
                    Log.i("Error", t.getMessage());
                }
            });
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

                Log.i("Selected Email", selectedEmail);
                if(selectedEmail.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Anda harus memilih warung terlebih dahulu.", Toast.LENGTH_SHORT).show();
                    return;
                } else if(totalAmount.getText().toString().equalsIgnoreCase("") || totalAmount.getText().toString().equals("0")) {
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
                    res = response.body().getWarung();
                    for(WarungModel.Warung i : res) {
                        if(i.getTrashManagerId() == new UserSharedPreference(context).getUser().getTrash_manager_id()) {
                            results.add(i);
                        }
                    }
                    if(results.size() == 0) {
                        Toast.makeText(context, "Belum ada warga/warung yang terdaftar.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    DropDownAdapter = new WarungSearchDropDownAdapter(context, results);
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
        ApiService.endpoint().farmerBuyFromShop(token, totalAmount, email).enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                if(response.isSuccessful()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    Toast.makeText(context, response.body().farmerBuyFromShop(), Toast.LENGTH_LONG).show();

                    InstantAutoComplete shop_name = layoutView.findViewById(R.id.namawarung);
                    date.setText(formatter.format(new Date()));
                    description.setText("Pembayaran sembako ke" + shop_name.getText().toString());
                    amount.setText("Rp " + Helper.formatRupiah(totalAmount));

                    ImageButton btnSecret = myView.findViewById(R.id.buttonSecret);
                    btnSecret.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(amount.getText().toString().contains("*")) {
                                amount.setText("Rp " + Helper.formatRupiah(totalAmount));
                            } else {
                                amount.setText("**********");
                            }
                        }
                    });

                    myBuild.setView(myView);
                    AlertDialog dialog = myBuild.create();
                    dialog.show();

                    MaterialButton backButton = myView.findViewById(R.id.back_button);
                    backButton.setOnClickListener(v -> {
                        dialog.hide();
                    });
                } else {
                    try {
                        String error = response.errorBody().string();
                        Log.i("Error 10", error);
                        Toast.makeText(context, "Masalah: " + error, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Masalah: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                Toast.makeText(context, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(" Tekan tombol volume atas untuk menyalakan flash\nTekan tombol volume bawah untuk mematikan flash\n\n");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
}