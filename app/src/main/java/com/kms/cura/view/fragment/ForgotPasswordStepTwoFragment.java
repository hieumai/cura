package com.kms.cura.view.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.view.activity.ForgotPasswordActivity;

public class ForgotPasswordStepTwoFragment extends Fragment implements View.OnClickListener {

    public static String STEP2_TAG = "step2";
    private Button btnNext, btnResend;
    private EditText edtCode;
    private String userID;

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
        userID = getArguments().getString(ForgotPasswordActivity.USER_ID);
        getActivity().findViewById(R.id.btnForgotBack).setOnClickListener(this);
        return parent;
    }

    private Button initButton(View parent, int id, View.OnClickListener listener) {
        Button btn = (Button) parent.findViewById(id);
        btn.setOnClickListener(listener);
        return btn;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNext) {
            if (checkCodeInput(edtCode.getText().toString())) {
                checkCodeAsynctask().execute();
            } else {
                Toast.makeText(getActivity(), R.string.code_empty, Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnResendCode) {
            sendCodeAsynctask(getArguments().getString(ForgotPasswordActivity.USER_EMAIL)).execute();
        } else if (v.getId() == R.id.btnForgotBack) {
            Fragment fragment = new ForgotPassworkStepOneFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment).commit();
        }
    }

    private AsyncTask<Object, Void, Void> sendCodeAsynctask(final String userEmail) {
        return new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            private ProgressDialog pDialog;
            private String email;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage(getString(R.string.sending_code));
                pDialog.setCancelable(false);
                pDialog.show();
                email = userEmail;
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    userID = UserController.sendResetCode(email);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pDialog.dismiss();
                if (exception != null) {
                    ErrorController.showDialog(getActivity(), "Error : " + exception.getMessage());
                } else {
                    Toast.makeText(getActivity(), R.string.new_code, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private boolean checkCodeInput(String code) {
        if (code.isEmpty()) {
            return false;
        }
        return true;
    }

    private AsyncTask<Object, Void, Void> checkCodeAsynctask() {
        return new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            private ProgressDialog pDialog;
            private String code, response;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage(getString(R.string.checkingCode));
                pDialog.setCancelable(false);
                pDialog.show();
                code = edtCode.getText().toString().toUpperCase();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    response = UserController.checkCode(userID, code);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pDialog.dismiss();
                if (exception != null) {
                    ErrorController.showDialog(getActivity(), "Error : " + exception.getMessage());
                } else {
                    switch (response) {
                        case UserEntity.CODE_VALID:
                            Fragment fragment = new ForgotPasswordStepThreeFragment();
                            fragment.setArguments(getArguments());
                            getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment).commit();
                            break;
                        case UserEntity.CODE_INVALID:
                            ErrorController.showDialog(getActivity(), getString(R.string.code_invalid));
                            break;
                        case UserEntity.CODE_EXPIRED:
                            ErrorController.showDialog(getActivity(), getString(R.string.code_expired));
                            break;
                    }
                }


            }
        };
    }
}
