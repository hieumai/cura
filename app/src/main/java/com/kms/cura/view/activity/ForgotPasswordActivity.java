package com.kms.cura.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kms.cura.R;
import com.kms.cura.view.fragment.ForgotPasswordStepTwoFragment;
import com.kms.cura.view.fragment.ForgotPassworkStepOneFragment;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Fragment step1 = new ForgotPassworkStepOneFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, step1).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(ForgotPasswordStepTwoFragment.STEP2_TAG) instanceof ForgotPasswordStepTwoFragment) {
            Fragment fragment = new ForgotPassworkStepOneFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment).commit();
        } else {
            finish();
        }
    }
}
