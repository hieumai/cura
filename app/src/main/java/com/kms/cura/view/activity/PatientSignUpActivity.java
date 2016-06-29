package com.kms.cura.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.UserController;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.utils.InputUtils;
import com.kms.cura.view.fragment.PatientProfileFragment;

public class PatientSignUpActivity extends AppCompatActivity implements TextWatcher, EventHandler {
    private EditText edtFirstName, edtEmail, edtPassword, edtPasswordReenter;
    private Button btnRegister;
    private int count = 0;
    private EventBroker broker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);
        initEditText();
        broker = EventBroker.getInstance();
        btnRegister = (Button) findViewById(R.id.btnRegister);
        registerEvent();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.registerPatient(getEditTextText(edtFirstName), getEditTextText(edtEmail), getEditTextText(edtPassword));
            }
        });

    }


    public void initEditText() {
        edtFirstName = initEditText(R.id.edtFirstName, this);
        edtEmail = initEditText(R.id.edtEmail, this);
        edtPassword = initEditText(R.id.edtPassword, this);
        edtPasswordReenter = initEditText(R.id.edtPasswordReenter, this);
    }

    public EditText initEditText(int id, TextWatcher watcher) {
        EditText tmp = (EditText) findViewById(id);
        tmp.addTextChangedListener(watcher);
        return tmp;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Ignore
    }

    public void registerEvent() {
        broker.register(this, EventConstant.REGISTER_SUCCESS);
        broker.register(this, EventConstant.REGISTER_FAILED);
        broker.register(this, EventConstant.CONNECTION_ERROR);
        broker.register(this, EventConstant.INTERNAL_ERROR);
    }

    public void unregisterEvent() {
        broker.unRegister(this, EventConstant.REGISTER_SUCCESS);
        broker.unRegister(this, EventConstant.REGISTER_FAILED);
        broker.unRegister(this, EventConstant.CONNECTION_ERROR);
        broker.unRegister(this, EventConstant.INTERNAL_ERROR);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        validateFirstName();
        validateEmail();
        validatePassword();
        validatePasswordReenter();
    }

    @Override
    public void afterTextChanged(Editable s) {
        btnRegister.setEnabled(validateFirstName() && validateEmail() && validatePassword() && validatePasswordReenter());
    }

    public String getEditTextText(EditText src) {
        return src.getText().toString();
    }

    public boolean validateFirstName() {
        if ("".equals(getEditTextText(edtFirstName))) {
            edtFirstName.setError(getResources().getString(R.string.FirstNameError));
            return false;
        }
        if (!InputUtils.isNameValid(getEditTextText(edtFirstName))) {
            edtFirstName.setError(getResources().getString(R.string.first_name_error));
            return false;
        }
        return true;
    }

    public boolean validateEmail() {
        if (!InputUtils.isEmailValid(getEditTextText(edtEmail))) {
            edtEmail.setError(getResources().getString(R.string.EmailError));
            return false;
        }
        return true;
    }

    public boolean validatePassword() {
        if (!InputUtils.isPasswordValid(getEditTextText(edtPassword))) {
            edtPassword.setError(getResources().getString(R.string.PasswordError));
            return false;
        }
        return true;
    }

    public boolean validatePasswordReenter() {
        if (!getEditTextText(edtPasswordReenter).equals(getEditTextText(edtPassword))) {
            edtPasswordReenter.setError(getResources().getString(R.string.PasswordReenterError));
            return false;
        }
        return true;
    }

    @Override
    public void handleEvent(String event, Object data) {
        switch (event) {
            case EventConstant.REGISTER_SUCCESS:
                Intent toProfile = new Intent(this, PatientProfileFragment.class);
                startActivity(toProfile);
                break;
            case EventConstant.REGISTER_FAILED:
                ErrorController.showDialog(this, "Register failed :" + data);
                break;
            case EventConstant.CONNECTION_ERROR:
                if (data != null) {
                    ErrorController.showDialog(this, "Error " + data);
                } else {
                    ErrorController.showDialog(this, "Error " + getResources().getString(R.string.ConnectionError));
                }
                break;
            case EventConstant.INTERNAL_ERROR:
                ErrorController.showDialog(this, getResources().getString(R.string.InternalError) + " :" + data);
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
