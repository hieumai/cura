package com.kms.cura.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.PatinetProfileUtils;

public class PatientProfileViewActivity extends AppCompatActivity {
    private Toolbar tbPatientProfileView;
    private TextView txtName, txtGender, txtDOB, txtLocation, txtInsurance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_view);
        initToolBar();
        initView();
        loadData();
    }

    public void initToolBar() {
        tbPatientProfileView = (Toolbar) findViewById(R.id.tbPatientProfileView);
        setSupportActionBar(tbPatientProfileView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View content_toolbar = getLayoutInflater().inflate(R.layout.content_toolbar_patient_profile_view, null);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Gravity.CENTER_HORIZONTAL);
        tbPatientProfileView.addView(content_toolbar, layoutParams);
    }

    public void initView() {
        txtName = (TextView) findViewById(R.id.txtName);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtDOB = (TextView) findViewById(R.id.txtDOB);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtInsurance = (TextView) findViewById(R.id.txtInsurance);
    }
    public void loadData(){
        PatientUserEntity entity = PatinetProfileUtils.getInstance().getData();
        txtName.setText(entity.getName());
        txtGender.setText(entity.getGender());
        txtDOB.setText(entity.getBirth().toString());
        txtLocation.setText(entity.getLocation());
        txtInsurance.setText(entity.getInsurance());
    }

}
