package com.kms.cura.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kms.cura.R;
import com.kms.cura.utils.InputUtils;

public class ForgotPasswordStepThreeFragment extends Fragment implements TextWatcher, View.OnClickListener {

    private Button btnDone;
    private EditText edtPwd, edtRePwd;
    private boolean edittedPwd, edittedRePwd;

    public ForgotPasswordStepThreeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent = inflater.inflate(R.layout.fragment_forgot_password_step_three, container, false);
        btnDone = (Button) parent.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        btnDone.setEnabled(false);
        edtPwd = initEditText(parent, R.id.edtEnterNewPassword, this);
        edtRePwd = initEditText(parent, R.id.edtReEnterPassword, this);
        edittedPwd = false;
        edittedRePwd = false;
        getActivity().findViewById(R.id.btnForgotBack).setOnClickListener(this);
        return parent;
    }

    private EditText initEditText(View parent, int id, TextWatcher textWatcher) {
        EditText edt = (EditText) parent.findViewById(id);
        edt.addTextChangedListener(textWatcher);
        return edt;
    }

    private boolean checkPwd() {
        String pwd = edtPwd.getText().toString();
        return InputUtils.isPasswordValid(pwd);
    }

    private boolean checkRePwd() {
        String pwd = edtPwd.getText().toString();
        String rePwd = edtRePwd.getText().toString();
        return pwd.equals(rePwd);
    }

    private boolean setErrorMessagePwd() {
        if (edtPwd.getText().toString().equals("")) {
            checkIfRequiredFieldIsEmpty(edittedPwd, edtPwd);
            edittedPwd = false;
            return false;
        }
        edittedPwd = true;
        if (!checkPwd()) {
            edtPwd.setError(getString(R.string.pwd_error));
            return false;
        }
        return true;
    }

    private void checkIfRequiredFieldIsEmpty(boolean isEditTextEdited, EditText editText) {
        if (isEditTextEdited) {
            editText.setError(getString(R.string.required_field));
        }
    }

    private boolean setErrorMessageRePwd() {
        if (edtRePwd.getText().toString().equals("")) {
            checkIfRequiredFieldIsEmpty(edittedRePwd, edtRePwd);
            edittedRePwd = false;
            return false;
        }
        edittedRePwd = true;
        if (!checkRePwd()) {
            edtRePwd.setError(getString(R.string.repwd_error));
            return false;
        }
        edtRePwd.setError(null);
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btnDone.setEnabled(setErrorMessagePwd() && setErrorMessageRePwd());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
    }
}
