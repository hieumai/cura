package com.kms.cura.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kms.cura.R;
import com.kms.cura.utils.InputUtils;

public class ChangePasswordActivity extends AppCompatActivity implements TextWatcher {
    private static String ACTIVITY_NAME = "Change Password";
    private Toolbar toolbar;
    private EditText password, newPassword, confirmPassword;
    private Button save;

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
    }

    private void initEditTexts() {
        password = initEditText(R.id.editText_currentPassword);
        newPassword = initEditText(R.id.editText_newPassword);
        confirmPassword = initEditText(R.id.editText_confirmNewPassword);
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

        if (!InputUtils.isPasswordValid(password.getText().toString())) {
            password.setError(getResources().getString(R.string.pwd_error));
            save.setEnabled(false);
        }
        if (!InputUtils.isPasswordValid(newPassword.getText().toString())) {
            newPassword.setError(getResources().getString(R.string.pwd_error));
            save.setEnabled(false);
        }
        if (!InputUtils.isPasswordValid(confirmPassword.getText().toString())) {
            confirmPassword.setError(getResources().getString(R.string.pwd_error));
            save.setEnabled(false);
        }
    }

    /* Check email and password after the user done entering information and re-enable the login button if both conditions are valid */
    @Override
    public void afterTextChanged(Editable s) {
        if (InputUtils.isPasswordValid(password.getText().toString()) && newPassword.getText().equals(confirmPassword.getText())) {
            save.setEnabled(true);
        } else if (newPassword.getText().equals("")) {
            newPassword.setError(getResources().getString(R.string.required_field));
        } else if (!confirmPassword.getText().equals(newPassword.getText())) {
            confirmPassword.setError(getResources().getString(R.string.repwd_error));
        }

    }


}
