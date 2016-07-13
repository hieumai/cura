package com.kms.cura.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.kms.cura.view.fragment.PatientAppointmentListFragment;
import com.kms.cura.view.fragment.PatientAppointmentListTabFragment;

import android.support.v4.app.FragmentPagerAdapter;
/**
 * Created by linhtnvo on 7/12/2016.
 */
public class PatientAppointmentListTabAdapter extends FragmentPagerAdapter{
    public static String title[] = {"Upcoming", "Past"};
    private static final int FRAGMENT_NUM = 2;


    public PatientAppointmentListTabAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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

}
