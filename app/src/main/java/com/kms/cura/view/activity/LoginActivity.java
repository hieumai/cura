package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.kms.cura.controller.UserController;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.utils.InputUtils;

public class LoginActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, EventHandler {

    private EditText email, password;
    private Button forgotPasswordButton, loginButton, createAccountButton;
    private EventBroker broker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        broker = EventBroker.getInstance();
        initView();
        registerEvent();
        createAccountButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        if (UserController.checkSignIn(this)) {
            UserController.autoSignIn(this);
            finish();
        }
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
        forgotPasswordButton = initButton(R.id.button_LoginUI_ForgotPassword);
        forgotPasswordButton.setPaintFlags(forgotPasswordButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
        if (!InputUtils.isEmailValid(email.getText().toString())) {
            email.setError(getResources().getString(R.string.email_error));
            loginButton.setEnabled(false);

        }
        if (!InputUtils.isPasswordValid(password.getText().toString())) {
            password.setError(getResources().getString(R.string.pwd_error));
            loginButton.setEnabled(false);
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
        }
    }

    @Override
    public void handleEvent(String event, String data) {
        switch (event) {
            case EventConstant.LOGIN_SUCCESS:
                if (!UserController.checkSignIn(this)) {
                    UserController.saveLoginInfo(this, email.getText().toString(), password.getText().toString());
                }
                switch (data) {
                    case EventConstant.TYPE_PATIENT:
                        Intent toHomePatient = new Intent(this, PatientViewActivity.class);
                        startActivity(toHomePatient);
                        break;
                    case EventConstant.TYPE_DOCTOR:
                        Intent toHomeDoctor = new Intent(this, DoctorViewActivity.class);
                        startActivity(toHomeDoctor);
                        break;
                }
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
                ErrorController.showDialog(this, internalError + " : " + data);
        }
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

}