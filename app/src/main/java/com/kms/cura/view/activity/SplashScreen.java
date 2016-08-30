package com.kms.cura.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.NotificationController;
import com.kms.cura.controller.RegisterIntentService;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.model.Settings;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.GCMUtils;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

public class SplashScreen extends AppCompatActivity implements EventHandler, View.OnClickListener {

    private EventBroker broker;
    private TextView txtRetry;
    DilatingDotsProgressBar progressBar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Settings.REGISTRATION_INCOMPLETE) {
                    ErrorController.showDialog(SplashScreen.this, getString(R.string.notreceiveNoti));
                } else {
                    UserController.saveGCMRegisterKey(SplashScreen.this, intent.getStringExtra(NotificationEntity.REG_ID));
                }
                navigateTo(CurrentUserProfile.getInstance().isPatient());
            }
        };
        registerReceiver();
        broker = EventBroker.getInstance();
        progressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
        txtRetry = (TextView) findViewById(R.id.txtRetry);
        txtRetry.setOnClickListener(this);
        if (UserController.checkSignIn(this)) {
            progressBar.showNow();
            registerEvent();
            UserController.autoSignIn(this);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void navigateTo(boolean patient) {
        Intent toHome = null;
        if (patient) {
            toHome = new Intent(this, PatientViewActivity.class);
        }
        else{
            toHome = new Intent(SplashScreen.this, DoctorViewActivity.class);
        }
        startActivity(toHome);
        unregisterEvent();
        finish();
    }

    @Override
    public void handleEvent(String event, Object data) {
        switch (event) {
            case EventConstant.LOGIN_SUCCESS:
                boolean patient = CurrentUserProfile.getInstance().isPatient();
                if (UserController.alreadyRegisterdGCM(SplashScreen.this) || !GCMUtils.checkPlayServices(this)) {
                    navigateTo(patient);
                    return;
                }
                Intent intent = new Intent(this, RegisterIntentService.class);
                intent.putExtra(RegisterIntentService.CURRENT_USER_ID, CurrentUserProfile.getInstance().getEntity().getId());
                startService(intent);
                break;
            case EventConstant.LOGIN_FAILED:
                ErrorController.showDialog(this, "Login failed :" + data);
                progressBar.hideNow();
                txtRetry.setVisibility(View.VISIBLE);
                break;
            case EventConstant.CONNECTION_ERROR:
                if (data != null) {
                    ErrorController.showDialog(this, "Error " + data);
                } else {
                    ErrorController.showDialog(this, "Error " + getResources().getString(R.string.ConnectionError));
                }
                progressBar.hideNow();
                txtRetry.setVisibility(View.VISIBLE);
                break;
            case EventConstant.INTERNAL_ERROR:
                String internalError = getResources().getString(R.string.InternalError);
                ErrorController.showDialog(this, internalError + " : " + data);
                progressBar.hideNow();
                txtRetry.setVisibility(View.VISIBLE);
        }
    }


    private void registerReceiver() {
        if (isReceiverRegistered){
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Settings.REGISTRATION_COMPLETE);
        intentFilter.addAction(Settings.REGISTRATION_INCOMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, intentFilter);
        isReceiverRegistered = true;
    }


    @Override
    protected void onPause() {
        unregisterEvent();
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerEvent();
        super.onResume();
    }

    public void registerEvent() {
        broker.register(this, EventConstant.LOGIN_SUCCESS);
        broker.register(this, EventConstant.LOGIN_FAILED);
        broker.register(this, EventConstant.CONNECTION_ERROR);
        broker.register(this, EventConstant.INTERNAL_ERROR);
    }

    public void unregisterEvent() {
        broker.unRegister(this, EventConstant.LOGIN_SUCCESS);
        broker.unRegister(this, EventConstant.LOGIN_FAILED);
        broker.unRegister(this, EventConstant.CONNECTION_ERROR);
        broker.unRegister(this, EventConstant.INTERNAL_ERROR);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtRetry && txtRetry.getVisibility() == View.VISIBLE){
            txtRetry.setVisibility(View.INVISIBLE);
            progressBar.showNow();
            UserController.autoSignIn(this);
        }
    }
}
