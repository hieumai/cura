package com.kms.cura.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.WorkingHourEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.ListViewRemoveItemListener;
import com.kms.cura.view.adapter.SettingWorkingHourAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingWorkingHourActivity extends AppCompatActivity implements View.OnClickListener, ListViewRemoveItemListener {

    private List<WorkingHourEntity> listWH;
    private List<HashMap<String, List<OpeningHour>>> listWeekDay;
    private ListView lvWorkingHour;
    private SettingWorkingHourAdapter adapter;
    private int weekDay;
    private FloatingActionButton fbAddWorkingHour;
    private ImageButton btnCancel, btnDone;
    private boolean editted = false;
    public static final String MODE_KEY = "mode";
    public static final String EDIT_KEY = "edit";
    public static final String ADD_KEY = "add";
    public static final String FACILITIES_KEY = "facilities";
    public static final String FACILITY_KEY = "facility";
    public static final String DAYOFWEEK_KEY = "weekday";
    public static final String START_KEY = "start";
    public static final String END_KEY = "end";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_working_hour);
        weekDay = getIntent().getIntExtra(DoctorWorkingHourSettingsActivity.WEEKDAY_KEY, 0);
        modifyToolbar();
        DoctorUserEntity doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
        listWH = doctorUserEntity.cloneWorkingHourEntities();
        listWeekDay = convertWorkingHour();
        setUpListView();
        initButton();
    }

    private void initButton() {
        btnCancel = (ImageButton) findViewById(R.id.btnWorkingHourCancel);
        btnDone = (ImageButton) findViewById(R.id.btnWorkingHourDone);
        fbAddWorkingHour = (FloatingActionButton) findViewById(R.id.fbAddWorkingHour);
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        fbAddWorkingHour.setOnClickListener(this);
    }

    private void setUpListView() {
        lvWorkingHour = (ListView) findViewById(R.id.lvWorkingHours);
        adapter = new SettingWorkingHourAdapter(this, R.layout.setting_workinghour_item, listWeekDay.get(weekDay), this);
        lvWorkingHour.setAdapter(adapter);
    }

    private void modifyToolbar() {
        TextView tvTitle = (TextView) findViewById(R.id.tvWeekDay);
        tvTitle.setText(DayOfTheWeek.getDayOfTheWeek(weekDay).toString());
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
    public void onClick(View v) {
        if (v.getId() == R.id.btnWorkingHourDone) {
            if (editted) {
                showWarningDialog(getString(R.string.change_done), doneListener);
            } else {
                finish();
            }
        } else if (v.getId() == R.id.btnWorkingHourCancel) {
            onBackPressed();
        } else if (v.getId() == R.id.fbAddWorkingHour) {
            Intent intent = new Intent(this, SettingEditWorkingHourActivity.class);
            intent.putExtra(MODE_KEY, ADD_KEY);
            intent.putStringArrayListExtra(FACILITIES_KEY, getWorkingFacility());
            startActivity(intent);
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

    @Override
    public void removeItem(int position) {
    }

    @Override
    public void removeItem(String facility, OpeningHour openingHour) {
        for (WorkingHourEntity entity : listWH) {
            if (entity.getFacilityEntity().getName().equals(facility)) {
                for (OpeningHour hour : entity.getWorkingTime()) {
                    if (hour.equals(openingHour)) {
                        entity.getWorkingTime().remove(hour);
                        break;
                    }
                }
            }
        }
        listWeekDay = convertWorkingHour();
        adapter.setData(listWeekDay.get(weekDay));
        adapter.notifyDataSetChanged();
        // update on big list

    }

    private ArrayList<String> getWorkingFacility() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String name : listWeekDay.get(weekDay).keySet()) {
            arrayList.add(name);
        }
        return arrayList;
    }
}
