package com.kms.cura.view.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.UserController;
import com.kms.cura.utils.InputUtils;
import com.kms.cura.view.activity.ForgotPasswordActivity;

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
            checkEmailAsyncTask().execute();
        } if (v.getId() == R.id.btnForgotBack) {
            getActivity().finish();
        }
    }

    private boolean checkEmailValid() {
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
        if (!checkEmailValid()) {
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

    private AsyncTask<Object, Void, Void> checkEmailAsyncTask() {
        return new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            private ProgressDialog pDialog;
            private boolean exist;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage(getString(R.string.check_mail));
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    exist = UserController.checkEmailExist(edtEmail.getText().toString());
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
                    if (!exist) {
                        ErrorController.showDialog(getActivity(), getString(R.string.email_not_exist));
                    } else {
                        sendCodeAsynctask().execute();
                    }
                }
            }
        };
    }

    private AsyncTask<Object, Void, Void> sendCodeAsynctask() {
        return new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            private ProgressDialog pDialog;
            private String id, email;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage(getString(R.string.sending_code));
                pDialog.setCancelable(false);
                pDialog.show();
                email = edtEmail.getText().toString();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    id = UserController.sendResetCode(email);
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
                    Fragment fragment = new ForgotPasswordStepTwoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(ForgotPasswordActivity.USER_ID, id);
                    bundle.putString(ForgotPasswordActivity.USER_EMAIL, email);
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment, ForgotPasswordStepTwoFragment.STEP2_TAG).commit();
                }
            }
        };
    }
}
