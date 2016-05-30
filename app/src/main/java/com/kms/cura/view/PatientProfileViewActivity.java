package com.kms.cura.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.kms.cura.R;

public class PatientProfileViewActivity extends AppCompatActivity {
    private Toolbar tbPatientProfileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_view);
        initToolBar();
    }

    public void initToolBar() {
        tbPatientProfileView = (Toolbar) findViewById(R.id.tbPatientProfileView);
        setSupportActionBar(tbPatientProfileView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View content_toolbar = getLayoutInflater().inflate(R.layout.content_toolbar_patient_profile_view, null);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Gravity.CENTER_HORIZONTAL);
        tbPatientProfileView.addView(content_toolbar, layoutParams);
    }
}
