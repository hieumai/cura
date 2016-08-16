package com.kms.cura.view.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.adapter.WorkingTimeAdapter;

import java.util.ArrayList;

public class FacilityInfoActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    public static final String FACILITY_KEY = "facility";
    private FacilityEntity facilityEntity;
    private LinearLayout btnBack;
    private TextView tvName, tvLocation, tvNumber, tvCall, tvDirection, tvDoctorList;
    private FloatingActionButton fbMenu, fbCall, fbDoctor, fbDirection;
    private LinearLayout layoutSubMenu;
    private boolean floatingMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_info);
        String jsonData = getIntent().getStringExtra(FACILITY_KEY);
        facilityEntity = JsonToEntityConverter.convertJsonStringToEntity(jsonData, FacilityEntity.getFacilityType());
        initMap();
        initTextView();
        initButton();
        initMenu();
        initWorkingTime();
        setDimLayout(false);
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.svFacility);
        scrollView.smoothScrollTo(0,0);
    }

    private void initWorkingTime() {
        ListView listView = (ListView) findViewById(R.id.lvWorkingTime);
        WorkingTimeAdapter adapter = new WorkingTimeAdapter(this, R.layout.opening_hour_item, new ArrayList(facilityEntity.getOpeningHours()));
        listView.setAdapter(adapter);
    }

    private void initMenu() {
        fbMenu = setUpFloatingButton(R.id.fbMenu, this);
        layoutSubMenu = (LinearLayout) findViewById(R.id.floatingMenu);
        fbDirection = setUpFloatingButton(R.id.fbDirection, this);
        fbCall = setUpFloatingButton(R.id.fbCall, this);
        fbDoctor = setUpFloatingButton(R.id.fbDoctor, this);
        fbDirection = setUpFloatingButton(R.id.fbDirection, this);
        layoutSubMenu = (LinearLayout) findViewById(R.id.floatingMenu);
        layoutSubMenu.setVisibility(View.INVISIBLE);
    }

    private void showFloatingMenu(boolean show) {
        if (show) {
            layoutSubMenu.setVisibility(View.VISIBLE);
            layoutSubMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_menu_show));
            setDimLayout(true);
        } else {
            layoutSubMenu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_menu_hide));
            setDimLayout(false);
        }
    }

    private FloatingActionButton setUpFloatingButton(int id, View.OnClickListener listener) {
        FloatingActionButton button = (FloatingActionButton) findViewById(id);
        button.setOnClickListener(listener);
        return button;
    }

    private void initTextView() {
        tvName = setUpTextView(R.id.tvFacilityName, facilityEntity.getName());
        tvLocation = setUpTextView(R.id.tvLocation, DataUtils.showUnicode(facilityEntity.getAddress()));
        tvNumber = setUpTextView(R.id.tvPhone, facilityEntity.getPhone());
        tvCall = (TextView) findViewById(R.id.tvCall);
        tvDirection = (TextView) findViewById(R.id.tvDirection);
        tvDoctorList = (TextView) findViewById(R.id.tvDoctorList);
    }

    private TextView setUpTextView(int id, String text) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(text);
        return tv;
    }

    private void initButton() {
        btnBack = (LinearLayout) findViewById(R.id.btnFacilityBack);
        btnBack.setOnClickListener(this);
        tvCall.setOnClickListener(this);
        tvDoctorList.setOnClickListener(this);
        tvDirection.setOnClickListener(this);
    }

    private void initMap() {
        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.flFacilityMap, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fbMenu) {
            floatingMenu = !floatingMenu;
            showFloatingMenu(floatingMenu);
        } else if (v.getId() == R.id.btnFacilityBack) {
            finish();
        } else if (v.getId() == R.id.fbCall || v.getId() == R.id.tvCall) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(facilityEntity.getPhone()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                ErrorController.showDialog(this, getString(R.string.call_error));
            }
        } else if (v.getId() == R.id.fbDoctor || v.getId() == R.id.tvDoctorList) {
            // show doctors list
        } else if (v.getId() == R.id.fbDirection || v.getId() == R.id.tvDirection) {
            // intent Ggl map find direction
        } else if (v.getId() == R.id.dimLayout1 || v.getId() == R.id.dimLayout2) {
            if (floatingMenu) {
                floatingMenu = false;
                showFloatingMenu(floatingMenu);
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        LatLng latLng = new LatLng(facilityEntity.getLatitude(), facilityEntity.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(facilityEntity.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    private void setDimLayout(boolean dim) {
        View layout1 = findViewById(R.id.dimLayout1);
        View layout2 = findViewById(R.id.dimLayout2);
        if (dim) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout1.setOnClickListener(this);
            layout2.setOnClickListener(this);
        } else {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
        }
    }
}
