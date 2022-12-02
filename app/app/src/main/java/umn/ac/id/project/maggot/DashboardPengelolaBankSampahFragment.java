package umn.ac.id.project.maggot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.ListWargaBinaanAdapter;
import umn.ac.id.project.maggot.adapter.ListWarungBinaanAdapter;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class DashboardPengelolaBankSampahFragment extends Fragment {
    private Context context;
    Toast toast = null;
    UserSharedPreference userSharedPreference;
    Button detailwarga, detailwarung;
    View view;
    TextView wargaBinaan, warungBinaan;
    public DashboardPengelolaBankSampahFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_pengelola_bank_sampah, container, false);

        ImageView barcodeImage = view.findViewById(R.id.barcode_image);

        MaterialButton logoutButton = view.findViewById(R.id.logout_button);
        getDataPeternak(view);
        getDataWarung(view);
//        populateLastData();
        detailwarga = view.findViewById(R.id.lihatdaftarwarga);
        detailwarung = view.findViewById(R.id.lihatdaftarwarung);
        detailwarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListPeternakActivity.class);
                startActivity(intent);
            }
        });
        detailwarung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListWarungActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(context, gso);

        logoutButton.setOnClickListener(v -> {
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    new TrashManagerSharedPreference(context).logout();
                    showToastMessage("Logout Complete!");
                    navigateToLoginPage();
                    ((Activity)context).finish();
                }
            });
        });

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

    private void getDataWarung(View view) {
        ApiService.endpoint().getWarung().enqueue(new Callback<WarungModel>() {
            @Override
            public void onResponse(@NonNull Call<WarungModel> call, @NonNull Response<WarungModel> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    List<WarungModel.Warung> results = response.body().getWarung();

                    warungBinaan = view.findViewById(R.id.wargabinaan4);
                    warungBinaan.setText(results.size() + " Warung");

                    results.subList(3, results.size()).clear();

                    ListWarungBinaanAdapter listwarungbinaanadapter = new ListWarungBinaanAdapter(context, results);
                    RecyclerView recyclerView2 = view.findViewById(R.id.listWarungBinaanRecyclerView);
                    recyclerView2.setAdapter(listwarungbinaanadapter);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(context));
                }
            }

            @Override
            public void onFailure(Call<WarungModel> call, Throwable t) {
                Log.d("Fail", t.toString());
            }
        });
    }


    private void getDataPeternak(View view) {
        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(@NonNull Call<PeternakModel> call, @NonNull Response<PeternakModel> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    List<PeternakModel.Peternak> results = response.body().getPeternak();

                    wargaBinaan = view.findViewById(R.id.wargabinaan2);
                    wargaBinaan.setText(String.valueOf(results.size()) + " Warga");

                    results.subList(3, results.size()).clear();
                    ListWargaBinaanAdapter listwargabinaanadapter = new ListWargaBinaanAdapter(context, results);
                    RecyclerView recyclerView1 = view.findViewById(R.id.listWargaBinaanRecyclerView);
                    recyclerView1.setAdapter(listwargabinaanadapter);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(context));
                }
            }

            @Override
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d("Fail", t.toString());
            }
        });
    }

    private void populateLastData(View view) {
        TextView wargaBinaan = view.findViewById(R.id.wargabinaan2);
        TextView warungBinaan = view.findViewById(R.id.wargabinaan4);

        TrashManagerSharedPreference trashManagerSharedPreference = new TrashManagerSharedPreference(context);

//        if (trashManagerSharedPreference.getTrashManager() != null) {
//            wargaBinaan.setText(trashManagerSharedPreference.getTrashManager().get());
//            warungBinaan.setText(trashManagerSharedPreference.getTrashManager().getAddress());
//        }
    }
}