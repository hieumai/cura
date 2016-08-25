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
import com.kms.cura.controller.SpecialityController;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.ListViewRemoveItemListener;
import com.kms.cura.view.adapter.SpecialitySettingAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingSpecialityActivity extends AppCompatActivity implements View.OnClickListener, ListViewRemoveItemListener, AdapterView.OnItemClickListener {

    private ImageButton btnCancel, btnDone;
    private ListView lvSpeciality;
    private DoctorUserEntity doctorUserEntity;
    private SpecialitySettingAdapter adapter;
    private List<SpecialityEntity> specialityEntitiesList, specialityEntitiesAddList;
    private FloatingActionButton fbAddSpeciality;
    private Dialog dialog;
    private boolean editted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_speciality);
        doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
        specialityEntitiesList = new ArrayList<>(doctorUserEntity.getSpeciality());
        specialityEntitiesAddList = getSpecialityEntitiesAddList();
        initButtton();
        initListView();
        setUpDialog();
    }

    private void initListView() {
        lvSpeciality = (ListView) findViewById(R.id.lvSpecialitySetting);
        adapter = new SpecialitySettingAdapter(specialityEntitiesList, R.layout.speciality_setting_item, this, this);
        lvSpeciality.setAdapter(adapter);
    }

    private void initButtton() {
        btnCancel = (ImageButton) findViewById(R.id.btnSpecialityCancel);
        btnDone = (ImageButton) findViewById(R.id.btnSpecialityDone);
        fbAddSpeciality = (FloatingActionButton) findViewById(R.id.fbAddSpeciality);
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        fbAddSpeciality.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSpecialityDone) {
            if (editted) {
                showWarningDialog(getString(R.string.change_done), doneListener);
            } else {
                finish();
            }
        } else if (v.getId() == R.id.btnSpecialityCancel) {
            onBackPressed();
        } else if (v.getId() == R.id.fbAddSpeciality) {
            specialityEntitiesAddList = getSpecialityEntitiesAddList();
            dialog.show();
        }
    }

    @Override
    public void removeItem(int position) {
        specialityEntitiesList.remove(position);
        adapter.setData(specialityEntitiesList);
        adapter.notifyDataSetChanged();
        editted = true;
    }

    @Override
    public void removeItem(String facility, OpeningHour openingHour) {

    }

    private List<SpecialityEntity> getSpecialityListAll() {
        if (SpecialityController.isDataLoaded()) {
            return SpecialityController.getAllSpecialities();
        }
        loadSpecialityList().execute();
        return SpecialityController.getAllSpecialities();
    }

    private List<SpecialityEntity> getSpecialityEntitiesAddList() {
        List<SpecialityEntity> listAll = getSpecialityListAll();
        for (SpecialityEntity entity : specialityEntitiesList) {
            int index = getIndex(listAll, entity);
            if (index != -1) {
                listAll.remove(index);
            }
        }
        return listAll;
    }

    private AsyncTask<Object, Void, Void> loadSpecialityList() {
        return new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(SettingSpecialityActivity.this);
                pDialog.setMessage(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    SpecialityController.initData();
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pDialog.dismiss();
                if (exception != null) {
                    ErrorController.showDialog(SettingSpecialityActivity.this, "Error : " + exception.getMessage());
                }
            }
        };
    }

    private void setUpDialog() {
        dialog = new Dialog(this);
        dialog.setTitle(R.string.add_speciality);
        FrameLayout layout = new FrameLayout(this);
        ListView listView = new ListView(this);
        SpecialitySettingAdapter adapter = new SpecialitySettingAdapter(specialityEntitiesAddList, R.layout.speciality_setting_item, this, null);
        listView.setAdapter(adapter);
        layout.addView(listView);
        dialog.setContentView(layout);
        dialog.setCancelable(true);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        specialityEntitiesList.add(specialityEntitiesAddList.get(position));
        adapter.setData(specialityEntitiesList);
        adapter.notifyDataSetChanged();
        dialog.dismiss();
        editted = true;
    }

    private int getIndex(List<SpecialityEntity> entities, SpecialityEntity entity) {
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
                // request
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

