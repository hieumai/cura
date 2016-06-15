package com.kms.cura.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.kms.cura.R;

public class DoctorProfileViewActivity extends AppCompatActivity {
    private Toolbar tbDoctorProfileView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_view);
        initToolBar();
    }
    public void initToolBar() {
        tbDoctorProfileView = (Toolbar) findViewById(R.id.tbDoctorProfileView);
        setSupportActionBar(tbDoctorProfileView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View content_toolbar = getLayoutInflater().inflate(R.layout.content_toolbar_doctor_profile_view, null);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Gravity.CENTER_HORIZONTAL);
        tbDoctorProfileView.addView(content_toolbar, layoutParams);
    }
}
