package com.kms.cura.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.utils.CurrentDoctorProfile;

import java.util.List;

public class DoctorProfileViewActivity extends AppCompatActivity {
    private Toolbar tbDoctorProfileView;
    private TextView txtName, txtDegree, txtSpeciality, txtYearExperience, txtPriceRange, txtGender;
    private LinearLayout facilityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_view);
        initToolBar();
        loadData();
    }

    public void initToolBar() {
        tbDoctorProfileView = (Toolbar) findViewById(R.id.tbDoctorProfileView);
        setSupportActionBar(tbDoctorProfileView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View content_toolbar = getLayoutInflater().inflate(R.layout.content_toolbar_doctor_profile_view, null);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Gravity.CENTER_HORIZONTAL);
        tbDoctorProfileView.addView(content_toolbar, layoutParams);
    }

    public void loadData() {
        DoctorUserEntity doctorUserEntity = CurrentDoctorProfile.getInstance().getEntity();
        txtName = loadText(doctorUserEntity.getName(), R.id.txtDoctorName);
        txtDegree = loadText(doctorUserEntity.getDegree().getName(), R.id.txtDoctorDegree);
        txtSpeciality = loadText(getSpecialityName(doctorUserEntity), R.id.txtDoctorSpecialties);
        txtGender = loadText(doctorUserEntity.getGender(), R.id.txtDoctorGender);
        txtPriceRange = loadText(getPriceRange(doctorUserEntity), R.id.txtDoctorPrice);
        txtYearExperience = loadText(doctorUserEntity.getExperience() + "", R.id.txtDoctorYearExperience);
        facilityLayout = (LinearLayout) findViewById(R.id.layoutFacility);
        List<FacilityEntity> facilityList = doctorUserEntity.getFacility();
        for (int i = 0; i < facilityList.size(); ++i) {
            facilityLayout.addView(createFacilityView(facilityList.get(i)));
        }
    }


    public View createFacilityView(FacilityEntity entity) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.facility_layout, null);
        ViewHolder holder = new ViewHolder(newView, entity);
        return newView;
    }

    public TextView loadText(String src, int id) {
        TextView textView = (TextView) findViewById(id);
        textView.setText(src);
        return textView;
    }

    public String getSpecialityName(DoctorUserEntity entity) {
        StringBuilder builder = new StringBuilder();
        List<SpecialityEntity> specialityList = entity.getSpeciality();
        for (int i = 0; i < specialityList.size(); ++i) {
            builder.append(specialityList.get(i).getName());
            if (i != specialityList.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public String getPriceRange(DoctorUserEntity entity) {
        StringBuilder builder = new StringBuilder();
        builder.append(entity.getMinPrice());
        builder.append("-");
        builder.append(entity.getMaxPrice());
        return builder.toString();
    }

    class ViewHolder {
        public TextView txtFacilityName, txtFacilityAddress, txtFacilityPhoneNumber, txtFacilityTime;
        private View root = null;

        public ViewHolder(View root, FacilityEntity entity) {
            this.root = root;
            txtFacilityName = loadText(entity.getName(), R.id.txtDoctorFacilityName);
            txtFacilityAddress = loadText(entity.getAddress(), R.id.txtDoctorFacilityAddress);
            txtFacilityPhoneNumber = loadText(entity.getPhone(), R.id.txtDoctorFacilityPhoneNumber);
            txtFacilityTime = loadFacilityTime(entity.getOpeningHours(), R.id.txtDoctorFacilityName);
        }

        public TextView loadText(String src, int id) {
            TextView textView = (TextView) root.findViewById(id);
            textView.setText(src);
            return textView;
        }

        public TextView loadFacilityTime(List<OpeningHour> times, int id) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < times.size(); ++i) {
                OpeningHour openingHour = times.get(i);
                builder.append(openingHour.getDayOfTheWeek().toString());
                builder.append("-");
                builder.append(openingHour.getOpenTime().toString());
                builder.append("-");
                builder.append(openingHour.getCloseTime().toString());
                builder.append("\n");
            }
            TextView textView = (TextView) root.findViewById(id);
            textView.setText(builder.toString());
            return textView;
        }

    }

}
