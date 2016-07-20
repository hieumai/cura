package com.kms.cura.view.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.fragment.HealthTrackerFragment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientHealthDetailActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener, DialogInterface.OnClickListener {

    private static final long MILIS_DAY = 24 * 60 * 60 * 1000;
    private ArrayList<HealthEntity> curHealthEntities, pastHealthEntities;
    private HealthEntity healthEntity;
    public static final String STATE = "state";
    public static final String KEY_POSITION = "position";
    private TextView tvHealthTitle, tvStartDate, tvEndDate, tvTitle, tvComment;
    private boolean edit_mode = false;
    private ImageButton btnStart, btnEnd, btnBack;
    private FrameLayout commentLayout;
    private EditText edtComment;
    private Toolbar toolbar;
    private int startDay, startMonth, startYear, endYear, endMonth, endDay;
    private boolean endDateSet = false, repeatUpdate = false, curToPast = false;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_health_detail);
        initHealthEntities();
        healthEntity = getEntityByPosition(getIntent().getIntExtra(KEY_POSITION, 0), getIntent().getIntExtra(STATE, 0));
        initDate();
        initTextView();
        initEditView();
        setDataOnView(healthEntity);
        setUpToolbar();
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(healthEntity.getStartDate());
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startMonth = calendar.get(Calendar.MONTH) + 1;
        startYear = calendar.get(Calendar.YEAR);
        if (healthEntity.isPastHealth()) {
            calendar.setTime(healthEntity.getEndDate());
            endDay = calendar.get(Calendar.DAY_OF_MONTH);
            endMonth = calendar.get(Calendar.MONTH) + 1;
            endYear = calendar.get(Calendar.YEAR);
            endDateSet = true;
        } else {
            endDay = startDay;
            endMonth = startMonth;
            endYear = startYear;
        }
    }

    private void setDataOnView(HealthEntity healthEntity) {
        tvHealthTitle.setText(healthEntity.getName());
        setDateString(tvStartDate, startDay, startMonth, startYear);
        if (healthEntity.isPastHealth()) {
            setDateString(tvEndDate, endDay, endMonth, endYear);
        } else {
            tvEndDate.setText("");
            endDateSet = false;
        }
    }

    private void initEditView() {
        btnStart = (ImageButton) findViewById(R.id.btnChooseStartDate);
        btnStart.setOnClickListener(this);
        btnEnd = (ImageButton) findViewById(R.id.btnChooseEndDate);
        btnEnd.setOnClickListener(this);
        commentLayout = (FrameLayout) findViewById(R.id.commentLayout);
        setEditVisibility();
    }

    private void initTextView() {
        tvHealthTitle = (TextView) findViewById(R.id.tvDetailTitle);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
    }

    private HealthEntity getEntityByPosition(int position, int state) {
        if (state == HealthTrackerFragment.STATE_CURRENT) {
            return curHealthEntities.get(position);
        }
        if (state == HealthTrackerFragment.STATE_PAST) {
            return pastHealthEntities.get(position);
        }
        return null;
    }

    private void initHealthEntities() {
        List<HealthEntity> entities = ((PatientUserEntity) (CurrentUserProfile.getInstance().getEntity())).getHealthEntities();
        curHealthEntities = new ArrayList<>();
        pastHealthEntities = new ArrayList<>();
        for (int i = 0; i < entities.size(); ++i) {
            if (entities.get(i).isPastHealth()) {
                pastHealthEntities.add(entities.get(i));
            } else {
                curHealthEntities.add(entities.get(i));
            }
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tbHealthDetail);
        toolbar.getMenu().clear();
        tvTitle = (TextView) toolbar.findViewById(R.id.tvHealthTitle);
        btnBack = (ImageButton) toolbar.findViewById(R.id.btnDetailBack);
        if (healthEntity.isCondition()) {
            tvTitle.setText(getString(R.string.condition));
        } else {
            tvTitle.setText(getString(R.string.symptom));
        }
        toolbar.inflateMenu(R.menu.menu_edit);
        toolbar.setOnMenuItemClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void modifyToolbar() {
        toolbar.getMenu().clear();
        if (edit_mode) {
            toolbar.inflateMenu(R.menu.menu_done);
        } else {
            toolbar.inflateMenu(R.menu.menu_edit);
        }
    }

    private void changeMode() {
        edit_mode = !edit_mode;
        setEditVisibility();
        modifyToolbar();
    }

    private void setEditVisibility() {
        if (edit_mode) {
            commentLayout.removeAllViews();
            View view = getLayoutInflater().inflate(R.layout.comment_edit_text, commentLayout, true);
            edtComment = (EditText) view.findViewById(R.id.edtComment);
            edtComment.setText(healthEntity.getComment());
            btnEnd.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
            tvEndDate.setClickable(true);
            tvStartDate.setClickable(true);
        } else {
            commentLayout.removeAllViews();
            View view = getLayoutInflater().inflate(R.layout.comment_text_view, commentLayout, true);
            tvComment = (TextView) view.findViewById(R.id.tvComment);
            tvComment.setText(healthEntity.getComment());
            btnEnd.setVisibility(View.INVISIBLE);
            btnStart.setVisibility(View.INVISIBLE);
            tvEndDate.setClickable(false);
            tvStartDate.setClickable(false);
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.btnEdit) {
            changeMode();
            return true;
        }
        if (item.getItemId() == R.id.btnDone) {
            pressDoneButton();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDetailBack) {
            if (edit_mode) {
                changeMode();
                initDate();
                setDataOnView(healthEntity);
            } else {
                finish();
            }
        } else if (v.getId() == R.id.btnChooseStartDate || v.getId() == R.id.tvStartDate) {
            showStartDatePicker();
        } else if (v.getId() == R.id.btnChooseEndDate || v.getId() == R.id.tvEndDate) {
            showEndDatePicker();
        }
    }

    private void showStartDatePicker() {
        Dialog dateDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT, myStartDateListener, startYear, startMonth - 1, startDay);
        dateDialog.show();
    }

    private void showEndDatePicker() {
        Dialog dateDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT, myEndDateListener, endYear, endMonth - 1, endDay);
        dateDialog.show();
    }

    private DatePickerDialog.OnDateSetListener myStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            if (checkBeforeCurrent(year, month + 1, day)) {
                if (endDateSet) {
                    if (checkStartDate(year, month + 1, day)) {
                        setSpecificDate(tvStartDate, day, month + 1, year);
                        tvStartDate.setVisibility(View.VISIBLE);
                    } else {
                        createDialog(getString(R.string.startDateError2)).show();
                    }
                } else {
                    setSpecificDate(tvStartDate, day, month + 1, year);
                    tvStartDate.setVisibility(View.VISIBLE);
                }
            } else {
                createDialog(getString(R.string.startDateError)).show();
            }
        }
    };

    private DatePickerDialog.OnDateSetListener myEndDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            if (checkEndDate(year, month + 1, day)) {
                if (checkBeforeCurrent(year, month + 1, day)) {
                    endDateSet = true;
                    setSpecificDate(tvEndDate, day, month + 1, year);
                    tvEndDate.setVisibility(View.VISIBLE);
                } else {
                    createDialog(getString(R.string.endDateErrorCurrent)).show();
                }
            } else {
                createDialog(getString(R.string.endDateErrorStart2)).show();
            }
        }
    };

    private boolean checkBeforeCurrent(int year, int month, int day) {
        Calendar date = cloneCalendar(year, month, day - 1);
        return date.before(Calendar.getInstance());
    }

    private boolean checkEndDate(int year, int month, int day) {
        Calendar endDate = cloneCalendar(year, month, day);
        Calendar startDate = cloneCalendar(startYear, startMonth, startDay - 1);
        return endDate.after(startDate);
    }

    private boolean checkStartDate(int year, int month, int day) {
        Calendar endDate = cloneCalendar(endYear, endMonth, endDay);
        Calendar startDate = cloneCalendar(year, month, day - 1);
        return endDate.after(startDate);
    }

    private Calendar cloneCalendar(int year, int month, int day) {
        Calendar date = (Calendar) Calendar.getInstance().clone();
        date.set(year, month - 1, day);
        return date;
    }

    private Date toDateSQL(int year, int month, int day) {
        Calendar calendar = cloneCalendar(year, month, day);
        return new Date(calendar.getTimeInMillis());
    }

    private void setSpecificDate(TextView tv, int day, int month, int year) {
        if (tv.getId() == R.id.tvStartDate) {
            this.startDay = day;
            this.startMonth = month;
            this.startYear = year;
        } else if (tv.getId() == R.id.tvEndDate) {
            this.endDay = day;
            this.endMonth = month;
            this.endYear = year;
        }
        setDateString(tv, day, month, year);
    }

    private void setDateString(TextView tv, int day, int month, int year) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        if (month < 10)
            builder.append("0");
        builder.append(month + "/");
        if (day < 10)
            builder.append('0');
        builder.append(day + "/");
        builder.append(year);
        tv.setText(builder.toString());
    }

    private AlertDialog createDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", this);
        return builder.create();
    }

    private void pressDoneButton() {
        boolean cur = !healthEntity.isPastHealth();
        healthEntity.setComment(edtComment.getText().toString());
        healthEntity.setStartDate(toDateSQL(startYear, startMonth, startDay));
        if (endDateSet) {
            healthEntity.setEndDate(toDateSQL(endYear, endMonth, endDay));
        }
        if (cur && healthEntity.isPastHealth()) {
            curToPast = true;
        }
        setDataOnView(healthEntity);
        if (checkExist(healthEntity)) {
            createDialog(getString(R.string.duplicateHealth)).show();
        } else {
            changeMode();
            updateHealth();
            repeatUpdate = true;
            if (curToPast) {
                curToPast = false;
            }
        }
    }

    private boolean checkExist(HealthEntity healthEntity) {
        int count = 0;
        for (int i = 0; i < pastHealthEntities.size(); ++i) {
            if (healthEntity.getName().equals(pastHealthEntities.get(i).getName()) &&
                    compareDate(healthEntity.getStartDate(), pastHealthEntities.get(i).getStartDate()) &&
                    compareDate(healthEntity.getEndDate(), pastHealthEntities.get(i).getEndDate())) {
                count += 1;
            }
        }

        for (int i = 0; i < curHealthEntities.size(); ++i) {
            if (healthEntity.getName().equals(pastHealthEntities.get(i).getName()) &&
                    healthEntity.getStartDate().equals(pastHealthEntities.get(i).getStartDate())) {
                count += 1;
            }
        }
        if (count == 1 && curToPast) {
            return true;
        }

        if (count == 2) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }

    public void removeOldHealth() {
        if (!repeatUpdate) {
            if (getIntent().getIntExtra(STATE, 0) == HealthTrackerFragment.STATE_CURRENT) {
                curHealthEntities.remove(getIntent().getIntExtra(KEY_POSITION, 0));
            } else {
                pastHealthEntities.remove(getIntent().getIntExtra(KEY_POSITION, 0));
            }
        } else {
            if (healthEntity.isPastHealth()) {
                pastHealthEntities.remove(pastHealthEntities.size() - 1);
            } else {
                curHealthEntities.remove(curHealthEntities.size() - 1);
            }
        }
        if (healthEntity.isPastHealth()) {
            pastHealthEntities.add(healthEntity);
        } else {
            curHealthEntities.add(healthEntity);
        }
    }

    public void updateHealth() {
        removeOldHealth();
        new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            private ArrayList<HealthEntity> healthEntities = new ArrayList<>();

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(PatientHealthDetailActivity.this);
                pDialog.setMessage("Updating...");
                pDialog.setCancelable(false);
                pDialog.show();
                healthEntities.addAll(curHealthEntities);
                healthEntities.addAll(pastHealthEntities);
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    PatientUserEntity entity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
                    entity.setHealthEntities(healthEntities);
                    CurrentUserProfile.getInstance().setData(UserController.updatePatientHealth(entity));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pDialog.dismiss();
                if (exception != null) {
                    ErrorController.showDialog(PatientHealthDetailActivity.this, "Error : " + exception.getMessage());
                } else {
                    Toast.makeText(PatientHealthDetailActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private boolean compareDate(Date start, Date end) {
        if (start.toString().equals(end.toString())) {
            return true;
        }
        return false;
    }
}
