package com.kms.cura.view.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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


public class Patient_BasicSettings_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private int day, month, year;
    private Spinner spnSex_settings, spnCountry_settings, spnInsurance_settings;
    private EditText name, city;
    private ImageButton calendar;
    private Button save;
    private TextView tvBirth;
    private PatientUserEntity entity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settings = inflater.inflate(R.layout.fragment_patient_settings_basic, container, false);
        initBirthTextView(settings);
        initEditText(settings);
        initButtons(settings);
        initSpinner(settings);
        return settings;
    }

    private void initButtons(View myFragmentView) {
        calendar = (ImageButton) myFragmentView.findViewById(R.id.button_settings_calendar);
        calendar.setOnClickListener(this);
        save = (Button) myFragmentView.findViewById(R.id.button_settings_save);
        save.setOnClickListener(this);
    }

    private void initBirthTextView(View myFragmentView) {
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
        tvBirth = (TextView) myFragmentView.findViewById(R.id.setting_birthday);
        setDateString(day, month, year);
    }

    private void initSpinner(View myFragmentView) {
        ArrayList<String> sex = new ArrayList<>();
        sex.add(getString(R.string.male));
        sex.add(getString(R.string.female));
        StringSexListAdapter adapter = new StringSexListAdapter(getActivity(), R.layout.string_list_item2, sex);
        spnSex_settings = (Spinner) myFragmentView.findViewById(R.id.spnSex_Settings);
        spnSex_settings.setAdapter(adapter);
        if (entity.getGender() != null && entity.getGender().equals("F")) {
            spnSex_settings.setSelection(1);
        } else {
            spnSex_settings.setSelection(0);
        }

        ArrayList<String> countries = Countries.getAllCountries();
        StringListAdapter countryAdapter = new StringListAdapter(getActivity(), R.layout.string_list_item2, countries, 0);
        spnCountry_settings = (Spinner) myFragmentView.findViewById(R.id.spnCountry_Settings);
        spnCountry_settings.setAdapter(countryAdapter);

        ArrayList<String> insurances = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.Insurance)));
        String currentInsurance = entity.getInsurance();
        int position = 0;
        for (int i = 0; i < insurances.size(); i++) {
            if (currentInsurance.equals(insurances.get(i))) {
                position = i;
            }
        }
        StringListAdapter insuranceAdapter = new StringListAdapter(getActivity(), R.layout.string_list_item2, insurances, 0);
        spnInsurance_settings = (Spinner) myFragmentView.findViewById(R.id.spnInsurance_Settings);
        spnInsurance_settings.setAdapter(insuranceAdapter);
        spnInsurance_settings.setSelection(position);
    }

    private void initEditText(View settings) {
        name = (EditText) settings.findViewById(R.id.editText_settings_name);
        name.setText(entity.getName());

        city = (EditText) settings.findViewById(R.id.editText_settings_city);
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
            DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, myDateListener, year, month - 1, day);
            dateDialog.show();
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
