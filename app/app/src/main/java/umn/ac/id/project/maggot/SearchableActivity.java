package umn.ac.id.project.maggot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.DropDownAdapter;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class SearchableActivity extends AppCompatActivity {


    public static final boolean JARGON = false;
    List<PeternakModel.Peternak> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Bundle appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
        Log.e("hehe", "haha");
        if (appData != null) {
            boolean jargon = appData.getBoolean(String.valueOf(SearchableActivity.JARGON));
        }


        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(Call<PeternakModel> call, Response<PeternakModel> response) {
                if(response.isSuccessful()) {
                    results = response.body().getPeternak();
                }
            }

            @Override
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d("hasil", t.toString());
            }
        });

        // Get the intent, verify the action and get the query
//        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            doMySearch(query);
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }
}