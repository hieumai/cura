package com.kms.cura.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

import com.kms.cura.view.fragment.DummyAppointment;
import com.kms.cura.view.fragment.PatientAppointmentListFragment;
import com.kms.cura.view.fragment.PatientAppointmentListTabFragment;

import android.support.v4.app.FragmentPagerAdapter;
/**
 * Created by linhtnvo on 7/12/2016.
 */
public class AppointmentListTabAdapter extends FragmentPagerAdapter{
    public static String title[] = {"Upcoming", "Past"};
    private static final int FRAGMENT_NUM = 2;
    private List<DummyAppointment> upcomingAppts, pastAppts;

    public AppointmentListTabAdapter(FragmentManager fragmentManager, List<DummyAppointment> upcomingAppts, List<DummyAppointment> pastAppts) {
        super(fragmentManager);
        this.upcomingAppts = upcomingAppts;
        this.pastAppts = pastAppts;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == PatientAppointmentListFragment.STATE_UPCOMING) {
            return createFragment(PatientAppointmentListFragment.STATE_UPCOMING);
        }
        if (position == PatientAppointmentListFragment.STATE_PAST) {
            return createFragment(PatientAppointmentListFragment.STATE_PAST);
        }
        return null;
    }

    public Fragment createFragment(int state) {
        Fragment fragment = new PatientAppointmentListTabFragment();
        Bundle args = new Bundle();
        args.putInt(PatientAppointmentListFragment.KEY_STATE, state);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return FRAGMENT_NUM;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    public void resetAdapter(List<DummyAppointment> upcomingAppts, List<DummyAppointment> pastAppts) {
        this.upcomingAppts = upcomingAppts;
        this.pastAppts = pastAppts;
    }
}
