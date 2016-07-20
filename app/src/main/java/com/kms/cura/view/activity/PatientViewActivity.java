package com.kms.cura.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.view.fragment.HealthTrackerFragment;
import com.kms.cura.view.fragment.PatientAppointmentListFragment;
import com.kms.cura.view.fragment.PatientHomeFragment;
import com.kms.cura.view.fragment.PatientProfileFragment;
import com.kms.cura.view.fragment.PatientSettingsFragment;

public class PatientViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnClickListener {
    private Toolbar patientToolbar;
    private Fragment patientHomeFragment, patientProfileFragment, patientSettingsFragment, patientAppointmentFragment;
    private HealthTrackerFragment patientHealthTrachkerFragment;
    static final public String PATIENT = "500";
    public final static String NAVIGATION_KEY = "naviKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        initToolBar();
        initDrawer();
        initNavigationView();
        initFragments();
        navigate(getIntent().getStringExtra(NAVIGATION_KEY));
    }


    private void initFragments() {
        patientHomeFragment = PatientHomeFragment.newInstance(getApplicationContext(), this);
        patientProfileFragment = new PatientProfileFragment();
        patientHealthTrachkerFragment = new HealthTrackerFragment();
        patientAppointmentFragment = new PatientAppointmentListFragment();
        String navigation = getIntent().getStringExtra(NAVIGATION_KEY);
        if (navigation != null && navigation.equals(ConditionSymptomSearchActivity.TO_HEALTH_TRACKER)) {
            changeFragment(patientHealthTrachkerFragment);
        } else {
            changeFragment(patientHomeFragment);
        }
        patientSettingsFragment = new PatientSettingsFragment();
    }

    private void changeFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Fragment_UserView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.patient_navigation_drawer_drawer);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, patientToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void initToolBar() {
        patientToolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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

        if (id == R.id.nav_search) {
            changeFragment(patientHomeFragment);
        } else if (id == R.id.nav_profile) {
            changeFragment(patientProfileFragment);
        } else if (id == R.id.nav_appointment) {
            changeFragment(patientAppointmentFragment);
        } else if (id == R.id.nav_health) {
            changeFragment(patientHealthTrachkerFragment);
        } else if (id == R.id.nav_messages) {
            Toast.makeText(this, "message", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            changeFragment(patientSettingsFragment);
        } else if (id == R.id.nav_signOut) {
            showDialogSignOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        UserController.userSignOut(this);
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void showDialogSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.logOut));
        builder.setMessage(getString(R.string.logOutMes));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.yes), this);
        builder.setNegativeButton(getString(R.string.no), this);
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            signOut();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }

    private void navigate(String key) {
        if (key != null) {
            switch (key) {
                case ConditionSymptomSearchActivity.TO_HEALTH_TRACKER:
                    changeFragment(patientHealthTrachkerFragment);
                    break;
                case PatientSignUpActivity.FROM_PATIENT_REGISTER:
                    changeFragment(patientProfileFragment);
                    break;
            }
        } else {
            changeFragment(patientHomeFragment);
        }
    }

}
