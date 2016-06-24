package com.kms.cura.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.adapter.WorkingHourExpandableAdapter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorProfileViewActivity extends AppCompatActivity {
    private Toolbar tbDoctorProfileView;
    private TextView txtName, txtDegree, txtSpeciality, txtYearExperience, txtPriceRange, txtGender;
    private LinearLayout facilityLayout;
    private ExpandableListView listWorkingHour;
    private RatingBar ratingBar;
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
        DoctorUserEntity doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
        txtName = loadText(doctorUserEntity.getName(), R.id.txtDoctorName);
        txtDegree = loadText(doctorUserEntity.getDegree().getName(), R.id.txtDoctorDegree);
        txtSpeciality = loadText(getSpecialityName(doctorUserEntity), R.id.txtDoctorSpecialties);
        txtGender = loadText(getGender(doctorUserEntity), R.id.txtDoctorGender);
        txtPriceRange = loadText(doctorUserEntity.getPriceRange(), R.id.txtDoctorPrice);
        txtYearExperience = loadText(doctorUserEntity.getExperience() + "", R.id.txtDoctorYearExperience);
        ratingBar = initRatingBar(doctorUserEntity);
        facilityLayout = (LinearLayout) findViewById(R.id.layoutFacility);
        List<FacilityEntity> facilityList = doctorUserEntity.getFacility();
        for (int i = 0; i < facilityList.size(); ++i) {
            facilityLayout.addView(createFacilityView(facilityList.get(i)));
        }
        setUpListWorkingHour();
    }

    private void setUpListWorkingHour() {
        listWorkingHour = (ExpandableListView) findViewById(R.id.listWorkingTime);
        List<HashMap<String, OpeningHour>> list = seedData();
        WorkingHourExpandableAdapter adapter = new WorkingHourExpandableAdapter(this, list);
        listWorkingHour.setAdapter(adapter);
        listWorkingHour.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(listWorkingHour, 0);
                return false;
            }
        });
    }

    public RatingBar initRatingBar(DoctorUserEntity entity) {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        if (entity.getRating() == 0) {
            ratingBar.setVisibility(View.INVISIBLE);
        } else {
            ratingBar.setRating((float) entity.getRating());
        }
        return ratingBar;
    }

    private View createFacilityView(FacilityEntity entity) {
        View facility = inflater.inflate(R.layout.facility_layout, null);
        ViewHolder holder = new ViewHolder(facility, entity);
        holder.loadFacility();
        return facility;
    }

    public String getGender(DoctorUserEntity doctorUserEntity) {
        if (doctorUserEntity.getGender() == null) {
            return null;
        }
        if (doctorUserEntity.getGender().equals(DoctorUserEntity.MALE)) {
            return getResources().getString(R.string.male);
        }
        return getResources().getString(R.string.female);
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

        public void loadFacility() {
            txtFacilityName = loadText(entity.getName(), R.id.txtDoctorFacilityName);
            txtFacilityAddress = loadText(DataUtils.showUnicode(entity.getAddress()), R.id.txtDoctorFacilityAddress);
            txtFacilityPhoneNumber = loadText(entity.getPhone(), R.id.txtDoctorFacilityPhoneNumber);
        }


    }


    private List<HashMap<String, OpeningHour>> seedData() {
        List<HashMap<String, OpeningHour>> list = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            HashMap<String, OpeningHour> workingtime = new HashMap<>();
            workingtime.put("A", new OpeningHour(DayOfTheWeek.getDayOfTheWeek(i), new Time(3, 3, 3), new Time(4, 4, 4)));
            workingtime.put("B", new OpeningHour(DayOfTheWeek.getDayOfTheWeek(i), new Time(3, 3, 3), new Time(4, 4, 4)));
            list.add(workingtime);
        }
        return list;
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        int totalHeight = getTotalHeight(listView, listAdapter, group, desiredWidth);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10) {
            height = 200;
        }
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private int getTotalHeight(ExpandableListView listView, ExpandableListAdapter listAdapter, int group, int desiredWidth) {
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }
        return totalHeight;
    }
}
