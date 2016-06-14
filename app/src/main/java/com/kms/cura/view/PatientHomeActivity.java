package com.kms.cura.view;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.kms.cura.R;

public class PatientHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        PatientHomeFragment patientHomeFragment= PatientHomeFragment.newInstance(getApplicationContext(),PatientHomeActivity.this);
        getSupportFragmentManager().beginTransaction().add(R.id.layoutContainer,patientHomeFragment).commit();
    }
}
