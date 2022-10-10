package umn.ac.id.maggotproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BuyMaggotActivity extends AppCompatActivity {
    TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_maggot);

        //INI BUAT NGESET CURRENT VALUE DI TEXTVIEW DATE PADA XML
        dateTimeDisplay = (TextView)findViewById(R.id.currentDate);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = (calendar.get(Calendar.MONTH) + 1);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        Button selectDate = findViewById(R.id.btnSelectDate);


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                DatePickerDialog datePickerDialog = new DatePickerDialog(BuyMaggotActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dateTimeDisplay.setText(day + "/" + month + "/" + year);
                            }
                        }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.listPeternakSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.arr, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
}