package com.kms.cura.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.activity.ViewPatientProfileActivity;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class PatientProfileFragment extends Fragment {

    private TextView txtName, txtGender, txtDOB, txtLocation, txtInsurance, txtHealthConcerns;
    private ImageView profile;
    private UserEntity user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        modifyToolbar();
        return inflater.inflate(R.layout.fagment_patient_profile_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = CurrentUserProfile.getInstance().getEntity();
        loadData(getActivity().getIntent().getStringExtra(ViewPatientProfileActivity.PATIENT_KEY));

    }

    public void loadData(String key) {
        PatientUserEntity entity = (PatientUserEntity) user;
        DataUtils.loadProfile(entity, (ImageView) getActivity().findViewById(R.id.ivAccountAvatar),this.getContext());
        if (key == null) {
            entity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
        } else {
            entity = JsonToEntityConverter.convertJsonStringToEntity(key, PatientUserEntity.getPatientUserType());
        }
        txtName = loadText(DataUtils.showUnicode(entity.getName()), R.id.txtName);
        txtGender = loadText(getGender(entity), R.id.txtGender, R.id.ivGender);
        if (entity.getBirth() == null) {
            txtDOB = loadText(null, R.id.txtDOB, R.id.ivDOB);
        } else {
            Date date = entity.getBirth();
            StringBuilder sb = new StringBuilder();
            sb.append(date.getDate());
            sb.append("/");
            sb.append(date.getMonth() + 1);
            sb.append("/");
            sb.append(date.getYear() + 1900);
            txtDOB = loadText(sb.toString(), R.id.txtDOB, R.id.ivDOB);
        }
        txtLocation = loadText(DataUtils.showUnicode(entity.getLocation()), R.id.txtLocation, R.id.ivLocation);
        txtInsurance = loadText(entity.getInsurance(), R.id.txtInsurance, R.id.ivInsurance);
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

    public TextView loadText(String src, int id, int imageId) {
        TextView textView = (TextView) getActivity().findViewById(id);
        ImageView imageView = (ImageView) getActivity().findViewById(imageId);
        if (src == null) {
            imageView.setVisibility(View.GONE);
            textView.setHeight(0);
        } else {
            textView.setText(src);
        }
        return textView;
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

    private void modifyToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.setTitle(getString(R.string.ProfileView));
        toolbar.inflateMenu(R.menu.menu_blank);
    }
}
