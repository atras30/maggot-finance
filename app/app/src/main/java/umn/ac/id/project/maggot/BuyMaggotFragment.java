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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.PeternakSearchDropDownAdapter;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.TransactionModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class BuyMaggotFragment extends Fragment {
    private Context context;
    ArrayAdapter<PeternakModel.Peternak> DropDownAdapter;
    List<PeternakModel.Peternak> results;
    String selectedFarmerEmail = "";
    String description = "-";
    View view = null;

    public BuyMaggotFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy_maggot, container, false);
        MaterialButton scanWithQrButton = view.findViewById(R.id.scanqrwarga);
        scanWithQrButton.setOnClickListener(v -> {
            scanWithQr();
        });
        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(Call<PeternakModel> call, Response<PeternakModel> response) {
                if (response.isSuccessful()) {
                    results = response.body().getPeternak();
                    DropDownAdapter = new PeternakSearchDropDownAdapter(context, (ArrayList<PeternakModel.Peternak>) results);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) view.findViewById(R.id.namawarga);
                    textView.setAdapter(DropDownAdapter);
                    textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                            Object item = parent.getItemAtPosition(position);
                            if (item instanceof PeternakModel.Peternak) {
                                PeternakModel.Peternak peternak = (PeternakModel.Peternak) item;
                                textView.setText(peternak.getFull_name());
                                Log.i("Peternak", peternak.toString());
                                selectedFarmerEmail = peternak.getEmail();
                                ((TextView) view.findViewById(R.id.edittext_email_warga)).setText(selectedFarmerEmail);
                            }
                        }
                    });

                    textView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            selectedFarmerEmail = "";
                            ((TextView) view.findViewById(R.id.edittext_email_warga)).setText("");
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
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d("Failure : ", t.toString());
            }
        });

        MaterialButton buttonBuy = view.findViewById(R.id.belisekarang);
        EditText buttons[] = new EditText[] {
                view.findViewById(R.id.jumlahbeli),
                view.findViewById(R.id.hargaperkg)
        };

        for(EditText button : buttons) {
            button.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(buttons[0].getText().toString().isEmpty() || buttons[1].getText().toString().isEmpty()) {
                        return;
                    }
                    TextView totalHarga = view.findViewById(R.id.textView5);
                    DecimalFormatSymbols formatid = new DecimalFormatSymbols();

                    formatid.setMonetaryDecimalSeparator(',');
                    formatid.setGroupingSeparator('.');

                    DecimalFormat df = new DecimalFormat("#,###.00", formatid);

                    totalHarga.setText("Rp " + df.format(Double.parseDouble(buttons[0].getText().toString()) * Double.parseDouble(buttons[1].getText().toString())));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        buttonBuy.setOnClickListener(v -> {
            if(selectedFarmerEmail.isEmpty()) {
                Toast.makeText(context, "Anda belum memilih warga.", Toast.LENGTH_SHORT).show();
                return;
            } else if(buttons[0].getText().toString().isEmpty()) {
                Toast.makeText(context, "Jumlah KG harus diisi.", Toast.LENGTH_SHORT).show();
                return;
            } else if(buttons[1].getText().toString().isEmpty()) {
                Toast.makeText(context, "Harga per KG harus diisi.", Toast.LENGTH_SHORT).show();
                return;
            }
            double weightInKg = Double.parseDouble(buttons[0].getText().toString());
            double amountPerKg = Double.parseDouble(buttons[1].getText().toString());
            Log.i("Jumlah KG : ", buttons[0].getText().toString());
            Log.i("Harga / KG : ", buttons[1].getText().toString());
            Log.i("Farmer Email : ", selectedFarmerEmail);
            Log.i("Description : ", description);

            String token = "Bearer " + new TrashManagerSharedPreference(context).getToken();

            ApiService.endpoint().buyMaggot(token, weightInKg, amountPerKg, selectedFarmerEmail, description).enqueue(new Callback<TransactionModel>() {
                @Override
                public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                    if(response.isSuccessful()) {
                        TransactionModel.BuyMaggotResult result = response.body().buyMaggot();
//                        Log.i("Transaction Result", result.toString());
                        Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            Log.i("Error", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TransactionModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        return view;
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
                        TextView emailWarga = view.findViewById(R.id.edittext_email_warga);
                        namaWarga.setText(user.getFull_name());
                        emailWarga.setText(user.getEmail());
                        selectedFarmerEmail = user.getEmail();
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

    private void scanWithQr() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(" Tekan tombol volume atas untuk menyalakan flash\nTekan tombol volume bawah untuk mematikan flash\n\n");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
}