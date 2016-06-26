package com.kms.cura.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.DegreeController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.FacilityController;
import com.kms.cura.controller.SpecialityController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.model.DegreeModel;
import com.kms.cura.utils.InputUtils;
import com.kms.cura.view.activity.RegisterDoctorActivity;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class RegDocAccountFragment extends Fragment implements View.OnClickListener, TextWatcher, EventHandler {

    private EditText edtPhone, edtEmail, edtPwd, edtRePwd;
    private Button btnRegister;
    private ImageButton btnBack;
    private List<SpecialityEntity> specialityList;
    private List<FacilityEntity> facilityList;
    private DegreeEntity degreeEntity;
    private Bundle bundle;
    private String name, sex;
    private Date dob;
    private EventBroker broker;
    boolean edittedPhone, edittedEmail, edittedPwd, edittedRePwd;

    public RegDocAccountFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broker = EventBroker.getInstance();
        registerEvent();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_reg_doc__account, container, false);
        edtPhone = setupEditText(myFragmentView, R.id.edtPhone, this);
        edtEmail = setupEditText(myFragmentView, R.id.edtEmail, this);
        edtPwd = setupEditText(myFragmentView, R.id.edtPwd, this);
        edtRePwd = setupEditText(myFragmentView, R.id.edtRePwd, this);
        btnRegister = (Button) myFragmentView.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnBack = (ImageButton) myFragmentView.findViewById(R.id.btnRegisterBack);
        btnBack.setOnClickListener(this);

        edittedPwd = false;
        edittedRePwd = false;
        reformData();
        return myFragmentView;
    }

    private EditText setupEditText(View parent, int resourceId, TextWatcher textWatcher) {
        EditText edt = (EditText) parent.findViewById(resourceId);
        edt.addTextChangedListener(textWatcher);
        return edt;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegisterBack) {
            Bundle bundle = createBundle();
            Fragment professional = new RegDocProfessionalFragment();
            professional.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_DocReg, professional).commit();
        } else if (v.getId() == R.id.btnRegister) {
            if (checkInput()) {
                String phone = edtPhone.getText().toString();
                String email = edtEmail.getText().toString();
                String pwd = edtPwd.getText().toString();
                bundle = getArguments();
                specialityList = SpecialityController.getSpecialitySelected(bundle);
                facilityList = FacilityController.getFacilitySelected(bundle);
                degreeEntity = DegreeController.getDegreeSelected(bundle);
                name = bundle.getString(RegisterDoctorActivity.FIRST_NAME) + " "
                        + bundle.getString(RegisterDoctorActivity.LAST_NAME);
                sex = bundle.getString(RegisterDoctorActivity.SEX);
                dob = getDOB();
                UserController.registerDoctor(null, name, email, pwd, phone, degreeEntity, specialityList, facilityList, sex, dob);
            }
        }
    }

    private boolean checkInput() {
        return checkEmail() && checkPwd() && checkRePwd() && checkPhone();
    }

    private Date getDOB() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(bundle.getInt(RegisterDoctorActivity.DOB_YEAR), bundle.getInt(RegisterDoctorActivity.DOB_MONTH) - 1, bundle.getInt(RegisterDoctorActivity.DOB_DAY));
        return new Date(calendar.getTimeInMillis());
    }

    private boolean checkPhone() {
        return !edtPhone.getText().toString().isEmpty();
    }

    private boolean checkEmail() {
        String email = edtEmail.getText().toString();
        return InputUtils.isEmailValid(email);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean flag = setErrorMessagePhone();
        flag &= setErrorMessageEmail();
        flag &= setErrorMessagePwd();
        flag &= setErrorMessageRePwd();
        btnRegister.setEnabled(flag);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private Bundle createBundle() {
        Bundle bundle = getArguments();
        bundle.putBoolean(RegisterDoctorActivity.PROFESSIONAL, true);
        bundle.putString(RegisterDoctorActivity.PHONE, edtPhone.getText().toString());
        bundle.putString(RegisterDoctorActivity.EMAIL, edtEmail.getText().toString());
        return bundle;
    }

    private void reformData() {
        Bundle bundle = getArguments();
        edtPhone.setText(bundle.getString(RegisterDoctorActivity.PHONE, ""));
        edtEmail.setText(bundle.getString(RegisterDoctorActivity.EMAIL, ""));
        edittedPhone = !edtPhone.getText().toString().equals("");
        edittedEmail = !edtEmail.getText().toString().equals("");
    }

    private boolean setErrorMessageEmail() {

        if (edtEmail.getText().toString().equals("")) {
            checkIfRequiredFieldIsEmpty(edittedEmail, edtEmail);
            edittedEmail = false;
            return false;
        }
        edittedEmail = true;
        if (!checkEmail() && edittedEmail) {
            edtEmail.setError(getString(R.string.email_error));
            return false;
        }
        return true;
    }

    private boolean setErrorMessagePwd() {
        if (edtPwd.getText().toString().equals("")) {
            checkIfRequiredFieldIsEmpty(edittedPwd, edtPwd);
            edittedPwd = false;
            return false;
        }
        edittedPwd = true;
        if (!checkPwd() && edittedPwd) {
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

    private boolean setErrorMessagePhone() {
        if (edtPhone.getText().toString().equals("")) {
            checkIfRequiredFieldIsEmpty(edittedPhone, edtPhone);
            edittedPhone = false;
            return false;
        }
        edittedPhone = true;
        return true;
    }

    private boolean setErrorMessageRePwd() {
        if (edtRePwd.getText().toString().equals("")) {
            checkIfRequiredFieldIsEmpty(edittedRePwd, edtRePwd);
            edittedRePwd = false;
            return false;
        }
        edittedRePwd = true;
        if (edittedRePwd) {
            if (!checkRePwd()) {
                edtRePwd.setError(getString(R.string.repwd_error));
                return false;
            }
            edtRePwd.setError(null);
        }
        return true;
    }


    private DegreeEntity getDegreeSelected() {
        int degree = bundle.getInt(RegisterDoctorActivity.DEGREE);
        return DegreeModel.getInstace().getDegrees().get(degree);
    }

    @Override
    public void handleEvent(String event, String data) {
        switch (event) {
            case EventConstant.REGISTER_SUCCESS:
                ErrorController.showDialog(getActivity(), "Register success");
                break;
            case EventConstant.REGISTER_FAILED:
                ErrorController.showDialog(getActivity(), "Register failed :" + data);
                break;
            case EventConstant.CONNECTION_ERROR:
                if (data != null) {
                    ErrorController.showDialog(getActivity(), "Error " + data);
                } else {
                    ErrorController.showDialog(getActivity(), "Error " + getResources().getString(R.string.ConnectionError));
                }
                break;
            case EventConstant.INTERNAL_ERROR:
                ErrorController.showDialog(getActivity(), getResources().getString(R.string.InternalError) + " :" + data);
        }
    }

    @Override
    public void onDetach() {
        unregisterEvent();
        super.onDetach();
    }
}
