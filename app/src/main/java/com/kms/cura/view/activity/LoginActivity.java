package com.kms.cura.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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
import com.kms.cura.utils.InputUtils;

public class LoginActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, EventHandler {

    private EditText email, password;
    private Button forgotPasswordButton, loginButton, createAccountButton;
    private EventBroker broker;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered = false;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Settings.REGISTRATION_INCOMPLETE) {
                    ErrorController.showDialog(LoginActivity.this, getString(R.string.notreceiveNoti));
                } else {
                    UserController.saveGCMRegisterKey(LoginActivity.this, intent.getStringExtra(NotificationEntity.REG_ID));
                }
                navigateTo(CurrentUserProfile.getInstance().isPatient());
            }
        };
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        broker = EventBroker.getInstance();
        initView();
        registerEvent();
        registerReceiver();
        createAccountButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
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

    /**
     * Init view with entity object
     */
    public void initView() {
        //Fix orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initButtons();
        initTextFields();
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

    private void initTextFields() {
        email = (EditText) findViewById(R.id.editText_LoginUI_Email);
        password = (EditText) findViewById(R.id.editText_LoginUI_Password);
        //Add TextChangedListener
        email.addTextChangedListener(this);
        password.addTextChangedListener(this);
    }

    private void initButtons() {
        initPasswordButton();
        initAccountButton();
        initLoginButton();
    }

    private void initLoginButton() {
        loginButton = initButton(R.id.button_LoginUI_Login);
        loginButton.setEnabled(false);
    }

    private void initAccountButton() {
        createAccountButton = initButton(R.id.button_LoginUI_CreateAccount);
        createAccountButton.setOnClickListener(this);
    }

    private void initPasswordButton() {
        forgotPasswordButton = initButton(R.id.btnForgotPassword);
        forgotPasswordButton.setPaintFlags(forgotPasswordButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotPasswordButton.setOnClickListener(this);
    }

    private Button initButton(int id) {
        return initButton(id, true);
    }

    private Button initButton(int id, boolean turnOffTransformation) {
        Button button = (Button) findViewById(id);
        if (turnOffTransformation) {
            button.setTransformationMethod(null);
        }
        return button;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation/keyboard change
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /*  Primative check for email and password as soon as the user type in something
           *   Disable the login button if either condition is invalid nad display an error message accordingly*/
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (email.hasFocus()) {
            if (!InputUtils.isEmailValid(email.getText().toString())) {
                email.setError(getResources().getString(R.string.email_error));
                loginButton.setEnabled(false);

            } else {
                email.setError(null);
            }
        }
        if (password.hasFocus()) {
            if (!InputUtils.isPasswordValid(password.getText().toString())) {
                password.setError(getResources().getString(R.string.pwd_error));
                loginButton.setEnabled(false);
            } else {
                password.setError(null);
            }
        }
    }

    /* Check email and password after the user done entering information and re-enable the login button if both conditions are valid */
    @Override
    public void afterTextChanged(Editable s) {
        if (InputUtils.isEmailValid(email.getText().toString()) && InputUtils.isPasswordValid(password.getText().toString())) {
            loginButton.setEnabled(true);
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_LoginUI_CreateAccount) {
            Intent intent = new Intent(this, AccountTypeSelectionActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.button_LoginUI_Login) {
            UserController.userLogin(email.getText().toString(), password.getText().toString());
        } else if (v.getId() == R.id.btnForgotPassword){
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        }
    }


    private void navigateTo(boolean patient) {
        Intent toHome = null;
        if (patient) {
            toHome = new Intent(this, PatientViewActivity.class);
        }
        else{
            toHome = new Intent(LoginActivity.this, DoctorViewActivity.class);
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
                UserController.saveLoginInfo(this, email.getText().toString(), password.getText().toString());
                if (UserController.alreadyRegisterdGCM(LoginActivity.this) || !GCMUtils.checkPlayServices(this)) {
                    navigateTo(patient);
                    return;
                }
                Intent intent = new Intent(this, RegisterIntentService.class);
                intent.putExtra(RegisterIntentService.CURRENT_USER_ID, CurrentUserProfile.getInstance().getEntity().getId());
                startService(intent);
                break;
            case EventConstant.LOGIN_FAILED:
                ErrorController.showDialog(this, "Login failed :" + data);
                break;
            case EventConstant.CONNECTION_ERROR:
                if (data != null) {
                    ErrorController.showDialog(this, "Error " + data);
                } else {
                    ErrorController.showDialog(this, "Error " + getResources().getString(R.string.ConnectionError));
                }
                break;
            case EventConstant.INTERNAL_ERROR:
                String internalError = getResources().getString(R.string.InternalError);
                if (data == null) {
                    data = "";
                }
                ErrorController.showDialog(this, internalError + " : " + data);
        }
    }

    @Override
    protected void onPause() {
        unregisterEvent();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerEvent();
        registerReceiver();
        super.onResume();
    }


}