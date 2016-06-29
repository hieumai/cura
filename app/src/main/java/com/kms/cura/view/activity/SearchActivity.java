package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.view.UpdateSpinner;
import com.kms.cura.view.adapter.DoctorListViewAdapter;
import com.kms.cura.view.adapter.SpinnerHintAdapter;
import com.kms.cura.view.fragment.PatientHomeFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnItemSelectedListener, View.OnClickListener {
    private static final String ACTIVITY_NAME = "Results";
    private UpdateSpinner updateSpinner;
    private int checkedSort;
    private ArrayAdapter<CharSequence> adapter;
    private ListView lv;
    private Spinner spinner;
    private Toolbar toolbar;
    private static String data;
    private ProgressDialog pDialog;
    private List<DoctorUserEntity> doctors;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        intent = getIntent();
        data = intent.getStringExtra(PatientHomeFragment.SEARCH_RESULT);
        initToolbar();
        initArray();
        lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(new DoctorListViewAdapter(this, doctors));

        initAdapter();
        initSpinner();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ACTIVITY_NAME);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    private List<DoctorUserEntity> initArray() {
        Gson gson = new Gson();
        doctors = gson.fromJson( data, DoctorUserEntity.getDoctorEntityListType());
        Collections.sort(doctors, new NameComparator());
        return doctors;
    }

    private void initAdapter() {
        adapter = ArrayAdapter.createFromResource(this, R.array.sort_by, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(
                new SpinnerHintAdapter(
                        adapter,
                        R.layout.sort_spinner_row,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        if (position == 1) {
            Collections.sort(doctors, new NameComparator());
        } else if (position == 2) {
            Collections.sort(doctors, new RatingComparator());
        } else if (position == 3) {
            Collections.sort(doctors, new PriceComparator());
        }
        lv.setAdapter(new DoctorListViewAdapter(this, doctors));
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    static class NameComparator implements Comparator<DoctorUserEntity> {
        public int compare(DoctorUserEntity u1, DoctorUserEntity u2) {
            String a1 = u1.getName();
            String a2 = u2.getName();
            return a1.compareTo(a2);
        }
    }

    static class RatingComparator implements Comparator<DoctorUserEntity> {
        public int compare(DoctorUserEntity u1, DoctorUserEntity u2) {
            Double a1 = u1.getRating();
            Double a2 = u2.getRating();
            return a2.compareTo(a1);
        }
    }

    static class PriceComparator implements Comparator<DoctorUserEntity> {
        public int compare(DoctorUserEntity u1, DoctorUserEntity u2) {
            Double a1 = u1.getMinPrice();
            Double a2 = u2.getMinPrice();
            return a1.compareTo(a2);
        }
    }

}

