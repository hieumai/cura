package com.kms.cura.view.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.UserModel;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.adapter.HealthTrackerTabAdapter;

import java.util.ArrayList;
import java.util.List;

public class HealthTrackerFragment extends Fragment {

    private ArrayList<HealthEntity> currentHealthEntities, pastHealthEntities;
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
        setupTabView(myFragmentView);
        List<HealthEntity> entities = ((PatientUserEntity) (CurrentUserProfile.getInstance().getEntity())).getHealthEntities();
        getDataFromListAll(entities);
        setupDataOnView();
        getActivity().setTitle(getString(R.string.health_tracker));

        return myFragmentView;
    }

    private void setupTabView(View parent) {
        viewPager = (ViewPager) parent.findViewById(R.id.tab_view);
        tabLayout = (TabLayout) parent.findViewById(R.id.tab_layout);
    }

    private void setupDataOnView() {
        adapter = new HealthTrackerTabAdapter(getChildFragmentManager(), currentHealth, pastHealth);
        viewPager.setAdapter(adapter);
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

    private ArrayList<String> getNameList(List<HealthEntity> list) {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            names.add(list.get(i).getName());
        }
        return names;
    }

    private ArrayList<HealthEntity> getCurrentHealth(List<HealthEntity> list) {
        ArrayList<HealthEntity> current = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isPastHealth()) {
                current.add(list.get(i));
            }
        }
        return current;
    }

    private ArrayList<HealthEntity> getPastHealth(List<HealthEntity> list) {
        ArrayList<HealthEntity> past = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isPastHealth()) {
                past.add(list.get(i));
            }
        }
        return past;
    }

    private void getDataFromListAll(List<HealthEntity> entities) {
        currentHealthEntities = getCurrentHealth(entities);
        pastHealthEntities = getPastHealth(entities);
        currentHealth = getNameList(currentHealthEntities);
        pastHealth = getNameList(pastHealthEntities);
    }
}
