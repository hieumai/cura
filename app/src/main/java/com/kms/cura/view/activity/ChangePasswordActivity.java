package com.kms.cura.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.InputUtils;

public class ChangePasswordActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private static String ACTIVITY_NAME = "Change Password";
    private Toolbar toolbar;
    private EditText password, newPassword, confirmPassword;
    private Button save;
    boolean editedPwd, editedNewPwd, editedRePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initToolbar();
        initEditTexts();
        initButtons();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.changePassword_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ACTIVITY_NAME);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initButtons() {
        save = (Button) findViewById(R.id.button_password_save);
        save.setOnClickListener(this);
    }

    private void initEditTexts() {
        password = initEditText(R.id.editText_currentPassword);
        newPassword = initEditText(R.id.editText_newPassword);
        confirmPassword = initEditText(R.id.editText_confirmNewPassword);
        password.addTextChangedListener(this);
        newPassword.addTextChangedListener(this);
        confirmPassword.addTextChangedListener(this);
    }

    private EditText initEditText(int id) {
        return (EditText) findViewById(id);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /*  Primative check for email and password as soon as the user type in something
           *   Disable the login button if either condition is invalid nad display an error message accordingly*/
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean flag = setErrorMessagePwd();
        flag &= setErrorMessagePwd();
        flag &= setErrorMessageNewPwd();
        flag &= setErrorMessageRePwd();
        save.setEnabled(flag);
    }

    private String getStringFromEditText(EditText editText) {
        return editText.getText().toString();
    }

    /* Check email and password after the user done entering information and re-enable the login button if both conditions are valid */
    @Override
    public void afterTextChanged(Editable s) {

    }

    private void checkIfRequiredFieldIsEmpty(boolean isEditTextEdited, EditText editText) {
        if (isEditTextEdited) {
            editText.setError(getString(R.string.required_field));
        }
    }

    private boolean setErrorMessagePwd() {
        if (getStringFromEditText(password).equals("")) {
            checkIfRequiredFieldIsEmpty(editedPwd, password);
            editedPwd = false;
            return false;
        }
        editedPwd = true;
        if (!checkPwd() && editedPwd) {
            password.setError(getString(R.string.pwd_error));
            return false;
        }
        return true;
    }

    private boolean setErrorMessageNewPwd() {
        if (getStringFromEditText(newPassword).equals("")) {
            checkIfRequiredFieldIsEmpty(editedNewPwd, newPassword);
            editedNewPwd = false;
            return false;
        }
        editedNewPwd = true;
        if (!checkNewPwd() && editedNewPwd) {
            newPassword.setError(getString(R.string.pwd_error));
            return false;
        }

        if (getStringFromEditText(newPassword).equals(getStringFromEditText(password))) {
            newPassword.setError(getString(R.string.NewPasswordError));
        }
        return true;
    }

    private boolean setErrorMessageRePwd() {
        if (getStringFromEditText(confirmPassword).equals("")) {
            checkIfRequiredFieldIsEmpty(editedRePwd, confirmPassword);
            editedRePwd = false;
            return false;
        }
        editedRePwd = true;
        if (editedRePwd) {
            if (!checkRePwd()) {
                confirmPassword.setError(getString(R.string.repwd_error));
                return false;
            }
            confirmPassword.setError(null);
        }
        return true;
    }

    private boolean checkPwd() {
        return InputUtils.isPasswordValid(getStringFromEditText(password));
    }

    private boolean checkRePwd() {
        return getStringFromEditText(newPassword).equals(getStringFromEditText(confirmPassword));
    }

    private boolean checkNewPwd() {
        return InputUtils.isPasswordValid(getStringFromEditText(newPassword));
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button_password_save) {
            UserEntity current = CurrentUserProfile.getInstance().getEntity();
            UserEntity user = new UserEntity(current.getId(), current.getName(), current.getEmail(), getStringFromEditText(newPassword));
            if (current.getPassword().equals(getStringFromEditText(password))) {
                UserController.updatePassword(user);
                UserController.saveLoginInfo(this, user.getEmail(), user.getPassword());
                onBackPressed();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
            } else {
                password.setText("");
                Toast.makeText(this, getResources().getString(R.string.wrongPassword), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
