package com.kms.cura.view.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.view.adapter.HealthListAdapter;

import java.util.ArrayList;

public class HealthTrackerTabFragment extends Fragment implements AdapterView.OnItemClickListener {

    private int state;
    private ArrayList<String> healthList;
    private ListView lvHealth;
    private HealthListAdapter adapter;

    public HealthTrackerTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_health_tracker_tab, container, false);
        setupData();
        setupListView(myFragmentView);
        return myFragmentView;
    }

    private void setupData() {
        Bundle bundle = getArguments();
        healthList = bundle.getStringArrayList(HealthTrackerFragment.KEY_HEALTH_LIST);
        state = bundle.getInt(HealthTrackerFragment.KEY_STATE);
    }

    private void setupListView(View parent) {
        lvHealth = (ListView) parent.findViewById(R.id.lvHealth);
        adapter = new HealthListAdapter(getActivity(), R.layout.health_list_item, healthList);
        lvHealth.setAdapter(adapter);
        lvHealth.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (state == HealthTrackerFragment.STATE_CURRENT) {
            Toast.makeText(getActivity(), "Click " + healthList.get(position) + " at position " + (position + 1), Toast.LENGTH_SHORT).show(); // just for testing UI
            // navigate to Patient's Conditions/Symptoms Details screen

        } else if (state == HealthTrackerFragment.STATE_PAST) {
            Toast.makeText(getActivity(), "Click " + healthList.get(position) + " at position " + (position + 1), Toast.LENGTH_SHORT).show(); // just for testing UI
            // navigate to Patient's Conditions/Symptoms Details screen

        }
    }

}
