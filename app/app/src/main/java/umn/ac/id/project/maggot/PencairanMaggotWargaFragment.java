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
import umn.ac.id.project.maggot.adapter.PeternakSearchDropDownAdapter;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.NotificationUserModel;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class PencairanMaggotWargaFragment extends Fragment {
    ArrayAdapter<PeternakModel.Peternak> DropDownAdapter;
    List<PeternakModel.Peternak> res;
    ArrayList<PeternakModel.Peternak> results = new ArrayList<PeternakModel.Peternak>();
    private Context context;
    private TextView selectedEmailTextView;
    EditText jumlahBayar;
    private String selectedEmail = "";
    View view = null;

    public PencairanMaggotWargaFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pencairan_maggot_warga, container, false);
        selectedEmailTextView = view.findViewById(R.id.selected_email);
        jumlahBayar = view.findViewById(R.id.editText2);
        selectedEmailTextView.setVisibility(View.GONE);
        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(Call<PeternakModel> call, Response<PeternakModel> response) {
                if(response.isSuccessful()) {
                    res = response.body().getPeternak();
                    for(PeternakModel.Peternak i : res) {
                        if(i.getTrash_manager_id() == new TrashManagerSharedPreference(context).getTrashManager().getId()) {
                            results.add(i);
                        }
                    }
                    if(results.size() == 0) {
                        Toast.makeText(context, "Belum ada warga/warung yang terdaftar.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    DropDownAdapter = new PeternakSearchDropDownAdapter(context, results);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) view.findViewById(R.id.namawarga);
                    textView.setAdapter(DropDownAdapter);
                    textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                            Object item = parent.getItemAtPosition(position);
                            if (item instanceof PeternakModel.Peternak){
                                PeternakModel.Peternak peternak =(PeternakModel.Peternak) item;
                                textView.setText(peternak.getFull_name());
                                selectedEmailTextView.setText(peternak.getEmail());
                                selectedEmailTextView.setVisibility(View.VISIBLE);
                                selectedEmail = peternak.getEmail();
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
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d("Failure : ", t.toString());
            }
        });

        Button switchwarung = view.findViewById(R.id.buttonwarung);
        switchwarung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new PencairanDanaWarungFragment(context);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
        });

        MaterialButton bayarSekarangButton = view.findViewById(R.id.materialButton3);
        bayarSekarangButton.setOnClickListener(v -> {
            if(selectedEmail.isEmpty()) {
                Toast.makeText(context, "Silahkan pilih warga terlebih dahulu.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(jumlahBayar.getText().toString().trim().isEmpty() || jumlahBayar.getText().toString().equals("0")) {
                Toast.makeText(context, "Jumlah pencairan harus diisi.", Toast.LENGTH_SHORT).show();
                return;
            }

            createFarmerWithdrawalRequest();
        });

        MaterialButton scanQrCodeButton = view.findViewById(R.id.scan_qr_code_button);
        scanQrCodeButton.setOnClickListener(v -> {
            scanCode();
        });

        return view;
    }

    private void createFarmerWithdrawalRequest() {
        Log.i("Token", "Bearer " + new UserSharedPreference(context).getToken());
        ApiService.endpoint().createFarmerWithdrawalRequest(selectedEmail, Integer.parseInt(jumlahBayar.getText().toString()), "Bearer " + new TrashManagerSharedPreference(context).getToken()).enqueue(new Callback<NotificationUserModel>() {
            @Override
            public void onResponse(Call<NotificationUserModel> call, Response<NotificationUserModel> response) {
                if(response.isSuccessful()) {
                    String message = response.body().createFarmerWithdrawalRequest();
                    if(message.trim().equalsIgnoreCase("Farmer's Withdrawal Notification Successfully Created.")) {
                        message = "Silakan minta konfirmasi dari pihak warga.";
                    }

                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Tekan tombol volume atas untuk menyalakan flash\nTekan tombol volume bawah untuk mematikan flash\n\n");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    //QR Scanner
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            String scannedEmail = result.getContents();
            InstantAutoComplete namaWarga = view.findViewById(R.id.namawarga);
            namaWarga.setText("Fetching Data...");

            ApiService.endpoint().getUserByEmail(scannedEmail).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if(response.isSuccessful()) {
                        UserModel.User user = response.body().getUserByEmail();
                        InstantAutoComplete namaWarga = view.findViewById(R.id.namawarga);

                        if(!user.getRole().equalsIgnoreCase("farmer")) {
                            Toast.makeText(context, "Warga tidak ditemukan", Toast.LENGTH_SHORT).show();
                            namaWarga.setText("");
                            return;
                        }
                        TextView emailwarga = view.findViewById(R.id.selected_email);
                        namaWarga.setText(user.getFull_name());
                        emailwarga.setText(user.getEmail());
                        emailwarga.setVisibility(View.VISIBLE);
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
}