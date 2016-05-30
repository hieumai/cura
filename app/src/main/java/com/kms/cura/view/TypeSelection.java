package com.kms.cura.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.kms.cura.R;

public class TypeSelection extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btnSelectPat, btnSelectDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);
        btnSelectDoc = (LinearLayout) findViewById(R.id.btnSelectDoc);
        btnSelectDoc.setOnClickListener(this);
        btnSelectPat = (LinearLayout) findViewById(R.id.btnSelectPat);
        btnSelectPat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSelectDoc) {
            // navigate to Doc Register
            Intent intent = new Intent(this, TypeSelection.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.btnSelectPat) {
            // navigate to Pat Register

        }
    }
}
