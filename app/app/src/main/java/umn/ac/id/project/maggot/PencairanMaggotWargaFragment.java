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

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

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
import umn.ac.id.project.maggot.retrofit.ApiService;

public class PencairanMaggotWargaFragment extends Fragment {
    ArrayAdapter<PeternakModel.Peternak> DropDownAdapter;
    List<PeternakModel.Peternak> results;
    private Context context;
    private TextView selectedEmailTextView;
    EditText jumlahBayar;
    private String selectedEmail = "";

    public PencairanMaggotWargaFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pencairan_maggot_warga, container, false);
        selectedEmailTextView = view.findViewById(R.id.selected_email);
        jumlahBayar = view.findViewById(R.id.editText2);
        selectedEmailTextView.setVisibility(View.GONE);
        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(Call<PeternakModel> call, Response<PeternakModel> response) {
                if(response.isSuccessful()) {
                    results = response.body().getPeternak();
                    DropDownAdapter = new PeternakSearchDropDownAdapter(context, (ArrayList<PeternakModel.Peternak>) results);
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
            else if(jumlahBayar.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Jumlah Bayar Harus Diisi.", Toast.LENGTH_SHORT).show();
                return;
            }

            createFarmerWithdrawalRequest();
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
                        message = "Permintaan pencairan berhasil, silahkan konfirmasi dari pihak peternak.";
                    }

                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationUserModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}