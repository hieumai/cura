package com.kms.cura.view.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.DegreeController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.FacilityController;
import com.kms.cura.controller.SpecialityController;
import com.kms.cura.utils.InputUtils;
import com.kms.cura.view.activity.RegisterDoctorActivity;
import com.kms.cura.view.adapter.StringSexListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class RegDocPersonalFragment extends Fragment implements View.OnClickListener {

    private EditText edtFName, edtLName;
    private String fName, lName;
    private int sex;
    private int day, month, year;
    private Calendar calendar;
    private TextView tvBirth;
    private ImageButton btnChooseDate, btnNext;
    private Spinner spnSex;
    private ProgressDialog pDialog;

    public RegDocPersonalFragment() {
        setCurrentDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_reg_doc__personal, container, false);
        tvBirth = (TextView) myFragmentView.findViewById(R.id.tvBDate);
        initButton(myFragmentView);
        initEdittext(myFragmentView);
        initSpinner(myFragmentView);

        reformData();

        return myFragmentView;
    }

    private void initButton(View myFragmentView) {
        btnChooseDate = (ImageButton) myFragmentView.findViewById(R.id.btnChooseDate);
        btnChooseDate.setOnClickListener(this);
        btnNext = (ImageButton) myFragmentView.findViewById(R.id.btnRegisterNext);
        btnNext.setOnClickListener(this);
    }

    private void initSpinner(View myFragmentView) {
        ArrayList<String> sex = new ArrayList<>();
        sex.add(getString(R.string.male));
        sex.add(getString(R.string.female));
        StringSexListAdapter adapter = new StringSexListAdapter(getActivity(), R.layout.string_list_item2, sex);
        spnSex = (Spinner) myFragmentView.findViewById(R.id.spnSex);
        spnSex.setAdapter(adapter);
    }

    private void initEdittext(View myFragmentView) {
        edtFName = (EditText) myFragmentView.findViewById(R.id.edtDocReg_FirstName);
        edtLName = (EditText) myFragmentView.findViewById(R.id.edtDocReg_LastName);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnChooseDate) {
            Dialog dateDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, myDateListener, year, month - 1, day);
            dateDialog.show();
        } else if (v.getId() == R.id.btnRegisterNext) {
            // check input
            if (checkInput()) {
                // process next page
                Bundle bundle = createBundle();
                final Fragment professional = new RegDocProfessionalFragment();
                professional.setArguments(bundle);
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                showProgressDialog();
                AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
                    private Exception exception = null;

                    @Override
                    protected Void doInBackground(Object[] params) {
                        try {
                            SpecialityController.initData();
                            FacilityController.initData();
                            DegreeController.initData();
                        } catch (Exception e) {
                            exception = e;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        hideProgressDialog();
                        if (exception == null) {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_DocReg, professional).commit();
                        } else {
                            ErrorController.showDialog(getActivity(), "Error : " + exception.getMessage());
                        }
                    }
                };
                task.execute();
            }
        }
    }

    public void setDateString(int day, int month, int year) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        if (month < 10)
            builder.append("0");
        builder.append(month + "/");
        if (day < 10)
            builder.append('0');
        builder.append(day + "/");
        builder.append(year);
        tvBirth.setText(builder.toString());
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            setSpecificDate(day, month + 1, year);
        }
    };

    private boolean checkInput() {
        // check and show error
        boolean flag = checkName(edtFName);
        flag &= checkName(edtLName);
        calendar = Calendar.getInstance();
        if ((calendar.get(Calendar.YEAR) - year) < 24) {
            Toast.makeText(getActivity(), getString(R.string.age_error), Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    private boolean checkName(EditText edt) {
        if (edt.getText().toString().isEmpty()) {
            edt.setError(getString(R.string.required_field));
            return false;
        }
        if (!InputUtils.isNameValid(edt.getText().toString().trim())) {
            edt.setError(getString(R.string.name_error));
            return false;
        }
        return true;
    }

    private Bundle createBundle() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putBoolean(RegisterDoctorActivity.PROFESSIONAL, false);
        } else {
            bundle.putBoolean(RegisterDoctorActivity.PROFESSIONAL, true);
        }
        fName = edtFName.getText().toString().trim();
        lName = edtLName.getText().toString().trim();
        bundle.putString(RegisterDoctorActivity.FIRST_NAME, fName);
        bundle.putString(RegisterDoctorActivity.LAST_NAME, lName);
        int tmp = spnSex.getSelectedItemPosition();
        if (tmp == 0) // male
            bundle.putString(RegisterDoctorActivity.SEX, RegisterDoctorActivity.SEX_MALE);
        else
            bundle.putString(RegisterDoctorActivity.SEX, RegisterDoctorActivity.SEX_FEMALE);
        bundle.putInt(RegisterDoctorActivity.DOB_DAY, day);
        bundle.putInt(RegisterDoctorActivity.DOB_MONTH, month);
        bundle.putInt(RegisterDoctorActivity.DOB_YEAR, year);
        return bundle;
    }

    private void reformData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            setSpecificDate(bundle.getInt(RegisterDoctorActivity.DOB_DAY), bundle.getInt(RegisterDoctorActivity.DOB_MONTH), bundle.getInt(RegisterDoctorActivity.DOB_YEAR));
            edtFName.setText(bundle.getString(RegisterDoctorActivity.FIRST_NAME, ""));
            edtLName.setText(bundle.getString(RegisterDoctorActivity.LAST_NAME, ""));
            String sex = bundle.getString(RegisterDoctorActivity.SEX, "");
            if (sex.equals(RegisterDoctorActivity.SEX_MALE))
                spnSex.setSelection(0);
            else if (sex.equals(RegisterDoctorActivity.SEX_FEMALE))
                spnSex.setSelection(1);
        } else {
            setDateString(day, month, year);
        }
    }

    private void setCurrentDate() {
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR) - 24;

    }

    private void setSpecificDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        setDateString(day, month, year);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

}

