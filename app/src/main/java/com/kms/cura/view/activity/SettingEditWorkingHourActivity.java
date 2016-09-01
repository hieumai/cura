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

import com.google.gson.Gson;
import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.view.CustomSpinner;
import com.kms.cura.view.adapter.StringSexListAdapter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class SettingEditWorkingHourActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomSpinner spnFacility;
    private ImageButton btnCancel, btnDone, btnEditFacility;
    private OpeningHour openingHour;
    private boolean editMode;
    private Time timeStart, timeEnd;
    private TextView tvStart, tvEnd;
    private Calendar calendar = Calendar.getInstance();
    private StringSexListAdapter adapter;
    private ArrayList<String> workingFacility;
    private String checkedFacility;
    public static final String ADD_NEW_WORKING_HOUR = "new_wh";
    public static final String EDIT_WORKING_HOUR_ENTITY = "edit_entity";
    public static final String EDIT_WORKING_HOUR_POSITION = "edit_position";
    public static final int UPDATE_FACILITY_CODE = 2;
    private boolean endDateSet = false;
    private static final int LOWER_BOUND = 12;
    private static final int UPPER_BOUND = 47;
    private static final int MID_BOUND = 30;

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
            endDateSet = true;
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
            if (tvEnd.getText().toString().equals(getString(R.string.click_to_add)) || tvStart.getText().toString().equals(getString(R.string.click_to_add))) {
                ErrorController.showDialog(this, getString(R.string.input_required));
            } else {
                showWarningDialog(getString(R.string.change_done), doneListener);
            }
        } else if (v.getId() == R.id.btnWorkingHourCancel) {
            onBackPressed();
        } else if (v.getId() == R.id.btnEditFacility) {
            Intent intent = new Intent(this, SettingFacilityEditActivity.class);
            intent.putExtras(getIntent());
            startActivityForResult(intent, UPDATE_FACILITY_CODE);
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
                Intent intent = getIntent();
                if (editMode) {
                    intent.putExtra(SettingWorkingHourActivity.MODE_KEY, SettingWorkingHourActivity.EDIT_KEY);
                    intent.putExtra(EDIT_WORKING_HOUR_ENTITY, new Gson().toJsonTree(getOpenningHourEntity(), OpeningHour.getOpeningHourType()).toString());
                    intent.putExtra(EDIT_WORKING_HOUR_POSITION, getIntent().getIntExtra(SettingWorkingHourActivity.POSITION_KEY, 0));
                } else {
                    intent.putExtra(SettingWorkingHourActivity.MODE_KEY, SettingWorkingHourActivity.ADD_KEY);
                    intent.putExtra(ADD_NEW_WORKING_HOUR, new Gson().toJsonTree(getOpenningHourEntity(), OpeningHour.getOpeningHourType()).toString());
                }
                intent.putExtra(SettingFacilityEditActivity.FACILITY_LIST, workingFacility);
                intent.putExtra(SettingWorkingHourActivity.FACILITY_KEY, workingFacility.get(spnFacility.getSelectedItemPosition()));
                intent.putExtra(DoctorWorkingHourSettingsActivity.EDITTED_REQUEST_KEY, true);
                setResult(RESULT_OK, intent);
                finish();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.dismiss();
            }
        }
    };

    @Override
    public void onBackPressed() {
        showWarningDialog(getString(R.string.change_discard), cancelListener);
    }

    public void showStartTimePicker() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, roundHour(hourOfDay, minute));
                calendar.set(Calendar.MINUTE, roundMinute(minute));
                Time tmp = new Time(calendar.getTime().getTime());
                if (tmp.after(timeEnd) && endDateSet) {
                    ErrorController.showDialog(SettingEditWorkingHourActivity.this, getString(R.string.time_start_error));
                } else {
                    timeStart = tmp;
                    tvStart.setText(timeStart.toString().substring(0, 5));
                }
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
                calendar.set(Calendar.HOUR_OF_DAY, roundHour(hourOfDay, minute));
                calendar.set(Calendar.MINUTE, roundMinute(minute));
                Time tmp = new Time(calendar.getTime().getTime());
                if (tmp.before(timeStart)) {
                    ErrorController.showDialog(SettingEditWorkingHourActivity.this, getString(R.string.time_end_error));
                } else {
                    timeEnd = tmp;
                    tvEnd.setText(timeEnd.toString().substring(0, 5));
                    endDateSet = true;
                }
            }
        }, 0, 0, true);
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();
    }

    private OpeningHour getOpenningHourEntity() {
        if (editMode) {
            openingHour.setOpenTime(timeStart);
            openingHour.setCloseTime(timeEnd);
        } else {
            openingHour = new OpeningHour(DayOfTheWeek.getDayOfTheWeek(getIntent().getIntExtra(SettingWorkingHourActivity.DAYOFWEEK_KEY, 0)), timeStart, timeEnd);
        }
        return openingHour;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_FACILITY_CODE && data != null) {
            ArrayList<String> facilities = data.getStringArrayListExtra(SettingFacilityEditActivity.FACILITY_LIST);
            setResult(UPDATE_FACILITY_CODE, data);
            if (editMode) { // remove the facility of the current editting working hour
                if (!checkExist(facilities, checkedFacility)) {
                    finish();
                }
            }
            workingFacility = facilities;
            initFacility();
        }
    }

    private boolean checkExist(ArrayList<String> stringArrayList, String s) {
        for (String elem : stringArrayList) {
            if (elem.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private int roundMinute(int minute) {
        if (minute > LOWER_BOUND && minute <= UPPER_BOUND) {
            return MID_BOUND;
        }
        return 0;
    }

    private int roundHour(int hour, int minute) {
        if (minute > UPPER_BOUND) {
            return hour + 1;
        }
        return hour;
    }
}
