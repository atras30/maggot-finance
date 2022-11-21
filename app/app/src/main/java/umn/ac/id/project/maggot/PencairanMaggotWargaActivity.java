package umn.ac.id.project.maggot;

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
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class PencairanMaggotWargaActivity extends AppCompatActivity {
    ArrayAdapter<PeternakModel.Peternak> DropDownAdapter;
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
                    DropDownAdapter = new PeternakSearchDropDownAdapter(PencairanMaggotWargaActivity.this, (ArrayList<PeternakModel.Peternak>) results);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) findViewById(R.id.namawarga);
                    textView.setAdapter(DropDownAdapter);
                    textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                            Object item = parent.getItemAtPosition(position);
                            if (item instanceof PeternakModel.Peternak){
                                PeternakModel.Peternak peternak =(PeternakModel.Peternak) item;
                                textView.setText(peternak.getFull_name());
                            }
                            ;                        }
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