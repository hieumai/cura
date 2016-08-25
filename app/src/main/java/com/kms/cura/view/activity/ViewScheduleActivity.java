package com.kms.cura.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.view.fragment.DoctorApptDayVIewFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ViewScheduleActivity extends AppCompatActivity {
    private ImageView btnBack;
    private TextView txtSelectedDay;
    private Bundle bundle;
    public static String FROM_REQUEST = "FROM_REQUEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        bundle = getIntent().getExtras();
        bundle.putInt(FROM_REQUEST, 1);
        setUpButton();
        DoctorApptDayVIewFragment fragment = new DoctorApptDayVIewFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setUpButton() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
