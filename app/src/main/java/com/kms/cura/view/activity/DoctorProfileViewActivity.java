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
import com.kms.cura.utils.DataUtils;

import java.util.List;

public class DoctorProfileViewActivity extends AppCompatActivity {
    private Toolbar tbDoctorProfileView;
    private TextView txtName, txtDegree, txtSpeciality, txtYearExperience, txtPriceRange, txtGender;
    private LinearLayout facilityLayout;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_view);
        init();
        initToolBar();
        loadData();
    }

    public void init() {
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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
        txtGender = loadText(getGender(doctorUserEntity), R.id.txtDoctorGender);
        txtPriceRange = loadText(getPriceRange(doctorUserEntity), R.id.txtDoctorPrice);
        txtYearExperience = loadText(doctorUserEntity.getExperience() + "", R.id.txtDoctorYearExperience);
        facilityLayout = (LinearLayout) findViewById(R.id.layoutFacility);
        List<FacilityEntity> facilityList = doctorUserEntity.getFacility();
        for (int i = 0; i < facilityList.size(); ++i) {
            facilityLayout.addView(createFacilityView(facilityList.get(i)));
        }
    }

    private View createFacilityView(FacilityEntity entity) {
        View facility = inflater.inflate(R.layout.facility_layout, null);
        ViewHolder holder = new ViewHolder(facility, entity);
        holder.loadFacility();
        return facility;
    }

    public String getGender(DoctorUserEntity doctorUserEntity){
        if(doctorUserEntity.getGender().equals(DoctorUserEntity.MALE)){
            return getResources().getString(R.string.male);
        }
        return getResources().getString(R.string.female);
    }

    private View createFacilityTimeView(String day, String time) {
        View facilityTime = inflater.inflate(R.layout.time_facility_layout, null);
        TextView txtDay = (TextView) facilityTime.findViewById(R.id.txtDay);
        TextView txtTime = (TextView) facilityTime.findViewById(R.id.txtTime);
        txtDay.setText(day);
        txtTime.setText(time);
        return facilityTime;
    }


    public TextView loadText(String src, int id) {
        TextView textView = (TextView) findViewById(id);
        textView.setText(src);
        return textView;
    }

    public String getSpecialityName(DoctorUserEntity entity) {
        StringBuilder builder = new StringBuilder();
        List<SpecialityEntity> specialityEntities = entity.getSpeciality();
        List<String> names = DataUtils.getListName(specialityEntities);
        for (int i = 0; i < names.size(); ++i) {
            builder.append(names.get(i));
            if (i != names.size() - 1) {
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
        private TextView txtFacilityName, txtFacilityAddress, txtFacilityPhoneNumber;
        private LinearLayout layoutFacilityTime;
        private View root = null;
        private FacilityEntity entity;

        public ViewHolder(View root, FacilityEntity entity) {
            this.root = root;
            this.entity = entity;
        }

        public TextView loadText(String src, int id) {
            TextView textView = (TextView) root.findViewById(id);
            textView.setText(src);
            return textView;
        }

        public void loadFacility(){
            txtFacilityName = loadText(entity.getName(), R.id.txtDoctorFacilityName);
            txtFacilityAddress = loadText(entity.getAddress(), R.id.txtDoctorFacilityAddress);
            txtFacilityPhoneNumber = loadText(entity.getPhone(), R.id.txtDoctorFacilityPhoneNumber);
            layoutFacilityTime = (LinearLayout) root.findViewById(R.id.layoutDoctorFacilityTime);
            List<OpeningHour> openingHours = entity.getOpeningHours();
            for (int i=0; i<openingHours.size();++i){
                layoutFacilityTime.addView(createFacilityTimeView(openingHours.get(i).getDayOfTheWeek().toString(),
                        getTime(openingHours.get(i))));
            }
        }

        public String getTime(OpeningHour openingHour){
            StringBuilder builder = new StringBuilder();
            builder.append(openingHour.getOpenTime());
            builder.append("-");
            builder.append(openingHour.getCloseTime());
            return  builder.toString();
        }


    }

}
