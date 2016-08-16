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

public class ForgotPassworkStepOneFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private Button btnNext;
    private EditText edtEmail;
    private boolean edittedEmail;

    public ForgotPassworkStepOneFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent = inflater.inflate(R.layout.fragment_forgot_passwork_step_one, container, false);
        btnNext = (Button) parent.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnNext.setEnabled(false);
        edtEmail = (EditText) parent.findViewById(R.id.edtEnterEmail);
        edtEmail.addTextChangedListener(this);
        getActivity().findViewById(R.id.btnForgotBack).setOnClickListener(this);
        return parent;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNext) {
            Fragment fragment = new ForgotPasswordStepTwoFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment, ForgotPasswordStepTwoFragment.STEP2_TAG).commit();
        } if (v.getId() == R.id.btnForgotBack) {
            getActivity().finish();
        }
    }

    private boolean checkEmail() {
        String email = edtEmail.getText().toString();
        return InputUtils.isEmailValid(email);
    }

    private boolean setErrorMessageEmail() {
        if (edtEmail.getText().toString().equals("")) {
            checkIfRequiredFieldIsEmpty(edittedEmail, edtEmail);
            edittedEmail = false;
            return false;
        }
        edittedEmail = true;
        if (!checkEmail()) {
            edtEmail.setError(getString(R.string.email_error));
            return false;
        }
        return true;
    }

    private void checkIfRequiredFieldIsEmpty(boolean isEditTextEdited, EditText editText) {
        if (isEditTextEdited) {
            editText.setError(getString(R.string.required_field));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btnNext.setEnabled(setErrorMessageEmail());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
