package com.kms.cura.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kms.cura.R;

import java.util.ArrayList;

public class DoctorProfessionalSettingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static String ACTIVITY_NAME = "Professional";
    private Toolbar toolbar;
    private ListView listView;
    private ArrayList<String> settingList;
    private final String SPECIALITY = "Speciality";
    private final String WORKING_HOUR = "Working Hour";
    private final String OTHER = "Others";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_professional_settings);
        settingList = createSettingStringList();
        initToolbar();
        initListView();
    }

    private ArrayList<String> createSettingStringList() {
        ArrayList<String> settingList = new ArrayList<>();
        settingList.add(SPECIALITY);
        settingList.add(WORKING_HOUR);
        settingList.add(OTHER);
        return settingList;
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.lvProfessional);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, settingList);
        listView.setAdapter(adapter);
        listView.setHeaderDividersEnabled(true);
        listView.setFooterDividersEnabled(true);
        listView.setOnItemClickListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.doctorProfessional_toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ACTIVITY_NAME);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (settingList.get(position)) {
            case SPECIALITY:
                startActivity(intentTo(SettingSpecialityActivity.class));
                break;
            case WORKING_HOUR:
                startActivity(intentTo(DoctorWorkingHourSettingsActivity.class));
                break;
            case OTHER:
                startActivity(intentTo(OtherSettingsActivity.class));
                break;
        }
    }

    private Intent intentTo(Class<?> cls) {
        return new Intent(this, cls);
    }
}