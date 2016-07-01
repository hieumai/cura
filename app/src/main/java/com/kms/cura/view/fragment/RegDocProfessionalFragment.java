package com.kms.cura.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.model.DegreeModel;
import com.kms.cura.model.FacilityModel;
import com.kms.cura.model.SpecialityModel;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.UpdateSpinner;
import com.kms.cura.view.adapter.CheckBoxAdapter;
import com.kms.cura.view.adapter.StringListAdapter;
import com.kms.cura.view.activity.RegisterDoctorActivity;

import java.util.ArrayList;

public class RegDocProfessionalFragment extends Fragment implements View.OnClickListener, UpdateSpinner, AdapterView.OnItemSelectedListener {

    private ArrayList<String> degree, speciality, facility;
    private ArrayList<String> userSpeciality, userFacility;
    private String userDegree;
    private boolean[] checkedSpeciality, checkedFacility;
    private int checkedDegree;
    private Spinner spnDegree, spnSpeciality, spnFacility;
    private CheckBoxAdapter adapterSpeciality, adapterFacility;
    private StringListAdapter adapterDegree;
    private ImageButton btnNext, btnBack;
    private UpdateSpinner updateSpinner;
    private View myFragmentView = null;
    private String spnText[] = {"Degree Types", "Areas of Speciality", "Facilities"};


    public RegDocProfessionalFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_reg_doc__professional, container, false);
        updateSpinner = this;
        degree = (ArrayList<String>) DataUtils.getListName(DegreeModel.getInstance().getDegrees());
        speciality = (ArrayList<String>) DataUtils.getListName(SpecialityModel.getInstace().getSpecialities());
        facility = (ArrayList<String>) DataUtils.getListName(FacilityModel.getInstace().getFacilities());
        addHintText();
        initButton(myFragmentView);
        reformData();
        initAdapter();
        initSpinner(myFragmentView);
        return myFragmentView;
    }

    private void initAdapter() {
        adapterDegree = new StringListAdapter(getActivity(), R.layout.string_list_item, degree, checkedDegree);
        adapterSpeciality = createAdapter(speciality, checkedSpeciality);
        adapterFacility = createAdapter(facility, checkedFacility);
    }

    private void addHintText() {
        degree.add(spnText[0] + "*");
        speciality.add(spnText[1] + "*");
        facility.add(spnText[2] + "*");
    }

    private void initButton(View myFragmentView) {
        btnBack = setupButton(myFragmentView, R.id.btnRegisterBack, this);
        btnNext = setupButton(myFragmentView, R.id.btnRegisterNext, this);
    }

    private void initSpinner(View myFragmentView) {
        spnDegree = (Spinner) myFragmentView.findViewById(R.id.spnDegree);
        spnDegree.setAdapter(adapterDegree);
        spnDegree.setSelection(checkedDegree);
        spnDegree.setOnItemSelectedListener(this);
        spnSpeciality = setupSpinner(myFragmentView, R.id.spnSpeciality, adapterSpeciality);
        spnFacility = setupSpinner(myFragmentView, R.id.spnFacility, adapterFacility);
    }

    private Spinner setupSpinner(View parent, int resourceId, CheckBoxAdapter adapter) {
        Spinner spn = (Spinner) parent.findViewById(resourceId);
        spn.setAdapter(adapter);
        spn.setSelection(adapter.getCount());
        return spn;
    }

    private CheckBoxAdapter createAdapter(ArrayList<String> data, boolean[] checked) {
        return new CheckBoxAdapter(getActivity(), R.layout.check_box_item, data, checked, updateSpinner);
    }

    private ImageButton setupButton(View parent, int resourceId, View.OnClickListener listener) {
        ImageButton btn = (ImageButton) parent.findViewById(resourceId);
        btn.setOnClickListener(listener);
        return btn;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegisterNext) {
            if (checkInput()) {
                // to next page
                Bundle bundle = createBundle();
                Fragment account = new RegDocAccountFragment();
                account.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_DocReg, account).commit();
            }
        } else if (v.getId() == R.id.btnRegisterBack) {
            Bundle bundle = createBundle();
            Fragment personal = new RegDocPersonalFragment();
            personal.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_DocReg, personal).commit();
        }
    }

    private boolean checkInput() {
        boolean flag = true;
        StringBuilder error_mes = new StringBuilder();
        int count = 0;
        if (!checkDegree()) count++;
        if (!checkSpeciality()) count++;
        if (!checkFacility()) count++;

        if (!checkDegree()) {
            flag = false;
            buildErrorMessage(error_mes, count, 0);
        }
        if (!checkSpeciality()) {
            flag = false;
            buildErrorMessage(error_mes, count, 1);
        }
        if (!checkFacility()) {
            flag = false;
            buildErrorMessage(error_mes, count, 2);
        }
        if (!flag) {
            error_mes.append("can't be left empty");
            Toast.makeText(getActivity(), error_mes.toString(), Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    private void buildErrorMessage(StringBuilder error_mes, int count, int curSpinner) {
        if (count > 1) {
            error_mes.append(spnText[curSpinner] + ", ");
            count--;
        } else {
            error_mes.append(spnText[curSpinner] + " ");
        }
    }

    private boolean checkDegree() {
        userDegree = degree.get(spnDegree.getSelectedItemPosition());
        return !(spnDegree.getSelectedItemPosition() == degree.size() - 1);
    }

    private boolean checkSpeciality() {
        userSpeciality = adapterSpeciality.getSelectedString();
        return !userSpeciality.isEmpty();
    }

    private boolean checkFacility() {
        userFacility = adapterFacility.getSelectedString();
        return !userFacility.isEmpty();
    }


    private Bundle createBundle() {
        Bundle bundle = getArguments();
        bundle.putString(RegisterDoctorActivity.DEGREE, userDegree);
        bundle.putStringArrayList(RegisterDoctorActivity.SPECIALITY, userSpeciality);
        bundle.putStringArrayList(RegisterDoctorActivity.FACILITY, userFacility);
        bundle.putInt(RegisterDoctorActivity.DEGREE, spnDegree.getSelectedItemPosition());
        bundle.putBooleanArray(RegisterDoctorActivity.SPECIALITY, adapterSpeciality.getSelectedBoolean());
        bundle.putBooleanArray(RegisterDoctorActivity.FACILITY, adapterFacility.getSelectedBoolean());
        return bundle;
    }

    private void reformData() {
        Bundle bundle = getArguments();
        if (bundle.getBoolean(RegisterDoctorActivity.PROFESSIONAL)) {
            checkedDegree = bundle.getInt(RegisterDoctorActivity.DEGREE);
            checkedSpeciality = bundle.getBooleanArray(RegisterDoctorActivity.SPECIALITY);
            checkedFacility = bundle.getBooleanArray(RegisterDoctorActivity.FACILITY);
        } else {
            checkedDegree = degree.size() - 1;
            checkedSpeciality = null;
            checkedFacility = null;
        }
    }

    @Override
    public void callBackUpdateSpinner() {
        adapterFacility = new CheckBoxAdapter(getActivity(), R.layout.check_box_item, facility, adapterFacility.getSelectedBoolean(), updateSpinner);
        spnFacility.setAdapter(adapterFacility);
        spnFacility.setSelection(adapterFacility.getCount());
        adapterSpeciality = new CheckBoxAdapter(getActivity(), R.layout.check_box_item, speciality, adapterSpeciality.getSelectedBoolean(), updateSpinner);
        spnSpeciality.setAdapter(adapterSpeciality);
        spnSpeciality.setSelection(adapterSpeciality.getCount());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        checkedDegree = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
