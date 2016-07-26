package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kms.cura.R;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.view.adapter.DoctorListViewAdapter;
import com.kms.cura.view.adapter.SpinnerHintAdapter;
import com.kms.cura.view.dialog.SearchFilterDialog;
import com.kms.cura.view.dialog.filter.FilterData;
import com.kms.cura.view.fragment.PatientHomeFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnItemSelectedListener, View.OnClickListener {
    public static final String ACTIVITY_NAME = "Results";
    public static String DOCTOR_SELECTED = "DOCTOR_SELECTED";
    private static final int NAME = 1;
    private static final int RAITING = 2;
    private static final int PRICE = 3;
    private int checkedSort;
    private ArrayAdapter<CharSequence> adapter;
    private ListView lv;
    private Spinner spinner;
    private Toolbar toolbar;
    private static String data;
    private ProgressDialog pDialog;
    private TextView textView;
    private List<DoctorUserEntity> doctors, filteredDoctors;
    private Intent intent;
    private Button filter;
    private SearchFilterDialog dialog;
    private LinearLayout[] linearLayouts = new LinearLayout[16];
    private boolean[] buttonState = new boolean[16];
    private DoctorListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        intent = getIntent();
        data = intent.getStringExtra(PatientHomeFragment.SEARCH_RESULT);
        dialog = new SearchFilterDialog(this, null);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                filterSearchResults();
                initSpinner();
            }
        });
        initButton();
        initToolbar();
        initArray();
        lv = (ListView) findViewById(R.id.listView1);
        filteredDoctors = new ArrayList<DoctorUserEntity>(doctors);
        listViewAdapter = new DoctorListViewAdapter(this, filteredDoctors);
        textView = (TextView) findViewById(R.id.empty_list_item);
        lv.setAdapter(listViewAdapter);
        initResultList(filteredDoctors);
        initAdapter();
        initSpinner();
    }

    private void initResultList(final List<DoctorUserEntity> doctorList) {
        if (doctorList.isEmpty()) {
            lv.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);

        } else {
            lv.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toProfileView = new Intent(SearchActivity.this, ViewDoctorProfileActivity.class);
                    toProfileView.putExtra(DOCTOR_SELECTED, EntityToJsonConverter.convertEntityToJson(doctorList.get(position)).toString());
                    startActivity(toProfileView);
                }
            });
        }
    }


    private void initButton() {
        filter = (Button) findViewById(R.id.button_filter);
        filter.setOnClickListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
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
    public void onClick(View v) {
        if (v.getId() == R.id.button_filter) {
            dialog.saveFilterData();
            dialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    private List<DoctorUserEntity> initArray() {
        Gson gson = new Gson();
        doctors = gson.fromJson(data, DoctorUserEntity.getDoctorEntityListType());
        Collections.sort(doctors, new NameComparator());
        filteredDoctors = new ArrayList<DoctorUserEntity>();
        return doctors;
    }

    private void filterSearchResults() {
        filteredDoctors.clear();
        List<FilterData> datas = dialog.getData();
        if (datas.isEmpty()) {
            initResultList(doctors);
            return;
        }
        for (DoctorUserEntity doctor : doctors) {
            for (FilterData data : datas) {
                if (data.isDoctorMatched(doctor)) {
                    filteredDoctors.add(doctor);
                    break;
                }
            }
        }

        initResultList(filteredDoctors);
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
        List<DoctorUserEntity> temp;
        if (filteredDoctors.isEmpty()) {
            temp = doctors;
        } else {
            temp = filteredDoctors;
        }
        if (position == NAME) {
            Collections.sort(temp, new NameComparator());
        } else if (position == RAITING) {
            Collections.sort(temp, new RatingComparator());
        } else if (position == PRICE) {
            Collections.sort(temp, new PriceComparator());
        }
        lv.setAdapter(new DoctorListViewAdapter(this, temp));
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



