package com.kms.cura.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kms.cura.R;
import com.kms.cura.view.fragment.HealthTrackerFragment;

public class DummyForHealthTracker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_for_health_tracker);
        Fragment healthTracker = new HealthTrackerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, healthTracker).commit();
    }
}
