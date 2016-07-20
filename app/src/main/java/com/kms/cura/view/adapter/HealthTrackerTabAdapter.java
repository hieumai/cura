package com.kms.cura.view.adapter;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kms.cura.view.fragment.HealthTrackerFragment;
import com.kms.cura.view.fragment.HealthTrackerTabFragment;

import java.util.ArrayList;

/**
 * Created by toanbnguyen on 6/15/2016.
 */
public class HealthTrackerTabAdapter extends FragmentPagerAdapter {

    public static String title[] = {"Current", "Past"};
    private static final int FRAGMENT_NUM = 2;
    private ArrayList<String> currentHealth, pastHealth;
    HealthTrackerTabFragment currentTab, pastTab;

    public HealthTrackerTabAdapter(FragmentManager fragmentManager, ArrayList<String> currentHealth, ArrayList<String> pastHealth) {
        super(fragmentManager);
        this.currentHealth = currentHealth;
        this.pastHealth = pastHealth;
        currentTab = createFragment(currentHealth, HealthTrackerFragment.STATE_CURRENT);
        pastTab = createFragment(pastHealth, HealthTrackerFragment.STATE_PAST);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == HealthTrackerFragment.STATE_CURRENT) {
            return currentTab;
        }
        if (position == HealthTrackerFragment.STATE_PAST) {
            return pastTab;
        }
        return null;
    }

    public HealthTrackerTabFragment createFragment(ArrayList<String> healthList, int state) {
        HealthTrackerTabFragment fragment = new HealthTrackerTabFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(HealthTrackerFragment.KEY_HEALTH_LIST , healthList);
        args.putInt(HealthTrackerFragment.KEY_STATE, state);
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

    public void resetAdapter(ArrayList<String> currentHealth, ArrayList<String> pastHealth) {
        this.currentHealth = currentHealth;
        this.pastHealth = pastHealth;
        currentTab.resetAdapter(currentHealth);
        pastTab.resetAdapter(pastHealth);
    }
}
