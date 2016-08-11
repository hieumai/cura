package com.kms.cura.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.UserController;
import com.kms.cura.event.EventBroker;
import com.kms.cura.view.MultipleChoiceBackPress;
import com.kms.cura.view.fragment.DoctorAppointmentMonthViewFragment;
import com.kms.cura.view.fragment.DoctorSettingsFragment;
import com.kms.cura.view.fragment.DoctorProfileFragment;
import com.kms.cura.view.fragment.DoctorRequestListFragment;
import com.kms.cura.view.fragment.MessageListFragment;

public class DoctorViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnClickListener, View.OnClickListener {
    public static final String NAVIGATION_KEY = "navi_key";
    private Toolbar doctorToolbar;
    private Fragment doctorHomeFragment, doctorProfileFragment, doctorSettingsFragment, doctorHealthTrachkerFragment, doctorRequestListFragment, doctorApptView2, doctorMessageFragment;

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
        doctorProfileFragment = new DoctorProfileFragment();
        doctorSettingsFragment = new DoctorSettingsFragment();
        doctorRequestListFragment = new DoctorRequestListFragment();
        doctorMessageFragment = new MessageListFragment();
    }

    private void changeFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Fragment_UserView, newFragment);
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
                this, drawer, doctorToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void initToolBar() {
//        patientToolbar = (Toolbar) findViewById(R.id.patientToolbar);
        doctorToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(patientToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        View content_toolbar = getLayoutInflater().inflate(R.layout.content_toolbar_patient_profile_view, null);
//        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Gravity.CENTER_HORIZONTAL);
//        patientToolbar.addView(content_toolbar, layoutParams);
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

        if (id == R.id.nav_profile) {
            changeFragment(doctorProfileFragment);
        } else if (id == R.id.nav_appointment) {
            doctorApptView2 = new DoctorAppointmentMonthViewFragment();
            changeFragment(doctorApptView2);
        } else if (id == R.id.nav_request) {
            changeFragment(doctorRequestListFragment);
        } else if (id == R.id.nav_messages) {
            changeFragment(doctorMessageFragment);
        } else if (id == R.id.nav_settings) {
            changeFragment(doctorSettingsFragment);
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

    @Override
    public void onClick(View v) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    private void navigate(String key) {
        if (key != null) {
            switch (key) {
                case MessageThreadActivity.TO_MESSAGE:
                    changeFragment(doctorMessageFragment);
                    break;
            }
        } else {
            changeFragment(doctorProfileFragment);
        }
    }
}
