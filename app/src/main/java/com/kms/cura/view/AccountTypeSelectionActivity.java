package com.kms.cura.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.kms.cura.R;

public class AccountTypeSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btnSelectPat, btnSelectDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);
        btnSelectDoc = initButton(R.id.btnSelectDoc, this);
        btnSelectPat = initButton(R.id.btnSelectPat, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSelectDoc) {
            // navigate to Doc Register

        } else if (v.getId() == R.id.btnSelectPat) {
            Intent toPatientRegister = new Intent(AccountTypeSelectionActivity.this, PatientSignUpActivity.class);
            startActivity(toPatientRegister);

        }
    }

    private LinearLayout initButton(int resourceId, View.OnClickListener listener) {
        LinearLayout layoutButton = (LinearLayout) findViewById(resourceId);
        layoutButton.setOnClickListener(listener);
        return layoutButton;
    }
}
