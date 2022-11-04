package umn.ac.id.project.maggot;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.adapter.DropDownAdapter;
import umn.ac.id.project.maggot.adapter.ListPeternakAdapter;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class BuyMaggotActivity extends AppCompatActivity {
    TextView dateTimeDisplay;
    private Calendar calendar;
    EditText searchPeternak;
    private SimpleDateFormat dateFormat;
    private String date;
    List<PeternakModel.Peternak> results;
    ListView listView;
    String selectedFromList;

    ArrayAdapter<String> nameAdapter;
    DropDownAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_maggot);
//        Button qrCodeScannerButton = findViewById(R.id.scan_qr_code_button);
//
//        qrCodeScannerButton.setOnClickListener(v -> {
//            scanCode();
//        });

        ApiService.endpoint().getPeternak().enqueue(new Callback<PeternakModel>() {
            @Override
            public void onResponse(Call<PeternakModel> call, Response<PeternakModel> response) {
                if(response.isSuccessful()) {
                    results = response.body().getPeternak();
                    String name[] = new String[results.size()];
                    for (int i=0; i<results.size(); i++) {
                        name[i] = results.get(i).getFull_name();
                    }
                    nameAdapter = new ArrayAdapter<String>(BuyMaggotActivity.this, android.R.layout.simple_list_item_1, name);
                    umn.ac.id.project.maggot.InstantAutoComplete textView = (umn.ac.id.project.maggot.InstantAutoComplete) findViewById(R.id.inputpeternak);
                    textView.setAdapter(nameAdapter);

                    textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus)
                                textView.showDropDown();

                        }
                    });

                    textView.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            textView.showDropDown();
                            return false;
                        }
                    });

//                    searchPeternak = findViewById((R.id.searchpeternak));
//                    searchPeternak.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                        @Override
//                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                                performSearch();
//                                return true;
//                            }
//                            return false;
//                        }
//                    });

//                    Spinner spinner = (Spinner) findViewById(R.id.listPeternakSpinner);
//                    // Create an ArrayAdapter using the string array and a default spinner layout
//                    adapter = new DropDownAdapter(BuyMaggotActivity.this, R.layout.peternak_dropdown, R.id.namaPeternak, results);
//                    // Specify the layout to use when the list of choices appears
//                    // Apply the adapter to the spinner
//                    spinner.setAdapter(adapter);
//                    listView = findViewById(R.id.listview);
//                    listView.setAdapter(nameAdapter);
//                    listView.setVisibility(View.VISIBLE);
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            selectedFromList = (String) (listView.getItemAtPosition(i));
//                            Toast.makeText(BuyMaggotActivity.this, "You clicked" + selectedFromList, Toast.LENGTH_SHORT).show();
//                            listView.setVisibility(View.GONE);
//
//
//                            for(PeternakModel.Peternak farmer : results)
//                            {
//                                if(farmer.getFull_name().equals(selectedFromList))
//                                {
//                                    int spinnerPosition = adapter.getPosition(farmer);
//                                    spinner.setSelection(spinnerPosition);
//                                }
//                            }
//                        }
//                    });




                }
            }

            @Override
            public void onFailure(Call<PeternakModel> call, Throwable t) {
                Log.d("Failure : ", t.toString());
            }
        });

        //INI BUAT NGESET CURRENT VALUE DI TEXTVIEW DATE PADA XML
        dateTimeDisplay = (TextView)findViewById(R.id.currentDate);
//        calendar = Calendar.getInstance();
//        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//        date = dateFormat.format(calendar.getTime());
//        dateTimeDisplay.setText(date);
//        int currentYear = calendar.get(Calendar.YEAR);
//        int currentMonth = (calendar.get(Calendar.MONTH) + 1);
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//        Button selectDate = findViewById(R.id.btnSelectDate);
//
//
//        selectDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(BuyMaggotActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                                dateTimeDisplay.setText(day + "/" + month + "/" + year);
//                            }
//                        }, currentYear, currentMonth, currentDay);
//                datePickerDialog.show();
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                listView.setVisibility(View.VISIBLE);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                nameAdapter.getFilter().filter(newText);
                listView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                listView.setVisibility(View.GONE);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //QR Scanner
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BuyMaggotActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    });

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    //END QR Scanner


}