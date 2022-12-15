package umn.ac.id.project.maggot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.ListWargaBinaanAdapter;
import umn.ac.id.project.maggot.adapter.ListWarungBinaanAdapter;
import umn.ac.id.project.maggot.global.GoogleAccount;
import umn.ac.id.project.maggot.global.TrashManagerSharedPreference;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiErrorHandler;
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

        Log.i("Token", "Bearer " + new TrashManagerSharedPreference(context).getToken());
        ApiService.endpoint().updateTrashManagerData("Bearer " + new TrashManagerSharedPreference(context).getToken()).enqueue(new Callback<TrashManagerModel>() {
            @Override
            public void onResponse(Call<TrashManagerModel> call, Response<TrashManagerModel> response) {
                if(response.isSuccessful()) {
                    TrashManagerModel.TrashManagers trashManager = response.body().updateTrashManagerData();
                    new TrashManagerSharedPreference(context).updateTrashManager(trashManager);

                    List<UserModel.User> allUsers = new TrashManagerSharedPreference(context).getTrashManager().getUsers();
                    ArrayList<UserModel.User> daftarWargaBinaan = new ArrayList<>();
                    ArrayList<UserModel.User> daftarWarungBinaan = new ArrayList<>();

                    for (UserModel.User user : allUsers) {
                        if(user.getRole().equalsIgnoreCase("farmer") && user.is_verified() == 1) {
                            daftarWargaBinaan.add(user);
                        } else if(user.getRole().equalsIgnoreCase("shop") && user.is_verified() == 1) {
                            daftarWarungBinaan.add(user);
                        }
                    }

                    getDataPeternak(view, daftarWargaBinaan);
                    getDataWarung(view, daftarWarungBinaan);

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
                            Intent intent2 = new Intent(context, ListWarungActivity.class);
                            startActivity(intent2);
                        }
                    });
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.i("error message", errorMessage);

                        if(ApiErrorHandler.getErrorMessage(errorMessage).equalsIgnoreCase("Unauthenticated.")) {
                            Toast.makeText(context, "Masalah: Silahkan login terlebih dahulu.", Toast.LENGTH_SHORT).show();
                            new TrashManagerSharedPreference(context).logout();
                            context.startActivity(new Intent(context, LoginActivity.class));
                            ((Activity) context).finish();
                            return;
                        }

                        Toast.makeText(context, "Masalah: " + ApiErrorHandler.getErrorMessage(errorMessage), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TrashManagerModel> call, Throwable t) {
                Toast.makeText(context, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_SHORT).show();
            }
        });

        MaterialButton logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            new GoogleAccount(context).signOut();
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

    private void getDataWarung(View view, ArrayList<UserModel.User> daftarWarungBinaan) {
        warungBinaan = view.findViewById(R.id.wargabinaan4);
        warungBinaan.setText(daftarWarungBinaan.size() + " Warung");

        if(daftarWarungBinaan.size() > 3) {
            daftarWarungBinaan.subList(3, daftarWarungBinaan.size()).clear();
        }

        ListWarungBinaanAdapter listwarungbinaanadapter = new ListWarungBinaanAdapter(context, daftarWarungBinaan);
        RecyclerView recyclerView2 = view.findViewById(R.id.listWarungBinaanRecyclerView);
        recyclerView2.setAdapter(listwarungbinaanadapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));
    }


    private void getDataPeternak(View view, ArrayList<UserModel.User> daftarWargaBinaan) {
        wargaBinaan = view.findViewById(R.id.wargabinaan2);
        wargaBinaan.setText(daftarWargaBinaan.size() + " Warga");

        if(daftarWargaBinaan.size() > 3) {
            daftarWargaBinaan.subList(3, daftarWargaBinaan.size()).clear();
        }
        ListWargaBinaanAdapter listwargabinaanadapter = new ListWargaBinaanAdapter(context, daftarWargaBinaan);
        RecyclerView recyclerView1 = view.findViewById(R.id.listWargaBinaanRecyclerView);
        recyclerView1.setAdapter(listwargabinaanadapter);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
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