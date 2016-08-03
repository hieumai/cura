package com.kms.cura.view.activity;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import com.kms.cura.view.fragment.PatientHomeFragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnItemSelectedListener, View.OnClickListener {
    private static final String ACTIVITY_NAME = "Results";
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
    private List<DoctorUserEntity> doctors;
    private Intent intent;
    private Button filter;
    private Dialog dialog;
    private LinearLayout[] linearLayouts = new LinearLayout[16];
    private boolean[] buttonState = new boolean[16];
    private TextView reset;
    private CheckBox acceptInsurance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        intent = getIntent();
        data = intent.getStringExtra(PatientHomeFragment.SEARCH_RESULT);
        initButton();
        initToolbar();
        initArray();
        lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(new DoctorListViewAdapter(this, doctors));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toProfileView = new Intent(SearchActivity.this, ViewDoctorProfileActivity.class);
                toProfileView.putExtra(DOCTOR_SELECTED, EntityToJsonConverter.convertEntityToJson(doctors.get(position)).toString());
                startActivity(toProfileView);
            }
        });
        initAdapter();
        initSpinner();
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
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_toolbar) {
            onBackPressed();
        } else if (v.getId() == R.id.button_filter) {
            initFilterDialog();
        } else if (v.getId() == R.id.male) {
            buttonState[0] = setFilterState(buttonState[0], linearLayouts[0]);
        } else if (v.getId() == R.id.female) {
            buttonState[1] = setFilterState(buttonState[1], linearLayouts[1]);
        } else if (v.getId() == R.id.sunday) {
            buttonState[2] = setFilterState(buttonState[2], linearLayouts[2]);
        } else if (v.getId() == R.id.monday) {
            buttonState[3] = setFilterState(buttonState[3], linearLayouts[3]);
        } else if (v.getId() == R.id.tuesday) {
            buttonState[4] = setFilterState(buttonState[4], linearLayouts[4]);
        } else if (v.getId() == R.id.wednesday) {
            buttonState[5] = setFilterState(buttonState[5], linearLayouts[5]);
        } else if (v.getId() == R.id.thursday) {
            buttonState[6] = setFilterState(buttonState[6], linearLayouts[6]);
        } else if (v.getId() == R.id.friday) {
            buttonState[7] = setFilterState(buttonState[7], linearLayouts[7]);
        } else if (v.getId() == R.id.saturday) {
            buttonState[8] = setFilterState(buttonState[8], linearLayouts[8]);
        } else if (v.getId() == R.id.beforeEight) {
            buttonState[9] = setFilterState(buttonState[9], linearLayouts[9]);
        } else if (v.getId() == R.id.eightToSix) {
            buttonState[10] = setFilterState(buttonState[10], linearLayouts[10]);
        } else if (v.getId() == R.id.afterSix) {
            buttonState[11] = setFilterState(buttonState[11], linearLayouts[11]);
        } else if (v.getId() == R.id.less_than_100) {
            buttonState[12] = setFilterState(buttonState[12], linearLayouts[12]);
        } else if (v.getId() == R.id.between_100_and_300) {
            buttonState[13] = setFilterState(buttonState[13], linearLayouts[13]);
        } else if (v.getId() == R.id.between_301_and_600) {
            buttonState[14] = setFilterState(buttonState[14], linearLayouts[14]);
        } else if (v.getId() == R.id.more_than_600) {
            buttonState[15] = setFilterState(buttonState[15], linearLayouts[15]);
        } else if (v.getId() == R.id.tvReset) {
            for (int i = 0; i < 16; i++) {
                TextView textView = (TextView) linearLayouts[i].getChildAt(0);
                textView.setTextColor(getResources().getColor(R.color.black));

                buttonState[i] = false;
                linearLayouts[i].setBackgroundColor(getResources().getColor(R.color.transparent));
            }
            acceptInsurance.setChecked(false);
        } else if (v.getId() == R.id.button_apply) {
            dialog.dismiss();
        }
    }

    private boolean setFilterState(boolean boo, LinearLayout layout) {
        TextView textView = (TextView) layout.getChildAt(0);

        if (boo == false) {
            layout.setBackgroundColor(getResources().getColor(R.color.colorAccent3));
            textView.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else {
            layout.setBackgroundColor(getResources().getColor(R.color.transparent));
            textView.setTextColor(getResources().getColor(R.color.black));
            return false;
        }
    }

    private void initFilterDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.fragment_search_filter);
        this.dialog.setCancelable(true);

        reset = (TextView) dialog.findViewById(R.id.tvReset);
        reset.setOnClickListener(this);

        acceptInsurance = (CheckBox) dialog.findViewById(R.id.checkbox_accept_insurance);

        linearLayouts[0] = (LinearLayout) dialog.findViewById(R.id.male);
        linearLayouts[1] = (LinearLayout) dialog.findViewById(R.id.female);
        linearLayouts[2] = (LinearLayout) dialog.findViewById(R.id.sunday);
        linearLayouts[3] = (LinearLayout) dialog.findViewById(R.id.monday);
        linearLayouts[4] = (LinearLayout) dialog.findViewById(R.id.tuesday);
        linearLayouts[5] = (LinearLayout) dialog.findViewById(R.id.wednesday);
        linearLayouts[6] = (LinearLayout) dialog.findViewById(R.id.thursday);
        linearLayouts[7] = (LinearLayout) dialog.findViewById(R.id.friday);
        linearLayouts[8] = (LinearLayout) dialog.findViewById(R.id.saturday);
        linearLayouts[9] = (LinearLayout) dialog.findViewById(R.id.beforeEight);
        linearLayouts[10] = (LinearLayout) dialog.findViewById(R.id.eightToSix);
        linearLayouts[11] = (LinearLayout) dialog.findViewById(R.id.afterSix);
        linearLayouts[12] = (LinearLayout) dialog.findViewById(R.id.less_than_100);
        linearLayouts[13] = (LinearLayout) dialog.findViewById(R.id.between_100_and_300);
        linearLayouts[14] = (LinearLayout) dialog.findViewById(R.id.between_301_and_600);
        linearLayouts[15] = (LinearLayout) dialog.findViewById(R.id.more_than_600);

        for (int i = 0; i < 16; i++) {
            linearLayouts[i].setOnClickListener(this);
        }

        ImageButton cancel = (ImageButton) dialog.findViewById(R.id.button_cancel);
        Button apply = (Button) dialog.findViewById(R.id.button_apply);
        apply.setOnClickListener(this);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
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
        if (position == NAME) {
            Collections.sort(doctors, new NameComparator());
        } else if (position == RAITING) {
            Collections.sort(doctors, new RatingComparator());
        } else if (position == PRICE) {
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

