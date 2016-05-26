package com.kms.cura.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kms.cura.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientSignUpActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private EditText edtFirstName, edtEmail, edtPassword, edtPasswordReenter;
    private TextView txtErrorFirstName, txtErrorEmail, txtErrorPassword;
    private Button btnRegister;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);
        initEditText();
        initTextView();
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Patient Register Function
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        if (!hasFocus) {
            if (id == R.id.edtFirstName) {
                if ("".equals(edtFirstName.getText().toString())) {
                    txtErrorFirstName.setText(R.string.FirstNameError);
                    --count;
                } else {
                    txtErrorFirstName.setText("");
                    ++count;
                }
            } else if (id == R.id.edtEmail) {
                if (!isEmail(edtEmail.getText().toString())) {
                    txtErrorEmail.setText(R.string.EmailError);
                    --count;
                } else {
                    txtErrorEmail.setText("");
                    ++count;
                }
            } else {
                if (!isPassword(edtPassword.getText().toString())) {
                    txtErrorPassword.setText(R.string.PasswordError);
                    --count;
                } else {
                    txtErrorPassword.setText("");
                    ++count;
                }
            }
        }
        btnRegister.setEnabled(count == 3);

    }

    public void initEditText() {
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPasswordReenter = (EditText) findViewById(R.id.edtPasswordReenter);
        edtFirstName.setOnFocusChangeListener(this);
        edtEmail.setOnFocusChangeListener(this);
        edtPassword.setOnFocusChangeListener(this);
    }

    public void initTextView() {
        txtErrorFirstName = (TextView) findViewById(R.id.txtErrorFirstName);
        txtErrorEmail = (TextView) findViewById(R.id.txtErrorEmail);
        txtErrorPassword = (TextView) findViewById(R.id.txtErrorPassword);
    }

    public boolean isEmail(String email) {
        if ("".equals(email)) {
            return false;
        }
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(email);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public boolean isPassword(String password) {
        if ("".equals(password)) {
            return false;
        }
        if (password.length() > 5 && password.length() < 17) {
            return true;
        }
        return false;
    }
}
