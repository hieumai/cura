package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.NotificationController;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.NotificationController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.AnimationExecutor;
import com.kms.cura.view.adapter.DoctorRequestListAdapter;
import com.kms.cura.view.fragment.DoctorApptDayVIewFragment;
import com.kms.cura.view.fragment.DoctorAppointmentMonthViewFragment;
import com.kms.cura.view.fragment.DoctorSettingsFragment;
import com.kms.cura.view.fragment.DoctorProfileFragment;
import com.kms.cura.view.fragment.DoctorRequestListFragment;
import com.kms.cura.view.fragment.MessageListFragment;
import com.kms.cura.view.service.NotificationListener;

import java.util.List;

public class DoctorViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnClickListener, View.OnClickListener, EventHandler {
    public static final String NAVIGATION_KEY = "navi_key";
    private Toolbar doctorToolbar;
    private Fragment doctorHomeFragment, doctorProfileFragment, doctorSettingsFragment, doctorMessageFragment, doctorRequestListFragment, doctorApptDayViewFragment, doctorApptView2;
    public static final String NAVIGATE_TO = "navigate_to";
    public static final String PATIENT_REQUEST = "patient_request";
    public static final String DOCTOR_APPT = "doctor_appt";
    private ProgressDialog pDialog;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private int countAppt;
    private int countMsg;
    private int countRequest;
    private EventBroker broker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        broker = EventBroker.getInstance();
        registerEvent();
        initToolBar();
        initDrawer();
        initNavigationView();
        initFragments();
    }

    private String getNavigationTo() {
        String navigate = getIntent().getStringExtra(NAVIGATE_TO);
        return navigate;
    }

    private void initFragments() {
        doctorProfileFragment = new DoctorProfileFragment();
        doctorSettingsFragment = new DoctorSettingsFragment();
        doctorRequestListFragment = new DoctorRequestListFragment();
        doctorMessageFragment = new MessageListFragment();
        doctorApptDayViewFragment = new DoctorApptDayVIewFragment();
        String navigateTo = getNavigationTo();
        if (navigateTo == null) {
            changeFragment(doctorProfileFragment);
            return;
        }
        switch (navigateTo) {
            case PATIENT_REQUEST:
                changeFragment(doctorRequestListFragment);
                break;
            case MessageListFragment.TO_MESSAGE:
                changeFragment(doctorMessageFragment);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(NotificationListener.NUM_MSG_NOTI);
                editor.commit();
                break;
            case DOCTOR_APPT:
                doctorApptView2 = new DoctorAppointmentMonthViewFragment();
                changeFragment(doctorApptView2);
                break;
        }
    }

    private void changeFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Fragment_UserView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.doctor_navigation_drawer_drawer);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        setApptNotiForDoctor();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);
        showProgressDialog();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            private List<NotificationEntity> msgNotifs;

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    msgNotifs = NotificationController.getMsgNotification(CurrentUserProfile.getInstance().getEntity());
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception != null) {
                    ErrorController.showDialog(DoctorViewActivity.this, "Error : " + exception.getMessage());
                } else {
                    countMsg = msgNotifs.size();
                    if (countMsg == 0) {
                        return;
                    }
                    changeToggle();
                    setMenuCounter(R.id.nav_messages, msgNotifs.size());
                }

            }
        };
        task.execute();
    }


    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
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
        unregisterServer();
    }

    private void unregisterServer() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);
        showProgressDialog();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    String regName = UserEntity.USER + CurrentUserProfile.getInstance().getEntity().getId();
                    NotificationController.unregisterGCM(regName);
                } catch (Exception e) {
                    //TODO : log for app
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                UserController.unregisterGCMLocal(DoctorViewActivity.this);
                CurrentUserProfile.getInstance().setData(null);
                Intent i = new Intent(DoctorViewActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return;
            }
        };
        task.execute();
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

    private void changeToggle() {
        toggle.setDrawerIndicatorEnabled(false);
        doctorToolbar.setNavigationIcon(R.drawable.noti_drawer);
        doctorToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    public void onClick(View v) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    private void setApptNotiForDoctor() {
        List<AppointmentEntity> appts = ((DoctorUserEntity) CurrentUserProfile.getInstance().getEntity()).getAppointmentList();
        for (AppointmentEntity entity : appts) {
            int status = entity.getStatus();
            if (status == AppointmentEntity.INCOMPLETED_STT) {
                ++countAppt;
            }
            if (status == AppointmentEntity.PENDING_STT) {
                ++countRequest;
            }
        }
        if (countRequest !=0 || countAppt !=0){
            changeToggle();
        }
        setMenuCounter(R.id.nav_request, countRequest);
        setMenuCounter(R.id.nav_appointment, countAppt);
    }

    private void setMenuCounter(@IdRes int itemId, int count) {
        View view = navigationView.getMenu().findItem(itemId).getActionView();
        TextView txtCounter = (TextView) view.findViewById(R.id.txtCounter);
        txtCounter.setVisibility(View.VISIBLE);
        if (countAppt == 0 && countMsg == 0 && countRequest == 0) {
            toggle.setDrawerIndicatorEnabled(true);
            initDrawer();
        }
        if (count == 0) {
            txtCounter.setVisibility(View.INVISIBLE);
            return;
        }
        if (count > 99) {
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

    @Override
    public void handleEvent(String event, Object data) {
        switch (event) {
            case EventConstant.UPDATE_MSG_NOTI_NUMBER:
                View view = navigationView.getMenu().findItem(R.id.nav_messages).getActionView();
                countMsg -= (int) data;
                if (countMsg >= 0) {
                    setMenuCounter(R.id.nav_messages, countMsg);
                }
                break;
            case EventConstant.UPDATE_APPT_NOTI_NUMBER:
                countAppt +=calculateIncreasement(data);
                setMenuCounter(R.id.nav_appointment, countAppt);
                break;
            case EventConstant.UPDATE_REQUEST_NOTI_NUMBER:
                countRequest +=calculateIncreasement(data);
                setMenuCounter(R.id.nav_request, countRequest);
                break;
        }
    }


    private int calculateIncreasement(Object data){
        boolean increasement = (boolean) data;
        if (increasement){
            return 1;
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        unregisterEvent();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        registerEvent();
        super.onResume();
    }

    private void registerEvent() {
        if (broker == null){
            broker = EventBroker.getInstance();
        }
        broker.register(this, EventConstant.UPDATE_APPT_NOTI_NUMBER);
        broker.register(this, EventConstant.UPDATE_REQUEST_NOTI_NUMBER);
        broker.register(this, EventConstant.UPDATE_MSG_NOTI_NUMBER);
    }

    private void unregisterEvent() {
        if (broker == null){
            broker = EventBroker.getInstance();
        }
        broker.unRegister(this, EventConstant.UPDATE_APPT_NOTI_NUMBER);
        broker.unRegister(this, EventConstant.UPDATE_REQUEST_NOTI_NUMBER);
        broker.unRegister(this, EventConstant.UPDATE_MSG_NOTI_NUMBER);
    }

}
