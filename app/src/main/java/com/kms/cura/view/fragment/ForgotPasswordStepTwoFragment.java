package com.kms.cura.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kms.cura.R;

public class ForgotPasswordStepTwoFragment extends Fragment implements View.OnClickListener {

    public static String STEP2_TAG = "step2";
    private Button btnNext, btnResend;
    private EditText edtCode;

    public ForgotPasswordStepTwoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent = inflater.inflate(R.layout.fragment_forgot_password_step_two, container, false);
        btnNext = initButton(parent, R.id.btnNext, this);
        btnResend = initButton(parent, R.id.btnResendCode, this);
        edtCode = (EditText) parent.findViewById(R.id.edtEnterCode);
        modifyToolbar();
        getActivity().findViewById(R.id.btnForgotBack).setOnClickListener(this);
        return parent;
    }

    private void modifyToolbar() {
        ImageButton btnBack = (ImageButton) getActivity().findViewById(R.id.btnForgotBack);
        btnBack.setOnClickListener(this);
    }

    private Button initButton(View parent, int id, View.OnClickListener listener) {
        Button btn = (Button) parent.findViewById(id);
        btn.setOnClickListener(listener);
        return btn;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNext) {
            Fragment fragment = new ForgotPasswordStepThreeFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment).commit();
        } if (v.getId() == R.id.btnForgotBack) {
            Fragment fragment = new ForgotPassworkStepOneFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment).commit();
        }
    }


}
