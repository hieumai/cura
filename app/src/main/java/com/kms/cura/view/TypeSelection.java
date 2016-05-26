package com.kms.cura.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.kms.cura.R;

public class TypeSelection extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnSelectPat, btnSelectDoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);
        btnSelectDoc = (ImageButton) findViewById(R.id.btnSelectDoc);
        btnSelectPat = (ImageButton) findViewById(R.id.btnSelectPat);
        btnSelectPat.setOnClickListener(this);
        btnSelectDoc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSelectDoc) {
            // navigate to Doc Register

        } else if (v.getId() == R.id.btnSelectPat) {
            // navigate to Pat Register

        }
    }
}
