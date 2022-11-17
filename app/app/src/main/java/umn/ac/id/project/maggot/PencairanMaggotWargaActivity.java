package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class PencairanMaggotWargaActivity extends AppCompatActivity {
    ArrayAdapter<String> nameAdapter;
    List<PeternakModel.Peternak> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencairan_maggot_warga);
        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(Call<PeternakModel> call, Response<PeternakModel> response) {
                if(response.isSuccessful()) {
                    results = response.body().getPeternak();
                    String name[] = new String[results.size()];
                    for (int i=0; i<results.size(); i++) {
                        name[i] = results.get(i).getFull_name();
                    }
                    nameAdapter = new ArrayAdapter<String>(PencairanMaggotWargaActivity.this, android.R.layout.simple_list_item_1, name);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) findViewById(R.id.namawarga);
                    textView.setAdapter(nameAdapter);
                    textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                textView.showDropDown();
                            }else {
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
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d("Failure : ", t.toString());
            }
        });
        Button switchwarung = findViewById(R.id.buttonwarung);
        switchwarung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PencairanMaggotWargaActivity.this, PencairanDanaWarungActivity.class);
                startActivity(intent);
            }
        });
    }

}