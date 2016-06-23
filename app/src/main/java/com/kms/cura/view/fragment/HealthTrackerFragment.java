package com.kms.cura.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.view.adapter.HealthTrackerTabAdapter;

import java.util.ArrayList;

public class HealthTrackerFragment extends Fragment {

    private ArrayList<String> currentHealth, pastHealth;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HealthTrackerTabAdapter adapter;
    public static final int STATE_CURRENT = 0;
    public static final int STATE_PAST = 1;
    public static final String KEY_STATE = "state";
    public static final String KEY_HEALTH_LIST = "healthlist";

    public HealthTrackerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_health_tracker, container, false);
        // get data from somewhere
        // just dummy data for now
        currentHealth = new ArrayList<>();
        currentHealth.add("Condition 1");
        currentHealth.add("Condition 2");
        currentHealth.add("Condition 3");
        currentHealth.add("Condition 4");
        currentHealth.add("Condition 5");
        currentHealth.add("Symptom 1");
        currentHealth.add("Symptom 2");
        currentHealth.add("Symptom 3");
        currentHealth.add("Condition 6");
        currentHealth.add("Condition 7");
        currentHealth.add("Condition 8");
        currentHealth.add("Condition 9");
        currentHealth.add("Symptom 4");
        currentHealth.add("Symptom 5");
        currentHealth.add("Condition 10");
        currentHealth.add("Condition 11");
        pastHealth = new ArrayList<>();
        pastHealth.add("Condition 12");
        pastHealth.add("Condition 13");
        pastHealth.add("Symptom 6");
        pastHealth.add("Symptom 7");
        pastHealth.add("Symptom 8");
        pastHealth.add("Condition 14");
        pastHealth.add("Condition 15");
        pastHealth.add("Symptom 9");
        pastHealth.add("Symptom 10");
        // ---------------------------------------
        getActivity().setTitle(getString(R.string.health_tracker));
        setHasOptionsMenu(true);
        setupTabView(myFragmentView);
        return myFragmentView;
    }

    private void setupTabView(View parent) {
        adapter = new HealthTrackerTabAdapter(getFragmentManager(), currentHealth, pastHealth);
        viewPager = (ViewPager) parent.findViewById(R.id.tab_view);
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) parent.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_health_tracker, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnAdd) {
            Toast.makeText(getActivity(), "Add Condition/Symptom", Toast.LENGTH_SHORT).show();
            // navigate to Conditions/Symptoms search screen

        }
        return true;
    }

    private void resetAdapter(ArrayList<String> currentHealth, ArrayList<String> pastHealth) {
        adapter.resetAdapter(currentHealth, pastHealth);
        adapter.notifyDataSetChanged();
    }
}
