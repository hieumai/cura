package com.kms.cura.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.WorkingHourEntity;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.adapter.StringListAdapter;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spnFacilities, spnApptLength;
    private Button btnRequestAppt, btnChooseDate;
    private LinearLayout btnChooseTime;
    private TextView txtDate, txtTime, txtDoctorName;
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
    public static String TIME_FRAME = "TIME_FRAME";
    public static String AVAILABLE = "AVAILABLE";
    public static int TIME_FRAME_NUMBER = 48;
    public static int REQUEST_cODE = 200;
    public static int RESULT_OK = 400;
    private String hintText;
    private String data;
    private List<String> time;
    private List<WorkingHourEntity> list;
    private DoctorUserEntity doctorUserEntity;
    private boolean selectDate = false;
    private int dateofWeek = 0;
    private String selectedStartTime;
    private String selectedEndTime;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        data = getIntent().getStringExtra(SearchActivity.DOCTOR_SELECTED);
        doctorUserEntity = JsonToEntityConverter.convertJsonStringToEntity(data, DoctorUserEntity.getDoctorEntityType());
        initView();
        setUpData();
        setUpSpinner();
    }

    public Button initButton(int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(this);
        return button;
    }

    public void initView() {
        txtDoctorName = (TextView) findViewById(R.id.txtDoctorName);
        txtDoctorName.setText(doctorUserEntity.getName());
        btnChooseDate = initButton(R.id.btnChooseDate);
        btnChooseTime = (LinearLayout) findViewById(R.id.btnChoseTime);
        btnRequestAppt = initButton(R.id.btnRequestAppt);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnChooseTime.setOnClickListener(this);
        initTextView();
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
        list = doctorUserEntity.getWorkingTime();
        setUpSpinnerFacility();
    }

    private void setUpSpinnerFacility() {
        doctorFacilityName = new ArrayList<>();
        for (WorkingHourEntity entity : list) {
            doctorFacilityName.add(entity.getFacilityEntity().getName());
        }
        doctorFacilityName.add(hintText);
    }

    private Bundle getDataFromSelected(int position) {
        Bundle bundle = new Bundle();
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        dateofWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dateofWeek == 1) {
            dateofWeek = 6;
        } else {
            dateofWeek -= 2;
        }
        List<OpeningHour> workingHour = list.get(position).getWorkingTime();
        List<OpeningHour> workingHourSelected = new ArrayList<>();
        for (OpeningHour hour : workingHour) {
            if (hour.getDayOfTheWeek().getCode() == dateofWeek) {
                workingHourSelected.add(hour);
            }
        }
        Collections.sort(workingHourSelected, new Comparator<OpeningHour>() {
            @Override
            public int compare(OpeningHour lhs, OpeningHour rhs) {
                if(lhs == null){
                    return 1;
                }
                if(rhs == null){
                    return -1;
                }
                if (lhs.getOpenTime().before(rhs.getOpenTime()) &&
                        lhs.getCloseTime().before(rhs.getCloseTime())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        List<Integer> available = new ArrayList<>();
        ArrayList<String> timeFrame = (ArrayList<String>) getTimeFrame(workingHourSelected, available);
        bundle.putStringArrayList(TIME_FRAME, timeFrame);
        bundle.putIntegerArrayList(AVAILABLE, (ArrayList<Integer>) available);
        return bundle;
    }

    private List<String> getTimeFrame(List<OpeningHour> list, List<Integer> newAvailable) {
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        String[] timeResource = getResources().getStringArray(R.array.Time48);
        int[] available = getAvailable();
        int start = getpositionTime(list.get(0).getOpenTime());
        int end = getpositionTime(list.get(list.size() - 1).getCloseTime());
        String[] timeFrame = getResources().getStringArray(R.array.TimeFrame48);
        ArrayList<String> selectedTimeFrame = new ArrayList<>();
        time = new ArrayList<>();
        for (int i = start; i < end; ++i) {
            selectedTimeFrame.add(timeFrame[i]);
            newAvailable.add(available[i]);
            time.add(timeResource[i]);
        }
        return selectedTimeFrame;
    }

    private int getpositionTime(Time src) {
        int position = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(src);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        position = hour * 2;
        if (min == 30) {
            position += 1;
        } else if (min > 30) {
            position += 2;
        }
        return position;
    }

    private int[] getAvailable() {
        List<AppointmentEntity> doctorApptswithDate = getApptsListbyDate(DataUtils.getAcceptedApptList(doctorUserEntity.getAppointmentList()));
        List<AppointmentEntity> patientApptswithDate = getApptsListbyDate(DataUtils.getAcceptedApptList(((PatientUserEntity) CurrentUserProfile.getInstance().getEntity()).getAppointmentList()));
        int[] available = new int[TIME_FRAME_NUMBER];
        for (int i = 0; i < TIME_FRAME_NUMBER; ++i) {
            available[i] = 1;
        }
        checkAvailable(available, doctorApptswithDate);
        checkAvailable(available, patientApptswithDate);
        return available;
    }

    private void checkAvailable(int[] available, List<AppointmentEntity> list) {
        for (AppointmentEntity entity : list) {
            int start = getpositionTime(entity.getStartTime());
            int end = getpositionTime(entity.getEndTime());
            for (int i = start; i < end; ++i) {
                available[i] = 0;
            }
        }
    }

    private List<AppointmentEntity> getApptsListbyDate(List<AppointmentEntity> list) {
        List<AppointmentEntity> doctorApptswithDate = new ArrayList<>();
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        java.sql.Date selected = new java.sql.Date(calendar.getTime().getTime());
        for (AppointmentEntity entity : list) {
            if (entity.getApptDay().getTime() == selected.getTime()) {
                doctorApptswithDate.add(entity);
            }
        }
        return doctorApptswithDate;
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
        } else {
            requestAppointment();
        }

    }

    private void requestAppointment() {
        String cmt = edtComment.getText().toString();
        if ("".equals(cmt)) {
            cmt = null;
        }
        FacilityEntity selectedFacility = list.get(spnFacilities.getSelectedItemPosition()).getFacilityEntity();
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        Date selectedDate = new Date(calendar.getTime().getTime());
        Time startTime = Time.valueOf(selectedStartTime);
        Time endTime = Time.valueOf(selectedEndTime);
        final PatientUserEntity patient = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
        PatientUserEntity patientUserEntity = new PatientUserEntity(patient.getId(),null,null,null,null,null,null,null,null,null);
        DoctorUserEntity doctor = new DoctorUserEntity(doctorUserEntity.getId(),null,null,null,null,null,null,null,null,null,null);
        final AppointmentEntity entity = new AppointmentEntity(patientUserEntity,
                doctor, selectedFacility, selectedDate, startTime, endTime, AppointmentEntity.PENDING_STT, cmt, null);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        showProgressDialog();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    PatientUserEntity patientUserEntity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
                    patientUserEntity.setAppointmentList(AppointmentController.bookAppointment(entity, patient));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception != null) {
                    ErrorController.showDialog(BookAppointmentActivity.this, "Error : " + exception.getMessage());
                } else {
                    finish();
                }
            }
        };
        task.execute();
    }

    private void chooseDate() {
        setCurrentDate();
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
        Bundle bundle = getDataFromSelected(spnFacilities.getSelectedItemPosition());
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
                Bundle timeBundle = data.getExtras();
                txtTime.setText(timeBundle.getString(ChooseTimeActivity.START_TIME) + "-" + timeBundle.getString(ChooseTimeActivity.END_TIME));
                selectedStartTime = time.get(timeBundle.getInt(ChooseTimeActivity.INDEX_START_TIME));
                selectedEndTime = time.get(timeBundle.getInt(ChooseTimeActivity.INDEX_END_TIME));
            }
        }
    }

    private boolean isDateSelectedInPast(int daySelected, int monthSelected, int yearSelected) {
        Calendar current = new GregorianCalendar(year, month - 1, day);
        Calendar selected = new GregorianCalendar(yearSelected, monthSelected - 1, daySelected);
        return (current.after(selected));
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

}
