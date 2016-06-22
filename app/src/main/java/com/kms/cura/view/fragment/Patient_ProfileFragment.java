package com.kms.cura.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;

import java.io.UnsupportedEncodingException;

public class Patient_ProfileFragment extends Fragment {
    private TextView txtName, txtGender, txtDOB, txtLocation, txtInsurance, txtHealthConcerns;
    private ImageView profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fagment_patient_profile_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    public void loadData() {
        PatientUserEntity entity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
        profile = loadImage(R.drawable.profile_anon128,R.id.ivAccountAvatar);
        txtName = loadText(entity.getName(), R.id.txtName);
        txtGender = loadText(getGender(entity), R.id.txtGender);
        if (entity.getBirth() == null) {
            txtDOB = loadText(null, R.id.txtDOB);
        } else {
            txtDOB = loadText(entity.getBirth().toString(), R.id.txtDOB);
        }
        txtLocation = loadText(entity.getLocation(), R.id.txtLocation);
        txtInsurance = loadText(entity.getInsurance(), R.id.txtInsurance);
        txtHealthConcerns = loadText(entity.getHealthConcern(), R.id.txtHealthConcern);
    }

    public String getGender(PatientUserEntity patientUserEntity) {
        if (patientUserEntity.getGender() == null) {
            return null;
        }
        if (patientUserEntity.getGender().equals(PatientUserEntity.GENDER_MALE)) {
            return getResources().getString(R.string.male);
        }
        return getResources().getString(R.string.female);
    }

    public TextView loadText(String src, int id) {
        TextView textView = (TextView) getActivity().findViewById(id);
        if (src == null) {
            textView.setHeight(0);
        } else {
            String newString = null;
            try {
                newString = new String(src.getBytes("ISO-8859-1"), "UTF-8");
                textView.setText(newString);
            } catch (UnsupportedEncodingException e) {
                textView.setText(src);
            }
        }
        return textView;
    }
    public ImageView loadImage(int src, int id){
        ImageView imageView = (ImageView) getActivity().findViewById(id);
        imageView.setBackgroundResource(src);
        return imageView;
    }

}
