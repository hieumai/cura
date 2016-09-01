package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.DegreeController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.adapter.StringListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class OtherSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static String ACTIVITY_NAME = "Other";
    private Toolbar toolbar;
    private Spinner spnDegree;
    private ArrayList<String> degree;
    private StringListAdapter adapterDegree;
    private int checkedDegree;
    private Button btnSave;
    private DoctorUserEntity entity;
    private ProgressDialog pDialog;
    private EditText minPrice, maxPrice, experienceStart;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);
        entity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        showProgressDialog();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    if (!DegreeController.isDataLoaded()) {
                        DegreeController.initData();
                    }
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception == null) {
                    degree = (ArrayList<String>) DataUtils.getListName(DegreeController.getAllDegree());
                    checkedDegree = Integer.parseInt(entity.getDegree().getId()) - 1;
                    initAdapter();
                    initSpinner();
                    initButton();
                    initToolbar();
                    initEditText();
                } else {
                    ErrorController.showDialog(getApplicationContext(), "Error : " + exception.getMessage());
                }
            }
        };
        task.execute();
    }

    private void initAdapter() {
        adapterDegree = new StringListAdapter(this, R.layout.string_list_item, degree, checkedDegree);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.other_settings_toolbar);
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

    private void initButton() {
        btnSave = (Button) findViewById(R.id.button_settings_save);
        btnSave.setOnClickListener(this);
    }

    private void initEditText() {
        minPrice = (EditText) findViewById(R.id.editText_minPrice);
        maxPrice = (EditText) findViewById(R.id.editText_maxPrice);
        experienceStart = (EditText) findViewById(R.id.editText_settings_experience_start);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - entity.getExperience();
        experienceStart.setText(Integer.toString(year));
        minPrice.setText(Double.toString(entity.getMinPrice()));
        maxPrice.setText(Double.toString(entity.getMaxPrice()));
    }

    private void initSpinner() {
        spnDegree = (Spinner) findViewById(R.id.spnDegree_Settings);
        spnDegree.setAdapter(adapterDegree);
        spnDegree.setSelection(checkedDegree);
        spnDegree.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        checkedDegree = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_settings_save) {
            new AsyncTask<Object, Void, Void>() {
                private Exception exception = null;
                private ProgressDialog pDialog;

                @Override
                protected void onPreExecute() {
                    pDialog = new ProgressDialog(OtherSettingsActivity.this);
                    pDialog.setMessage(getResources().getString(R.string.UpdatingMessage));
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                @Override
                protected Void doInBackground(Object[] params) {
                    try {
                        DoctorUserEntity entityUpdated = getDoctorUserEntity();
                        CurrentUserProfile.getInstance().setData(UserController.updateDoctorBasic(entityUpdated));
                    } catch (Exception e) {
                        exception = e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    pDialog.dismiss();
                    if (exception == null) {
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    } else {
                        ErrorController.showDialog(getApplicationContext(), "Error : " + exception.getMessage());
                    }
                }
            }.execute();
        }
    }

    public DoctorUserEntity getDoctorUserEntity() {
        DoctorUserEntity doctorUserEntity = ((DoctorUserEntity) CurrentUserProfile.getInstance().getEntity()).cloneDoctorBasic();
        doctorUserEntity.setDegree(DegreeController.getAllDegree().get(spnDegree.getSelectedItemPosition()));
        doctorUserEntity.setExperience(Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(experienceStart.getText().toString()));
        doctorUserEntity.setMinPrice(Double.parseDouble(minPrice.getText().toString()));
        doctorUserEntity.setMaxPrice(Double.parseDouble(maxPrice.getText().toString()));
        return doctorUserEntity;
    }
}
