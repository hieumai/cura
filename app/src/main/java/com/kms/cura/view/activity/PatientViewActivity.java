package com.kms.cura.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.NotificationController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.fragment.HealthTrackerFragment;
import com.kms.cura.view.fragment.MessageListFragment;
import com.kms.cura.view.fragment.PatientAppointmentListFragment;
import com.kms.cura.view.fragment.PatientHomeFragment;
import com.kms.cura.view.fragment.PatientProfileFragment;
import com.kms.cura.view.fragment.PatientSettingsFragment;

import java.util.List;

public class PatientViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnClickListener, View.OnClickListener {
    private Toolbar patientToolbar;
    private Fragment patientHomeFragment, patientProfileFragment, patientSettingsFragment, patientAppointmentFragment, patientMessageFragment;
    private HealthTrackerFragment patientHealthTrachkerFragment;
    static final public String PATIENT = "500";
    public final static String NAVIGATION_KEY = "naviKey";
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private ProgressDialog pDialog;
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
        patientMessageFragment = new MessageListFragment();
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
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.patient_navigation_drawer_drawer);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);
        showProgressDialog();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            private List<NotificationEntity> msgNotifs;
            private List<NotificationEntity> apptNotifs;
            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    UserEntity userEntity = CurrentUserProfile.getInstance().getEntity();
                    msgNotifs = NotificationController.getMsgNotification(userEntity);
                    apptNotifs = NotificationController.getApptNotification(userEntity);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception != null) {
                    ErrorController.showDialog(PatientViewActivity.this, "Error : " + exception.getMessage());
                } else {
                    if (msgNotifs.size() == 0 && apptNotifs.size() == 0){
                        return;
                    }
                    changeToggle();
                    setMenuCounter(R.id.nav_messages, msgNotifs.size());
                    setUpApptNoti(apptNotifs);
                }

            }
        };
        task.execute();
    }

    private void setUpApptNoti(List<NotificationEntity> notifs){
        int count = 0;
        for (NotificationEntity noti : notifs){
            if (((AppointmentEntity)noti.getRefEntity()).getStatus() != AppointmentEntity.PENDING_STT){
                ++count;
            }
        }
        setMenuCounter(R.id.nav_appointment, count);
    }

    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
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
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment instanceof MessageListFragment) {
                ((MessageListFragment) fragment).onBackPressed();
            }
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
            changeFragment(patientMessageFragment);
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

    @Override
    public void onClick(View v) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }


    private void changeToggle(){
        toggle.setDrawerIndicatorEnabled(false);
        patientToolbar.setNavigationIcon(R.drawable.noti_drawer);
        patientToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void setMenuCounter(@IdRes int itemId, int count) {
        if (count == 0) {
            return;
        }
        View view = navigationView.getMenu().findItem(itemId).getActionView();
        TextView txtCounter = (TextView) view.findViewById(R.id.txtCounter);
        txtCounter.setVisibility(View.VISIBLE);
        if (count > 99){
            txtCounter.setText(R.string.maxNoti);
            return;
        }
        txtCounter.setText(String.valueOf(count));
    }


    private void showProgressDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
