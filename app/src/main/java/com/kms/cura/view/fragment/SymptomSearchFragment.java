package com.kms.cura.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.SymptomController;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.view.activity.ConditionSymptomSearchActivity;
import com.kms.cura.view.activity.PatientViewActivity;
import com.kms.cura.view.adapter.FilterAdapter;

import java.util.ArrayList;
import java.util.List;

public class SymptomSearchFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {

    private List<SymptomEntity> symptomEntities;
    private ListView lvSymptom;
    private FilterAdapter adapter;;
    private LinearLayout btnSearchCondition;
    private ImageButton btnBack;
    private EditText edtSearch;
    private ImageView btnClearSearch;
    public SymptomSearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_symptom_search, container, false);
        initSearchField(myFragmentView);
        initButton(myFragmentView, this);
        symptomEntities = SymptomController.getAllSymptom();
        setUpDataOnView();
        setTitle();
        return myFragmentView;
    }

    private void initSearchField(View parent) {
        lvSymptom = (ListView) parent.findViewById(R.id.lvSymptom);
        lvSymptom.setOnItemClickListener(this);
        edtSearch = (EditText) parent.findViewById(R.id.edtHealthSearch);
        edtSearch.addTextChangedListener(this);
        btnClearSearch = (ImageView) parent.findViewById(R.id.btnClearSearch);
        btnClearSearch.setOnClickListener(this);
        btnClearSearch.setVisibility(View.INVISIBLE);
    }

    private void initButton(View myFragmentView, View.OnClickListener listener) {
        btnSearchCondition = (LinearLayout) myFragmentView.findViewById(R.id.btnSeachCondition);
        btnSearchCondition.setOnClickListener(listener);
        btnBack = (ImageButton) getActivity().findViewById(R.id.btnHealthBack);
        btnBack.setOnClickListener(this);
    }

    private void setTitle() {
        TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvHealthTitle);
        tvTitle.setText(getString(R.string.symptom));
    }
    private void setUpDataOnView() {
        adapter = new FilterAdapter(getActivity(), R.layout.string_list_item2, getNameList(symptomEntities));
        lvSymptom.setAdapter(adapter);
    }

    private ArrayList<String> getNameList(List<SymptomEntity> symptomEntities) {
        ArrayList<String> nameStrings = new ArrayList<>();
        for (int i = 0; i < symptomEntities.size(); ++i) {
            nameStrings.add(symptomEntities.get(i).getName());
        }
        return nameStrings;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSeachCondition) {
            changeToFragment(new ConditionSearchFragment());
        } else if (v.getId() == R.id.btnHealthBack) {
            backToMain();
        } else if (v.getId() == R.id.btnClearSearch) {
            edtSearch.setText("");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SymptomEntity entity = symptomEntities.get((int) adapter.getItemId(position));
        Toast.makeText(getActivity(), entity.getName(), Toast.LENGTH_SHORT).show();
    }

    private void changeToFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fragmentHolder, fragment);
        ft.commit();
    }

    private void backToMain() {
        Intent intent = new Intent(getActivity(), PatientViewActivity.class);
        intent.putExtra(PatientViewActivity.NAVIGATION_KEY, ConditionSymptomSearchActivity.TO_HEALTH_TRACKER);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (edtSearch.getText().toString().equals("")) {
            btnClearSearch.setVisibility(View.INVISIBLE);
            adapter.getFilter().filter(edtSearch.getText().toString());
        } else {
            btnClearSearch.setVisibility(View.VISIBLE);
            adapter.getFilter().filter(edtSearch.getText().toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
