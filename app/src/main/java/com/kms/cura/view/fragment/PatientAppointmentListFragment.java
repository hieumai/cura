package com.kms.cura.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.view.adapter.PatientAppointmentListTabAdapter;

import java.util.List;

/**
 * Created by linhtnvo on 7/8/2016.
 */
public class PatientAppointmentListFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PatientAppointmentListTabAdapter adapter;
    public static final int STATE_UPCOMING = 0;
    public static final int STATE_PAST = 1;
    public static final String KEY_STATE = "state";

    public PatientAppointmentListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        setupTabView(myFragmentView);
        setupDataOnView();
        modifyToolbar();
        return myFragmentView;
    }

    private void setupTabView(View parent) {
        viewPager = (ViewPager) parent.findViewById(R.id.tab_view);
        tabLayout = (TabLayout) parent.findViewById(R.id.tab_layout);
    }

    private void setupDataOnView() {
        adapter = new PatientAppointmentListTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void modifyToolbar() {
        Toolbar healthTrackerToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        healthTrackerToolbar.getMenu().clear();
        healthTrackerToolbar.setTitle(getString(R.string.Appointment));
    }

}
