package com.kms.cura.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.Countries;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.adapter.StringListAdapter;
import com.kms.cura.view.adapter.StringSexListAdapter;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;


public class PatientBasicSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private static String ACTIVITY_NAME = "Basic";
    private int day, month, year;
    private Spinner spnSex_settings, spnCountry_settings, spnInsurance_settings;
    private EditText name, city;
    private ImageButton calendar;
    private Button save;
    private TextView tvBirth;
    private PatientUserEntity entity;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
        setContentView(R.layout.activity_patient_settings_basic);
        initToolbar();
        initBirthTextView();
        initEditText();
        initButtons();
        initSpinner();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.basic_settings_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ACTIVITY_NAME);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initButtons() {
        calendar = (ImageButton) findViewById(R.id.button_settings_calendar);
        calendar.setOnClickListener(this);
        save = (Button) findViewById(R.id.button_settings_save);
        save.setOnClickListener(this);
    }

    private void initBirthTextView() {
        Date birth = entity.getBirth();
        if (birth != null) {
            day = birth.getDay();
            month = birth.getMonth() + 1;
            year = birth.getYear() + 1900;
        } else {
            day = 0;
            month = 0;
            year = 1900;
        }
        tvBirth = (TextView) findViewById(R.id.setting_birthday);
        setDateString(day, month, year);
    }

    private void initSpinner() {
        ArrayList<String> sex = new ArrayList<>();
        sex.add(getString(R.string.male));
        sex.add(getString(R.string.female));
        StringSexListAdapter adapter = new StringSexListAdapter(this, R.layout.string_list_item2, sex);
        spnSex_settings = (Spinner) findViewById(R.id.spnSex_Settings);
        spnSex_settings.setAdapter(adapter);
        if (entity.getGender() != null && entity.getGender().equals("F")) {
            spnSex_settings.setSelection(1);
        } else {
            spnSex_settings.setSelection(0);
        }

        ArrayList<String> countries = Countries.getAllCountries();
        StringListAdapter countryAdapter = new StringListAdapter(this, R.layout.string_list_item2, countries, 0);
        spnCountry_settings = (Spinner) findViewById(R.id.spnCountry_Settings);
        spnCountry_settings.setAdapter(countryAdapter);

        ArrayList<String> insurances = new ArrayList<String>(Arrays.asList(this.getResources().getStringArray(R.array.Insurance)));
        String currentInsurance = entity.getInsurance();
        int position = 0;
        for (int i = 0; i < insurances.size(); i++) {
            if (currentInsurance.equals(insurances.get(i))) {
                position = i;
            }
        }
        StringListAdapter insuranceAdapter = new StringListAdapter(this, R.layout.string_list_item2, insurances, 0);
        spnInsurance_settings = (Spinner) findViewById(R.id.spnInsurance_Settings);
        spnInsurance_settings.setAdapter(insuranceAdapter);
        spnInsurance_settings.setSelection(position);
    }

    private void initEditText() {
        name = (EditText) findViewById(R.id.editText_settings_name);
        name.setText(entity.getName());

        city = (EditText) findViewById(R.id.editText_settings_city);
        String formatedCity = null;
        try {
            formatedCity = new String(entity.getLocation().getBytes("ISO-8859-1"), "UTF-8");
            city.setText(formatedCity);
        } catch (UnsupportedEncodingException e) {
            city.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_settings_calendar) {
            DatePickerDialog dateDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, myDateListener, year, month - 1, day);
            dateDialog.show();
        } else if (v.getId() == R.id.button_settings_save) {
            Toast.makeText(PatientBasicSettingsActivity.this, "Save", Toast.LENGTH_SHORT).show();
        }

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            setSpecificDate(day, month + 1, year);
        }
    };

    private void setSpecificDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        setDateString(day, month, year);
    }

    public void setDateString(int day, int month, int year) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        if (month < 10) {
            builder.append("0");
        }
        builder.append(month + "/");
        if (day < 10) {
            builder.append('0');
        }
        builder.append(day + "/");
        builder.append(year);
        tvBirth.setText(builder.toString());
    }
}
