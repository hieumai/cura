package com.kms.cura.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kms.cura.R;
import com.kms.cura.utils.InputUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private EditText email, password;
    private Button forgotPasswordButton, loginButton, createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

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
        loginButton.setOnClickListener(this);
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
        int id = v.getId();
        if (id == R.id.button_LoginUI_Login) {
            Intent toProfileView = new Intent(LoginActivity.this, PatientProfileViewActivity.class);
            startActivity(toProfileView);
        }
		else if (v.getId() == R.id.button_LoginUI_CreateAccount) {
            Intent intent = new Intent(this, AccountTypeSelectionActivity.class);
            startActivity(intent);
        }
    }
}