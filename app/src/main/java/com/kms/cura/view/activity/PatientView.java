package com.kms.cura.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;

import java.io.UnsupportedEncodingException;

public class PatientView extends Activity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar tbPatientProfileView;
    private Fragment Doctor_HomeFragment, Patient_ProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        initToolBar();
        initDrawer();
        initNavigationView();
        initFragments();
    }

    private void initFragments() {
        Patient_ProfileFragment = new Patient_ProfileFragment();

    }

    private void changeFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.Fragment_PatientView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.doctor_navigation_drawer_drawer);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tbPatientProfileView, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void initToolBar() {
//        tbPatientProfileView = (Toolbar) findViewById(R.id.tbPatientProfileView);
        tbPatientProfileView = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(tbPatientProfileView);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        View content_toolbar = getLayoutInflater().inflate(R.layout.content_toolbar_patient_profile_view, null);
//        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Gravity.CENTER_HORIZONTAL);
//        tbPatientProfileView.addView(content_toolbar, layoutParams);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
            changeFragment(Patient_ProfileFragment);
        } else if (id == R.id.nav_appointment) {
            Toast.makeText(this, "appointment", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_health) {
            Toast.makeText(this, "health", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_messages) {
            Toast.makeText(this, "message", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_signOut) {
            Toast.makeText(this, "signOut", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
