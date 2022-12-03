package umn.ac.id.project.maggot;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.DetailWargaAdapter;
import umn.ac.id.project.maggot.adapter.ShopTransactionAdapter;
import umn.ac.id.project.maggot.global.UserSharedPreference;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.TransactionModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class ShopTransactionFragment extends Fragment {
    private Context context;
    TextView dateTimeDisplay;
    TextView dateTimeDisplay2;
    private Calendar calendar;
    private Calendar tanggalawal;
    private Calendar tanggalakhir;
    private Calendar currentDate;
    EditText searchPeternak;
    private SimpleDateFormat dateFormat;
    private String date;

    public ShopTransactionFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_transaction, container, false);
        ApiService.endpoint().getTransactions(new UserSharedPreference(context).getUser().getEmail()).enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(@NonNull Call<TransactionModel> call, @NonNull Response<TransactionModel> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    List<TransactionModel.Transaction> results = response.body().getTransactions();

                    try {
                    ShopTransactionAdapter shoptransactionadapter = new ShopTransactionAdapter(context, results);
                    RecyclerView recyclerView = view.findViewById(R.id.listTransaction);
                    recyclerView.setAdapter(shoptransactionadapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));

                } catch (Exception e) {
                        call.cancel();
                    }


                } else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.i("Failed", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                Log.d("Gagal", t.toString());
            }
        });

        //INI BUAT NGESET CURRENT VALUE DI TEXTVIEW DATE PADA XML
        dateTimeDisplay = (TextView) view.findViewById(R.id.tanggalawal);
        calendar = Calendar.getInstance();
        tanggalawal = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        currentDate = Calendar.getInstance();
        currentDate.set(currentYear, currentMonth, currentDay);

        ImageView selectDate1 = view.findViewById(R.id.pilihtanggalawal);
        selectDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tanggalawal.set(year, month, day);
                                if(calendar.after(tanggalawal) || tanggalawal.equals(calendar) || tanggalawal.equals(tanggalakhir))
                                {
                                    dateTimeDisplay.setText(day + "/" + (month + 1) + "/" + year);
                                }else {
                                    Toast.makeText(context,"Tanggal awal tidak boleh melebihi tanggal sekarang",Toast.LENGTH_SHORT).show();

                                }


                            }
                        }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
            }
        });

        tanggalakhir = Calendar.getInstance();
        dateTimeDisplay2 = (TextView) view.findViewById(R.id.tanggalakhir);
        ImageView selectDate2 = view.findViewById(R.id.pilihtanggalakhir);
        selectDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tanggalakhir.set(year, month, day);
                                if(tanggalakhir.after(tanggalawal) || tanggalakhir.equals(tanggalawal))
                                {
                                    dateTimeDisplay2.setText(day + "/" + (month + 1) + "/" + year);


                                }else {
                                    Toast.makeText(context,"Tanggal Akhir harus lebih dari tanggal awal",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
            }
        });
        return view;
    }
}