package com.autokeep.AutoKeep;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserNewOrders extends AppCompatActivity {
    private static final String TAG = "UserNewOrders";

    @BindView(R.id._start_date) TextView startDate;
    @BindView(R.id._end_date) TextView endDate;
    //private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener_Start , mDateSetListener_End;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_new_orders);
        ButterKnife.bind(this);

        //mDisplayDate = (TextView) findViewById(R.id.editText);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        UserNewOrders.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener_Start,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        UserNewOrders.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener_End,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener_Start = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                startDate.setText(date);
            }
        };
        mDateSetListener_End = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                endDate.setText(date);
            }
        };
        final Spinner carType = findViewById(R.id._cartype);
        ArrayAdapter<String> carTypeAdaptor = new ArrayAdapter<String>(UserNewOrders.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cartype));
        carTypeAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carType.setAdapter(carTypeAdaptor);
        carType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Toast.makeText(getBaseContext(),carType.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                } else if (i == 1) {
                    Toast.makeText(getBaseContext(),carType.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }else if (i == 2) {
                    Toast.makeText(getBaseContext(),carType.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final Spinner carSit = findViewById(R.id._carsit);
        ArrayAdapter<String> carSitAdaptor = new ArrayAdapter<String>(UserNewOrders.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.carsit));
        carSitAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carSit.setAdapter(carSitAdaptor);
        carSit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Toast.makeText(getBaseContext(),carSit.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                } else if (i == 1) {
                    Toast.makeText(getBaseContext(),carSit.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }else if (i == 2) {
                    Toast.makeText(getBaseContext(),carSit.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }else if (i == 3) {
                    Toast.makeText(getBaseContext(),carSit.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
