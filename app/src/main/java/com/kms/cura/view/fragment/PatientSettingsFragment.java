package com.kms.cura.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.kms.cura.R;
import com.kms.cura.view.activity.ChangePasswordActivity;
import com.kms.cura.view.activity.PatientBasicSettingsActivity;
import com.kms.cura.view.adapter.StringSexListAdapter;

import java.util.ArrayList;


public class PatientSettingsFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private static final int BASIC_SETTINGS = 0;
    private static final int CHANGE_PASSWORD = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_list, container, false);
        modifyToolbar();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] str = getResources().getStringArray(R.array.Settings);
        ArrayList<String> settings = new ArrayList<>();
        for (int i = 0; i < str.length; ++i) {
            settings.add(str[i]);
        }
        StringSexListAdapter adapter = new StringSexListAdapter(getActivity(), R.layout.string_list_item2, settings);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    private void changeActivity(Class className) {
        Intent intent = new Intent(getActivity(), className);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == BASIC_SETTINGS) {
            changeActivity(PatientBasicSettingsActivity.class);
        } else if (position == CHANGE_PASSWORD) {
            changeActivity(ChangePasswordActivity.class);
        }
    }

    private void modifyToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.setTitle(getString(R.string.settings));
        toolbar.inflateMenu(R.menu.menu_blank);
    }
}
