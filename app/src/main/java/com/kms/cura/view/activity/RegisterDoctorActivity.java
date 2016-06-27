package com.kms.cura.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kms.cura.R;
import com.kms.cura.view.fragment.RegDocPersonalFragment;

public class RegisterDoctorActivity extends AppCompatActivity {

    static final private String STEP_PERSONAL = "11";
    static final private String STEP_PROFESSIONAL = "12";
    static final private String STEP_ACCOUNT = "13";
    static final public String FIRST_NAME = "21";
    static final public String LAST_NAME = "22";
    static final public String SEX = "23";
    static final public String SEX_MALE = "M";
    static final public String SEX_FEMALE = "F";
    static final public String DOB_DAY = "241";
    static final public String DOB_MONTH = "242";
    static final public String DOB_YEAR = "243";
    static final public String PROFESSIONAL = "25";
    static final public String DEGREE = "251";
    static final public String SPECIALITY = "252";
    static final public String FACILITY = "253";
    static final public String PHONE = "261";
    static final public String EMAIL = "262";
    static final public String PWD = "263";


    int curStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);
        Fragment personal = new RegDocPersonalFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.Fragment_DocReg, personal).commit();
    }
}
