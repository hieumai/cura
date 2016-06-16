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

import java.util.List;

public class DoctorProfileViewActivity extends AppCompatActivity {
    private Toolbar tbDoctorProfileView;
    private TextView txtName, txtDegree, txtSpeciality, txtYearExperience, txtPriceRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_view);
        initToolBar();
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

    }


    public View createFacilityView(FacilityEntity entity) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.facility_layout, null);
        ViewHolder holder = new ViewHolder(newView, entity);
        return newView;
    }

    class ViewHolder {
        public TextView txtFacilityName, txtFacilityAddress, txtFacilityPhoneNumber, txtFacilityTime;
        public LinearLayout layoutDoctorFacilityTime;

        public ViewHolder(View root, FacilityEntity entity) {
            txtFacilityName = loadText(root,entity.getName(), R.id.txtDoctorFacilityName);
            txtFacilityAddress = loadText(root,entity.getAddress(), R.id.txtDoctorFacilityAddress);
            txtFacilityPhoneNumber = loadText(root,entity.getPhone(), R.id.txtDoctorFacilityPhoneNumber);
            txtFacilityTime = loadFacilityTime(entity.getOpeningHours(),R.id.txtDoctorFacilityName, root);
        }

        public TextView loadText(View root, String src, int id) {
            TextView textView = (TextView) root.findViewById(id);
            if (src == null) {
                textView.setHeight(0);
            } else {
                textView.setText(src);
            }
            return textView;
        }

        public TextView loadFacilityTime(List<OpeningHour> times, int id, View root) {
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
