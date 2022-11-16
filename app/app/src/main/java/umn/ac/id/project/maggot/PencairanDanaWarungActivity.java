package umn.ac.id.project.maggot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.WarungModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class PencairanDanaWarungActivity extends AppCompatActivity {
    ArrayAdapter<String> nameAdapter;
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
                    String name[] = new String[results.size()];
                    for (int i=0; i<results.size(); i++) {
                        name[i] = results.get(i).getFull_name();
                    }
                    nameAdapter = new ArrayAdapter<String>(PencairanDanaWarungActivity.this , android.R.layout.simple_list_item_1, name);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) findViewById(R.id.namawarung);
                    textView.setAdapter(nameAdapter);

                    textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                textView.setText("");
                                textView.showDropDown();
                            } else {
                                textView.showDropDown();
                                textView.setText("Masukkan nama warung disini...");

                            }
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
    }
}