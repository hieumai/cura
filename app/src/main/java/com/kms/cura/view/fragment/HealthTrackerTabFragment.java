package com.kms.cura.view.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.view.activity.PatientHealthDetailActivity;
import com.kms.cura.view.adapter.HealthListAdapter;

import java.util.ArrayList;
import java.util.List;

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
            navigateToDetailPage(state, position);
        } else if (state == HealthTrackerFragment.STATE_PAST) {
            navigateToDetailPage(state, position);
        }
    }

    private void navigateToDetailPage(int state, int position) {
        Intent intent = new Intent(getActivity(), PatientHealthDetailActivity.class);
        intent.putExtra(PatientHealthDetailActivity.STATE, state);
        intent.putExtra(PatientHealthDetailActivity.KEY_POSITION, position);
        startActivity(intent);
    }

    public void resetAdapter(ArrayList<String> healthList) {
        if (adapter == null) {
            return;
        }
        this.healthList = healthList;
        adapter.resetAdapter(healthList);
        adapter.notifyDataSetChanged();
    }
}
