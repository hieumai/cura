package com.kms.cura.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.kms.cura.R;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.WorkingHourEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.adapter.WorkingHourExpandableSettingsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorWorkingHourSettingsActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener, View.OnClickListener {

    private static String ACTIVITY_NAME = "Working Hours";
    private ExpandableListView listWorkingHour;
    private List<WorkingHourEntity> listWH;
    private List<HashMap<String, List<OpeningHour>>> listWeekDay;
    public static final String WEEKDAY_KEY = "weekday";
    private ImageButton btnCancel, btnDone;
    private boolean editted = false;
    public static String EDITTED = "editted";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_working_hour_settings);
        DoctorUserEntity doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
        initButton();
        listWH = doctorUserEntity.cloneWorkingHourEntities();
        listWeekDay = convertWorkingHour();
        setUpListWorkingHour();
    }

    private void initButton() {
        btnCancel = (ImageButton) findViewById(R.id.btnWorkingHourCancel);
        btnDone = (ImageButton) findViewById(R.id.btnWorkingHourDone);
        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void setUpListWorkingHour() {
        listWorkingHour = (ExpandableListView) this.findViewById(R.id.listWorkingTimeSettings);
        WorkingHourExpandableSettingsAdapter adapter = new WorkingHourExpandableSettingsAdapter(this, listWeekDay);
        listWorkingHour.setAdapter(adapter);
        listWorkingHour.expandGroup(0);
        listWorkingHour.setOnChildClickListener(this);
    }

    private List<HashMap<String, List<OpeningHour>>> convertWorkingHour() {
        List<HashMap<String, List<OpeningHour>>> list = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            HashMap<String, List<OpeningHour>> workingtime = new HashMap<>();
            for (WorkingHourEntity workingHourEntity : listWH) {
                List<OpeningHour> workingHourOfi = new ArrayList<>();
                for (int k = 0; k < workingHourEntity.getWorkingTime().size(); ++k) {
                    if (workingHourEntity.getWorkingTime().get(k).getDayOfTheWeek().getCode() == i) {
                        workingHourOfi.add(workingHourEntity.getWorkingTime().get(k));
                    }
                }
                workingtime.put(workingHourEntity.getFacilityEntity().getName(), workingHourOfi);
            }
            list.add(workingtime);
        }
        return list;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(this, SettingWorkingHourActivity.class);
        intent.putExtra(WEEKDAY_KEY, childPosition);
        startActivity(intent);
        return true;
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
                // request
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
}
