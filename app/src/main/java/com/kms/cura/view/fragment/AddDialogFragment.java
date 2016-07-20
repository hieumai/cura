package com.kms.cura.view.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.activity.ConditionInfoActivity;
import com.kms.cura.view.activity.ConditionSymptomSearchActivity;
import com.kms.cura.view.activity.SymptomInfoActivity;

import java.sql.Date;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener, DialogInterface.OnShowListener {

    private static final long MILIS_DAY  = 24*60*60*1000;
    public static final String TITLE_KEY = "title";
    public static final String TYPE_KEY = "type";
    private TextView tvTitle, tvStartDate, tvEndDate;
    private ImageButton btnStart, btnEnd;
    private int startDay, startMonth, startYear, endDay, endMonth, endYear;
    private Calendar calendar;
    private boolean endDateSet = false;
    private EditText edtComment;
    private AlertDialog dialog;
    private int dialogPositive;
    private ProgressDialog pDialog;
    private HealthEntity healthEntity;

    public AddDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View parent = inflater.inflate(R.layout.fragment_add_dialog, null);
        AlertDialog.Builder builder = createDialogBuilder(parent);
        edtComment = (EditText) parent.findViewById(R.id.edtComment);
        setTitle(parent);
        initButton(parent);
        setCurrentStartDate();
        initTextView(parent);
        dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    private AlertDialog.Builder createDialogBuilder(View parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(parent);
        builder.setPositiveButton(getString(R.string.save), null);
        builder.setNegativeButton(getString(R.string.cancel), this);
        return builder;
    }

    private void initTextView(View parent) {
        tvStartDate = (TextView) parent.findViewById(R.id.tvStartDate);
        tvStartDate.setOnClickListener(this);
        setSpecificDate(tvStartDate, startDay, startMonth, startYear);
        tvEndDate = (TextView) parent.findViewById(R.id.tvEndDate);
        tvEndDate.setOnClickListener(this);
    }

    private void initButton(View parent) {
        btnStart = (ImageButton) parent.findViewById(R.id.btnChooseStartDate);
        btnEnd = (ImageButton) parent.findViewById(R.id.btnChooseEndDate);
        btnStart.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
    }

    private void setTitle(View parent) {
        tvTitle = (TextView) parent.findViewById(R.id.tvAddTitle);
        tvTitle.setText(getArguments().getString(TITLE_KEY));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnChooseStartDate || v.getId() == R.id.tvStartDate) {
            showStartDatePicker();
        } else if (v.getId() == R.id.btnChooseEndDate || v.getId() == R.id.tvEndDate) {
            showEndDatePicker();
        } else if (v.getId() == dialogPositive) {
            healthEntity = createHealthEntity(getArguments(), toDateSQL(startYear, startMonth, startDay), toDateSQL(endYear, endMonth, endDay));
            if (checkExist(healthEntity)) {
                createDialog(getString(R.string.duplicateHealth)).show();
            } else {
                addHealthEntityToData();
            }
        }
    }

    private void showStartDatePicker() {
        Dialog dateDialog = new DatePickerDialog(getActivity(), android.app.AlertDialog.THEME_HOLO_LIGHT, myStartDateListener, startYear, startMonth - 1, startDay);
        dateDialog.show();
    }

    private void showEndDatePicker() {
        Dialog dateDialog = new DatePickerDialog(getActivity(), android.app.AlertDialog.THEME_HOLO_LIGHT, myEndDateListener, endYear, endMonth - 1, endDay);
        dateDialog.show();
    }

    private DatePickerDialog.OnDateSetListener myStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            if (checkBeforeCurrent(year, month + 1, day)) {
                setSpecificDate(tvStartDate, day, month + 1, year);
                tvStartDate.setVisibility(View.VISIBLE);
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

    private void setCurrentStartDate() {
        calendar = Calendar.getInstance();
        endDay = startDay = calendar.get(Calendar.DAY_OF_MONTH);
        endMonth = startMonth = calendar.get(Calendar.MONTH) + 1;
        endYear = startYear = calendar.get(Calendar.YEAR);
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

    private boolean checkBeforeCurrent(int year, int month, int day) {
        Calendar date = cloneCalendar(year, month, day - 1);
        return date.before(calendar);
    }

    private boolean checkEndDate(int year, int month, int day) {
        Calendar endDate = cloneCalendar(year, month, day);
        Calendar startDate = cloneCalendar(startYear, startMonth, startDay - 1);
        return endDate.after(startDate);
    }

    private Calendar cloneCalendar(int year, int month, int day) {
        Calendar date = (Calendar) calendar.clone();
        date.set(year, month - 1, day);
        return date;
    }

    private Date toDateSQL(int year, int month, int day) {
        calendar.set(year, month - 1, day);
        return new Date(calendar.getTimeInMillis());
    }

    private HealthEntity createHealthEntity(Bundle bundle, Date startDate, Date endDate) {
        if (bundle.getString(TYPE_KEY) != null && bundle.getString(TYPE_KEY).equals(ConditionInfoActivity.TYPE)) {
            ConditionEntity conditionEntity = new ConditionEntity(bundle.getString(ConditionEntity.ID), bundle.getString(ConditionEntity.NAME), bundle.getString(ConditionEntity.DESCRIPTION));
            if (endDateSet) {
                return new HealthEntity(startDate, endDate, conditionEntity, null, edtComment.getText().toString());
            }
            return new HealthEntity(startDate, null, conditionEntity, null, edtComment.getText().toString());
        }
        if (bundle.getString(TYPE_KEY) != null && bundle.getString(TYPE_KEY).equals(SymptomInfoActivity.TYPE)) {
            SymptomEntity symptomEntity = new SymptomEntity(bundle.getString(SymptomEntity.ID), bundle.getString(SymptomEntity.NAME));
            if (endDateSet) {
                return new HealthEntity(startDate, endDate, null, symptomEntity, edtComment.getText().toString());
            }
            return new HealthEntity(startDate, null, null, symptomEntity, edtComment.getText().toString());
        }
        return null;
    }

    @Override
    public void onShow(DialogInterface d) {
        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        dialogPositive = b.getId();
        b.setOnClickListener(this);
    }

    private AlertDialog createDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNegativeButton("OK", this);
        return builder.create();
    }

    private void addHealthEntityToData() {
        new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            HealthEntity healthEntity;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Updating...");
                pDialog.setCancelable(false);
                pDialog.show();
                healthEntity = getHealthEntity();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    PatientUserEntity entity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
                    entity.addHealthEntity(healthEntity);
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
                    ErrorController.showDialog(getActivity(), "Error : " + exception.getMessage());
                } else {
                    Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        }.execute();
    }

    private boolean checkExist(HealthEntity healthEntity) {
        PatientUserEntity entity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
        for (int i = 0; i < entity.getHealthEntities().size(); ++i) {
            if (healthEntity.getName().equals(entity.getHealthEntities().get(i).getName())) {
                if (!healthEntity.isPastHealth() && !entity.getHealthEntities().get(i).isPastHealth()) {
                    return true;
                } else if (compareDate(healthEntity.getStartDate(), entity.getHealthEntities().get(i).getStartDate()) &&
                        compareDate(healthEntity.getEndDate(), entity.getHealthEntities().get(i).getEndDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    private HealthEntity getHealthEntity() {
        return healthEntity;
    }

    private boolean compareDate(Date start, Date end) {
        if (start == null && end == null) {
            return true;
        }
        if (start == null || end == null) {
            return false;
        }
        if (start.toString().equals(end.toString())) {
            return true;
        }
        return false;
    }
}
