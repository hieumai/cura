package com.kms.cura.view.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.view.CustomSpinner;
import com.kms.cura.view.adapter.StringListAdapter;
import com.kms.cura.view.adapter.StringSexListAdapter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class SettingEditWorkingHourActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomSpinner spnFacility;
    private ImageButton btnCancel, btnDone, btnEditFacility;
    private OpeningHour openingHour;
    private boolean editMode;
    private boolean editted = false;
    private Time timeStart, timeEnd;
    private TextView tvStart, tvEnd;
    private Calendar calendar = Calendar.getInstance();
    private StringSexListAdapter adapter;
    private ArrayList<String> workingFacility;
    private String checkedFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_edit_working_hour);
        Intent intent = getIntent();
        editMode = intent.getStringExtra(SettingWorkingHourActivity.MODE_KEY).equals(SettingWorkingHourActivity.EDIT_KEY);
        workingFacility = intent.getStringArrayListExtra(SettingWorkingHourActivity.FACILITIES_KEY);
        if (editMode) {
            timeStart = Time.valueOf(intent.getStringExtra(SettingWorkingHourActivity.START_KEY));
            timeEnd = Time.valueOf(intent.getStringExtra(SettingWorkingHourActivity.END_KEY));
            openingHour = new OpeningHour(DayOfTheWeek.getDayOfTheWeek(intent.getIntExtra(SettingWorkingHourActivity.DAYOFWEEK_KEY, 0)), timeStart, timeEnd);
            checkedFacility = intent.getStringExtra(SettingWorkingHourActivity.FACILITY_KEY);
        } else {
            timeStart = new Time(calendar.getTimeInMillis());
            timeEnd = new Time(calendar.getTimeInMillis());
            checkedFacility = workingFacility.get(0);
        }
        initButton();
        initTextView();
        initFacility();
    }

    private void initFacility() {
        spnFacility = (CustomSpinner) findViewById(R.id.spnFacilityEdit);
        adapter = new StringSexListAdapter(this, R.layout.setting_facility_spinner_item, workingFacility);
        spnFacility.setAdapter(adapter);
        for (int i = 0; i < workingFacility.size(); ++i) {
            if (workingFacility.get(i).equals(checkedFacility)) {
                spnFacility.setSelection(i);
            }
        }
    }

    private void initTextView() {
        tvStart = (TextView) findViewById(R.id.tvStartTime);
        tvEnd = (TextView) findViewById(R.id.tvEndTime);
        if (editMode) {
            tvStart.setText(timeStart.toString().substring(0, 5));
            tvEnd.setText(timeEnd.toString().substring(0, 5));
        }
        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);
    }

    private void initButton() {
        btnCancel = (ImageButton) findViewById(R.id.btnWorkingHourCancel);
        btnDone = (ImageButton) findViewById(R.id.btnWorkingHourDone);
        btnEditFacility = (ImageButton) findViewById(R.id.btnEditFacility);
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnEditFacility.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnWorkingHourDone) {
            if (editted) {
                showWarningDialog(getString(R.string.change_done), doneListener);
            } else {
                finish();
            }
        } else if (v.getId() == R.id.btnWorkingHourCancel) {
            onBackPressed();
        } else if (v.getId() == R.id.btnEditFacility) {
            Intent intent = new Intent(this, SettingFacilityEditActivity.class);
            intent.putExtras(getIntent());
            startActivity(intent);
        } else if (v.getId() == R.id.tvStartTime) {
            showStartTimePicker();
        } else if (v.getId() == R.id.tvEndTime) {
            showEndTimePicker();
        }
    }

    private void showWarningDialog(String mes, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warning);
        builder.setMessage(mes);
        builder.setPositiveButton(R.string.yes, listener);
        builder.setNegativeButton(R.string.no, listener);
        builder.create().show();
    }

    private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finish();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.dismiss();
            }
        }
    };

    private DialogInterface.OnClickListener doneListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                // update data
                finish();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.dismiss();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (editted) {
            showWarningDialog(getString(R.string.change_discard), cancelListener);
        } else {
            finish();
        }
    }

    public void showStartTimePicker() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                timeStart.setTime(calendar.getTime().getTime());
                tvStart.setText(timeStart.toString().substring(0, 5));
            }
        }, 0, 0, true);
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();
    }

    public void showEndTimePicker() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                Time tmp = new Time(calendar.getTime().getTime());
                if (tmp.before(timeStart)) {
                    ErrorController.showDialog(SettingEditWorkingHourActivity.this, getString(R.string.time_end_error));
                } else {
                    timeEnd = tmp;
                    tvEnd.setText(timeEnd.toString().substring(0, 5));
                }
            }
        }, 0, 0, true);
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();
    }
}
