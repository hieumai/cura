package com.kms.cura.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kms.cura.R;
import com.kms.cura.view.fragment.PatientProfileFragment;

public class ViewPatientProfileActivity extends AppCompatActivity {

    public static final String PATIENT_KEY = "patient";
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_profile);
        btnBack = setUpButton();
        loadFragment();
    }

    private void loadFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flProfileView, new PatientProfileFragment());
        transaction.commit();
    }

    private ImageView setUpButton() {
        ImageView button = (ImageView) findViewById(R.id.btnBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return button;
    }
}
