package umn.ac.id.project.maggot;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.WarungSearchDropDownAdapter;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.model.NotificationUserModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class PencairanDanaWarungFragment extends Fragment {
    View view = null;
    ArrayAdapter<WarungModel.Warung> DropDownAdapter;
    List<WarungModel.Warung> res;
    ArrayList<WarungModel.Warung> results = new ArrayList<WarungModel.Warung>();
    private Context context;
    private String selectedEmail = "";
    TextView selectedEmailTextView = null;
    EditText jumlahBayar = null;

    public PencairanDanaWarungFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pencairan_dana_warung, container, false);
        selectedEmailTextView = view.findViewById(R.id.selected_email);
        selectedEmailTextView.setVisibility(View.GONE);

        ApiService.endpoint().getWarung().enqueue(new Callback<WarungModel>() {
            @Override
            public void onResponse(@NonNull Call<WarungModel> call, @NonNull Response<WarungModel> response) {
                if(response.isSuccessful()) {
                    res = response.body().getWarung();
                    for(WarungModel.Warung i : res) {
                        if(i.getTrashManagerId() == new TrashManagerSharedPreference(context).getTrashManager().getId()) {
                            results.add(i);
                        }
                    }
                    if(results == null) {
                        Toast.makeText(context, "Belum ada warga/warung yang terdaftar.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    DropDownAdapter = new WarungSearchDropDownAdapter(context, results);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) view.findViewById(R.id.namawarung);
                    textView.setAdapter(DropDownAdapter);
                    textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                            Object item = parent.getItemAtPosition(position);
                            if (item instanceof WarungModel.Warung){
                                WarungModel.Warung warung =(WarungModel.Warung) item;
                                textView.setText(warung.getFull_name());
                                selectedEmail = warung.getEmail();
                                selectedEmailTextView.setText(selectedEmail);
                                selectedEmailTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    textView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            selectedEmail = "";
                            selectedEmailTextView.setText("");
                            selectedEmailTextView.setVisibility(View.GONE);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

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
        Button switchwarga = view.findViewById(R.id.buttonwarga);
        switchwarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new PencairanMaggotWargaFragment(context);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
        });

        jumlahBayar = view.findViewById(R.id.editText2);
        MaterialButton makeRequestButton = view.findViewById(R.id.materialButton4);
        makeRequestButton.setOnClickListener(v -> {
            if(selectedEmail.isEmpty()) {
                Toast.makeText(context, "Silahkan pilih warga terlebih dahulu.", Toast.LENGTH_SHORT).show();
                return;
            } else if(jumlahBayar.getText().toString().trim().isEmpty() || jumlahBayar.getText().toString().equals("0")) {
                Toast.makeText(context, "Jumlah pencairan harus diisi.", Toast.LENGTH_SHORT).show();
                return;
            }

            createShopWithdrawalRequest();
        });

        MaterialButton scanQrCodeButton = view.findViewById(R.id.scan_qr_code_button);
        scanQrCodeButton.setOnClickListener(v -> {
            scanCode();
        });
        return view;
    }
    //QR Scanner
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            String scannedEmail = result.getContents();
            InstantAutoComplete namaWarung = view.findViewById(R.id.namawarung);
            namaWarung.setText("Fetching Data...");

            ApiService.endpoint().getUserByEmail(scannedEmail).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if(response.isSuccessful()) {
                        UserModel.User user = response.body().getUserByEmail();
                        InstantAutoComplete namaWarung = view.findViewById(R.id.namawarung);

                        if(!user.getRole().equalsIgnoreCase("shop")) {
                            Toast.makeText(context, "Warga tidak ditemukan", Toast.LENGTH_SHORT).show();
                            namaWarung.setText("");
                            return;
                        }
                        TextView emailWarung = view.findViewById(R.id.selected_email);
                        namaWarung.setText(user.getFull_name());
                        emailWarung.setText(user.getEmail());
                        emailWarung.setVisibility(View.VISIBLE);
                        selectedEmail = user.getEmail();
                    } else {
                        try {
                            Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            Log.i("Error", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Error", t.getMessage());
                }
            });
        }
    });

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Tekan tombol volume atas untuk menyalakan flash\nTekan tombol volume bawah untuk mematikan flash\n\n");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    private void createShopWithdrawalRequest() {
        ApiService.endpoint().createShopWithdrawalRequest(selectedEmail, Integer.parseInt(jumlahBayar.getText().toString()), "Bearer " + new TrashManagerSharedPreference(context).getToken()).enqueue(new Callback<NotificationUserModel>() {
            @Override
            public void onResponse(Call<NotificationUserModel> call, Response<NotificationUserModel> response) {
                if(response.isSuccessful()) {
                    String message = response.body().createShoprWithdrawalRequest();
                    if(message.trim().equalsIgnoreCase("Shop's Withdrawal Notification Successfully Created.")) {
                        message = "Silakan minta konfirmasi dari pihak warung.";
                    }

                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Toast.makeText(context, "Masalah: " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationUserModel> call, Throwable t) {
                Toast.makeText(context, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}