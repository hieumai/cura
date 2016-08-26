package com.kms.cura.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.WorkingHourEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.activity.FacilityInfoActivity;
import com.kms.cura.view.adapter.WorkingHourExpandableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by linhtnvo on 6/29/2016.
 */
public class DoctorProfileFragment extends Fragment {
    private TextView txtName, txtDegree, txtSpeciality, txtYearExperience, txtPriceRange, txtGender;
    private LinearLayout facilityLayout;
    private ExpandableListView listWorkingHour;
    private RatingBar ratingBar;
    private LayoutInflater inflater;
    private View root;
    private List<WorkingHourEntity> listWH;
    private ImageView profile;
    private DoctorUserEntity doctorUserEntity;

    public DoctorProfileFragment() {

    }

    public static DoctorProfileFragment newInstance(DoctorUserEntity entity) {
        DoctorProfileFragment fragment = new DoctorProfileFragment();
        fragment.setDoctorUserEntity(entity);
        return fragment;
    }

    public void setDoctorUserEntity(DoctorUserEntity doctorUserEntity) {
        this.doctorUserEntity = doctorUserEntity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = this.inflater.inflate(R.layout.fragment_doctor_profile_view, null);
        loadData();
        if (CurrentUserProfile.getInstance().isDoctor()) {
            modifyToolbar();
        }
        return root;
    }

    public void loadData() {
        if (CurrentUserProfile.getInstance().isDoctor()) {
            doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
        }
        calculateRatingAsyntask().execute();
        DataUtils.loadProfile(doctorUserEntity, (ImageView) root.findViewById(R.id.ivDoctor), this.getContext());
        txtName = loadText(doctorUserEntity.getName(), R.id.txtDoctorName);
        txtDegree = loadText(doctorUserEntity.getDegree().getName(), R.id.txtDoctorDegree);
        txtSpeciality = loadText(getSpecialityName(doctorUserEntity), R.id.txtDoctorSpecialties);
        txtGender = loadText(getGender(doctorUserEntity), R.id.txtDoctorGender);
        txtPriceRange = loadText(doctorUserEntity.getPriceRange(), R.id.txtDoctorPrice);
        txtYearExperience = loadText(doctorUserEntity.getExperience() + " years experience", R.id.txtDoctorYearExperience);
        facilityLayout = (LinearLayout) root.findViewById(R.id.layoutFacility);
        listWH = doctorUserEntity.getWorkingTime();
        for (int i = 0; i < listWH.size(); ++i) {
            facilityLayout.addView(createFacilityView(listWH.get(i).getFacilityEntity()));
        }
        setUpListWorkingHour();
    }

    private void setUpListWorkingHour() {
        listWorkingHour = (ExpandableListView) root.findViewById(R.id.listWorkingTime);
        List<HashMap<String, List<OpeningHour>>> list = convertWorkingHour();
        WorkingHourExpandableAdapter adapter = new WorkingHourExpandableAdapter(getActivity(), list);
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
        RatingBar ratingBar = (RatingBar) root.findViewById(R.id.ratingbar);
        if (entity.getRating() == 0) {
            ratingBar.setVisibility(View.INVISIBLE);
        } else {
            ratingBar.setRating(entity.getRating());
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
        TextView textView = (TextView) root.findViewById(id);
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


    class ViewHolder implements View.OnClickListener {
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
            txtFacilityName.setOnClickListener(this);
            txtFacilityAddress = loadText(DataUtils.showUnicode(entity.getAddress()), R.id.txtDoctorFacilityAddress);
            txtFacilityPhoneNumber = loadText(entity.getPhone(), R.id.txtDoctorFacilityPhoneNumber);
            TextView detail = (TextView) root.findViewById(R.id.btnFacilityDetail);
            detail.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), FacilityInfoActivity.class);
            intent.putExtra(FacilityInfoActivity.FACILITY_KEY, EntityToJsonConverter.convertEntityToJson(entity).toString());
            startActivity(intent);
        }
    }


    private List<HashMap<String, List<OpeningHour>>> convertWorkingHour() {
        List<HashMap<String, List<OpeningHour>>> list = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            HashMap<String, List<OpeningHour>> workingtime = new HashMap<>();
            for (WorkingHourEntity workingHourEntity : listWH) {
                List<OpeningHour> workingHourOfi = new ArrayList<>();
                for (int k = 0; k < workingHourEntity.getWorkingTime().size(); ++k) {
                    if (workingHourEntity.getWorkingTime().get(k).getDayOfTheWeek().getCode() == i) {
                        workingHourOfi.add(workingHourEntity.getWorkingTime().get(k));
                    }
                }
                workingtime.put(workingHourEntity.getFacilityEntity().getName(), workingHourOfi);
            }
            list.add(workingtime);
        }
        return list;
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        int totalHeight = getTotalHeight(listView, listAdapter, group, desiredWidth);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10) {
            height = 200;
        }
        params.height = height + 30;
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

    private void modifyToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.setTitle(getString(R.string.ProfileView));
        toolbar.inflateMenu(R.menu.menu_blank);
    }

    private AsyncTask<Object, Void, Void> calculateRatingAsyntask() {
        return new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            private ProgressDialog pDialog;
            private List<AppointmentEntity> appointmentEntities;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    appointmentEntities = AppointmentController.getAppointment(AppointmentEntity.searchEntityForDoctor(doctorUserEntity));
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
                    doctorUserEntity.setAppointmentList(appointmentEntities);
                    ratingBar = initRatingBar(doctorUserEntity);
                }
            }
        };
    }
}