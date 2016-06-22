package com.kms.cura.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;

import java.io.UnsupportedEncodingException;

public class PatientProfileViewActivity extends AppCompatActivity {
    private Toolbar tbPatientProfileView;
    private TextView txtName, txtGender, txtDOB, txtLocation, txtInsurance, txtHealthConcerns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_view);
        initToolBar();
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

    public void loadData() {
        PatientUserEntity entity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
        txtName = loadText(entity.getName(), R.id.txtName);
        txtGender = loadText(getGender(entity), R.id.txtGender);
        if (entity.getBirth() == null) {
            txtDOB = loadText(null, R.id.txtDOB);
        } else {
            txtDOB = loadText(entity.getBirth().toString(), R.id.txtDOB);
        }
        txtLocation = loadText(entity.getLocation(), R.id.txtLocation);
        txtInsurance = loadText(entity.getInsurance(), R.id.txtInsurance);
        txtHealthConcerns = loadText(entity.getHealthConcern(), R.id.txtHealthConcern);
    }

    public String getGender(PatientUserEntity patientUserEntity) {
        if (patientUserEntity.getGender() == null) {
            return null;
        }
        if (patientUserEntity.getGender().equals(PatientUserEntity.GENDER_MALE)) {
            return getResources().getString(R.string.male);
        }
        return getResources().getString(R.string.female);
    }

    public TextView loadText(String src, int id) {
        TextView textView = (TextView) findViewById(id);
        if (src == null) {
            textView.setHeight(0);
        } else {
            String newString = null;
            try {
                newString = new String(src.getBytes("ISO-8859-1"), "UTF-8");
                textView.setText(newString);
            } catch (UnsupportedEncodingException e) {
                textView.setText(src);
            }
        }
        return textView;
    }
}
