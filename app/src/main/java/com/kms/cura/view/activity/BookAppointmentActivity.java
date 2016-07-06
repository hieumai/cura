package com.kms.cura.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.view.adapter.StringListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spnFacilities, spnApptLength;
    private Button btnRequestAppt, btnChooseDate;
    private LinearLayout btnChooseTime;
    private TextView txtDate, txtTime;
    private EditText edtComment;
    private ImageView btnBack;
    private List<String> doctorFacilityName;
    private List<String> apptLength;
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private Calendar calendar;
    private int facilitySelected = -1;
    private int apptLengthSelected = -1;
    public static String DATE_SELECTED = "DATE_SELECTED";
    public static String MONTH_SELECTED = "MONTH_SELECTED";
    public static String YEAR_SELECTED = "YEAR_SELECTED";
    public static String LENGTH_SELECTED = "LENGTH_SELECTED";
    public static int REQUEST_cODE = 200;
    public static int RESULT_OK = 400;
    private String hintText;
    private boolean selectDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        initView();
    }

    public Button initButton(int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(this);
        return button;
    }

    public void initView() {
        btnChooseDate = initButton(R.id.btnChooseDate);
        btnChooseTime = (LinearLayout) findViewById(R.id.btnChoseTime);
        btnRequestAppt = initButton(R.id.btnRequestAppt);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnChooseTime.setOnClickListener(this);
        initTextView();
        setUpData();
        setUpSpinner();
    }

    public void setUpSpinner() {
        initSpinner();
        StringListAdapter adapterFacility = new StringListAdapter(this, R.layout.string_list_item,
                (ArrayList) doctorFacilityName, facilitySelected);
        spnFacilities.setAdapter(adapterFacility);
        spnFacilities.setSelection(adapterFacility.getCount());

        StringListAdapter adapterApptLength = new StringListAdapter(this, R.layout.string_list_item,
                (ArrayList) apptLength, apptLengthSelected);
        spnApptLength.setAdapter(adapterApptLength);
        spnApptLength.setSelection(adapterApptLength.getCount());
    }

    public void initSpinner() {
        spnApptLength = (Spinner) findViewById(R.id.spnAppLength);
        spnFacilities = (Spinner) findViewById(R.id.spnFacility);
    }

    public void setUpData() {
        hintText = getResources().getString(R.string.HintText);
        String[] length = getResources().getStringArray(R.array.ApptLength);
        apptLength = new ArrayList<>();
        Collections.addAll(apptLength, length);
        apptLength.add(hintText);
        doctorFacilityName = new ArrayList<>();
        doctorFacilityName.add("A");
        doctorFacilityName.add("B");
        doctorFacilityName.add("C");
        doctorFacilityName.add(hintText);
    }

    public void initTextView() {
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        edtComment = (EditText) findViewById(R.id.edtComment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnChooseDate) {
            chooseDate();
        } else if (id == R.id.btnChoseTime) {
            chooseTime();
        } else if (id == R.id.btnBack) {
            finish();
        }

    }

    private void chooseDate() {
        if (day == 0 && month == 0 && year == 0) {
            setCurrentDate();
        }
        Dialog dateDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, myDateListener, year, month - 1, day);
        dateDialog.show();
    }

    private void chooseTime() {
        if (spnApptLength.getSelectedItemPosition() != apptLength.size() - 1 &&
                spnFacilities.getSelectedItemPosition() != doctorFacilityName.size() - 1 && selectDate) {
            Intent toChooseTime = new Intent(this, ChooseTimeActivity.class);
            Bundle bundle = createBundle();
            toChooseTime.putExtras(bundle);
            startActivityForResult(toChooseTime, REQUEST_cODE);
        } else {
            Toast.makeText(this, getResources().getString(R.string.BookAptError), Toast.LENGTH_SHORT).show();
        }
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            setSpecificDate(day, month + 1, year);
        }
    };

    private void setCurrentDate() {
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

    }

    private void setSpecificDate(int day, int month, int year) {
        if (isDateSelectedInPast(day, month, year)) {
            ErrorController.showDialog(this, getResources().getString(R.string.BookAptDayError));
            return;
        }
        this.day = day;
        this.month = month;
        this.year = year;
        setDateString(day, month, year);
    }

    public void setDateString(int day, int month, int year) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        if (month < 10)
            builder.append("0");
        builder.append(month + "/");
        if (day < 10)
            builder.append('0');
        builder.append(day + "/");
        builder.append(year);
        txtDate.setText(builder.toString());
        selectDate = true;
    }

    public Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(DATE_SELECTED, day);
        bundle.putInt(MONTH_SELECTED, month);
        bundle.putInt(YEAR_SELECTED, year);
        bundle.putInt(LENGTH_SELECTED, spnApptLength.getSelectedItemPosition() + 1);
        return bundle;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_cODE) {
            if (resultCode == RESULT_OK) {
                Bundle time = data.getExtras();
                txtTime.setText(time.getString(ChooseTimeActivity.START_TIME) + "-" + time.getString(ChooseTimeActivity.END_TIME));
            }
        }
    }

    private boolean isDateSelectedInPast(int daySelected, int monthSelected, int yearSelected) {
        Calendar current = new GregorianCalendar(year, month - 1, day);
        Calendar selected = new GregorianCalendar(yearSelected, monthSelected - 1, daySelected);
        return (current.after(selected));
    }

}
