package com.kms.cura.view.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.FacilityController;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.view.ListViewRemoveItemListener;
import com.kms.cura.view.adapter.FacilitySettingAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingFacilityEditActivity extends AppCompatActivity implements View.OnClickListener, ListViewRemoveItemListener, AdapterView.OnItemClickListener {

    private ImageButton btnCancel, btnDone;
    private ListView lvSpeciality;
    private FacilitySettingAdapter adapter;
    private List<String> facilityEntitiesNameList;
    private List<FacilityEntity> facilityEntitiesAddList, facilityEntitiesList, facilityEntitiesListAll;
    private FloatingActionButton fbAddFacility;
    private Dialog dialog;
    private boolean editted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_facility_edit);
        getFacilityListAll();
    }

    private void initListView() {
        lvSpeciality = (ListView) findViewById(R.id.lvFacilitySetting);
        adapter = new FacilitySettingAdapter(facilityEntitiesList, R.layout.speciality_setting_item, this, this);
        lvSpeciality.setAdapter(adapter);
    }

    private void initButtton() {
        btnCancel = (ImageButton) findViewById(R.id.btnFacilityCancel);
        btnDone = (ImageButton) findViewById(R.id.btnFacilityDone);
        fbAddFacility = (FloatingActionButton) findViewById(R.id.fbAddFacility);
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        fbAddFacility.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFacilityDone) {
            if (editted) {
                showWarningDialog(getString(R.string.change_done), doneListener);
            } else {
                finish();
            }
        } else if (v.getId() == R.id.btnFacilityCancel) {
            onBackPressed();
        } else if (v.getId() == R.id.fbAddFacility) {
            facilityEntitiesAddList = getFacilityEntitiesAddList();
            dialog.show();
        }
    }

    @Override
    public void removeItem(int position) {
        facilityEntitiesList.remove(position);
        adapter.setData(facilityEntitiesList);
        adapter.notifyDataSetChanged();
        editted = true;
    }

    @Override
    public void removeItem(String facility, OpeningHour openingHour) {

    }

    private List<FacilityEntity> getFacilitiesByName() {
        List<FacilityEntity> facilityEntities = new ArrayList<>();
        for (String name : facilityEntitiesNameList) {
            for (FacilityEntity entity : facilityEntitiesListAll) {
                if (name.equals(entity.getName())) {
                    facilityEntities.add(new FacilityEntity(entity));
                }
            }
        }
        return facilityEntities;
    }

    private List<String> getFacilityEntitiesNameList() {
        List<String> names = new ArrayList<>();
        for (FacilityEntity entity : facilityEntitiesList) {
            names.add(entity.getName());
        }
        return names;
    }

    private void getFacilityListAll() {
        loadFacilityList().execute();
    }

    private List<FacilityEntity> getFacilityEntitiesAddList() {
        List<FacilityEntity> listAll = facilityEntitiesListAll;
        for (FacilityEntity entity : facilityEntitiesList) {
            int index = getIndex(listAll, entity);
            if (index != -1) {
                listAll.remove(index);
            }
        }
        return listAll;
    }

    private AsyncTask<Object, Void, Void> loadFacilityList() {
        return new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(SettingFacilityEditActivity.this);
                pDialog.setMessage(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    FacilityController.initData();
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pDialog.dismiss();
                if (exception != null) {
                    ErrorController.showDialog(SettingFacilityEditActivity.this, "Error : " + exception.getMessage());
                } else {
                    setUpData();
                }
            }
        };
    }

    private void setUpData() {
        facilityEntitiesListAll = FacilityController.getAllFacilities();
        facilityEntitiesNameList = getIntent().getStringArrayListExtra(SettingWorkingHourActivity.FACILITIES_KEY);
        facilityEntitiesList = getFacilitiesByName();
        facilityEntitiesAddList = getFacilityEntitiesAddList();
        initButtton();
        initListView();
        setUpDialog();
    }

    private void setUpDialog() {
        dialog = new Dialog(this);
        dialog.setTitle(R.string.add_speciality);
        FrameLayout layout = new FrameLayout(this);
        ListView listView = new ListView(this);
        FacilitySettingAdapter adapter = new FacilitySettingAdapter(facilityEntitiesAddList, R.layout.speciality_setting_item, this, null);
        listView.setAdapter(adapter);
        layout.addView(listView);
        dialog.setContentView(layout);
        dialog.setCancelable(true);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        facilityEntitiesList.add(facilityEntitiesAddList.get(position));
        adapter.setData(facilityEntitiesList);
        adapter.notifyDataSetChanged();
        dialog.dismiss();
        editted = true;
    }

    private int getIndex(List<FacilityEntity> entities, FacilityEntity entity) {
        for (int i = 0; i < entities.size(); ++i) {
            if (entities.get(i).getId().equals(entity.getId())) {
                return i;
            }
        }
        return -1;
    }

    private void showWarningDialog(String mes, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warning);
        builder.setMessage(mes);
        builder.setPositiveButton(R.string.yes, listener);
        builder.setNegativeButton(R.string.no, listener);
        builder.create().show();
    }

    private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finish();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.dismiss();
            }
        }
    };

    private DialogInterface.OnClickListener doneListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                // update
                // * remove ? -> remove working hour of this facility
                // * add
                finish();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.dismiss();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (editted) {
            showWarningDialog(getString(R.string.change_discard), cancelListener);
        } else {
            finish();
        }
    }
}
