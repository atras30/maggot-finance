package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.PeternakSearchDropDownAdapter;
import umn.ac.id.project.maggot.adapter.WarungSearchDropDownAdapter;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class PencairanDanaWarungActivity extends AppCompatActivity {
    ArrayAdapter<WarungModel.Warung> DropDownAdapter;
    List<WarungModel.Warung> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencairan_dana_warung);
        ApiService.endpoint().getWarung().enqueue(new Callback<WarungModel>() {
            @Override
            public void onResponse(@NonNull Call<WarungModel> call, @NonNull Response<WarungModel> response) {
                if(response.isSuccessful()) {
                    results = response.body().getWarung();
                    DropDownAdapter = new WarungSearchDropDownAdapter(PencairanDanaWarungActivity.this, (ArrayList<WarungModel.Warung>) results);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) findViewById(R.id.namawarung);
                    textView.setAdapter(DropDownAdapter);
                    textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                            Object item = parent.getItemAtPosition(position);
                            if (item instanceof WarungModel.Warung){
                                WarungModel.Warung warung =(WarungModel.Warung) item;
                                textView.setText(warung.getFull_name());
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


//



                }
            }

            @Override
            public void onFailure(Call<WarungModel> call, Throwable t) {
                Log.d("Failure : ", t.toString());
            }
        });
        Button switchwarga = findViewById(R.id.buttonwarga);
        switchwarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PencairanDanaWarungActivity.this, PencairanMaggotWargaActivity.class);
                startActivity(intent);
            }
        });
    }
}