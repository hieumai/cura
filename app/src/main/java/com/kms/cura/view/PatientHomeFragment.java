package com.kms.cura.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.model.Settings;
import com.kms.cura.utils.GPSTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PatientHomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private EditText edtName, edtLocation;
    private RadioGroup rdbtngroupLocation;
    private RadioButton rdbtnCurrentLocation, rdbtnManualEnter;
    private Spinner SpnSpeciality;
    private Button btnRegister;
    private Context mContext;
    private Activity activity;
    private String currentLocation = null;
    private boolean checked = true;


    public PatientHomeFragment() {
        // Required empty public constructor
    }

    public static PatientHomeFragment newInstance(Context mContext, Activity activity) {
        PatientHomeFragment fragment = new PatientHomeFragment();
        fragment.setContext(mContext);
        fragment.setActivity(activity);
        return fragment;
    }

    private void setContext(Context src) {
        this.mContext = src;
    }

    private void setActivity(Activity src) {
        this.activity = src;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_home, container, false);
        initView(root);
        return root;
    }

    public void initView(View root) {
        edtName = (EditText) root.findViewById(R.id.edtName);
        edtLocation = (EditText) root.findViewById(R.id.edtLocation);
        rdbtngroupLocation = (RadioGroup) root.findViewById(R.id.rdbtngroupLoacation);
        rdbtngroupLocation.setOnCheckedChangeListener(PatientHomeFragment.this);
        rdbtnCurrentLocation = initRadioButton(root, R.id.rdbtnCurrentLocation, 1);
        rdbtnManualEnter = initRadioButton(root, R.id.rdbtnManuallyEnter, 2);
    }

    public RadioButton initRadioButton(View root, int id, int newId) {
        RadioButton tmp = (RadioButton) root.findViewById(id);
        tmp.setId(newId);
        return tmp;
    }

    public void initButton(View root){
        btnRegister = (Button) root.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checked){
                    currentLocation = edtLocation.getText().toString();
                }
                //Search Function implement here
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case 1:
                edtLocation.setEnabled(false);
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {
                    requestPermission();
                } else {
                    getCurrentLocation();
                }
                break;
            case 2:
                checked = false;
                edtLocation.setEnabled(true);
                break;
            default:
                break;
        }
    }

    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            StringBuilder builder = new StringBuilder();
            builder.append(addresses.get(0).getLocality());
            builder.append(", ");
            builder.append(addresses.get(0).getCountryName());
            return builder.toString();
        } catch (IOException e) {
            ErrorController.LocationError(mContext, mContext.getResources().getString(R.string.AddressError));
            return null;
        }
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Settings.MY_PERMISSION_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Settings.MY_PERMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                return;
            }
        }
    }

    public void getCurrentLocation() {
        GPSTracker gpsTracker = new GPSTracker(mContext);
        if (gpsTracker.canGetLocation()) {
            if (!gpsTracker.isError()) {
                currentLocation = getAddress(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                edtLocation.setText(currentLocation);
                return;
            }
            ErrorController.showDialog(mContext, gpsTracker.getErrorMessage());
            return;
        }
        ErrorController.showDialog(mContext, mContext.getResources().getString(R.string.LocationError));
    }
}
