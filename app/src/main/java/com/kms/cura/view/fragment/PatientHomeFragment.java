package com.kms.cura.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.SpecialityController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.model.Settings;
import com.kms.cura.model.SpecialityModel;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.utils.GPSTracker;
import com.kms.cura.view.ReloadData;
import com.kms.cura.view.UpdateSpinner;
import com.kms.cura.view.activity.PatientViewActivity;
import com.kms.cura.view.activity.SearchActivity;
import com.kms.cura.view.adapter.CheckBoxAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PatientHomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, UpdateSpinner, ReloadData, EventHandler {
    private static final String FRAGMENT_NAME = "Home";
    private EditText edtName, edtLocation;
    private RadioGroup rdbtngroupLocation;
    private RadioButton rdbtnCurrentLocation, rdbtnManualEnter;
    private Spinner spnSpeciality;
    private Button btnSearch;
    private Context mContext;
    private Activity activity;
    private String currentLocation = null;
    private boolean checked = true;
    private ArrayList<String> speciality;
    private UpdateSpinner updateSpinner;
    private CheckBoxAdapter specialityAdapter;
    private boolean[] specialitySelected = null; // it stores which specialities are chosen
    private final int CURRENT_LOCATION = 1;
    private final int MANUALLY_ENTER = 2;
    private GPSTracker gpsTracker;
    private ProgressDialog pDialog;
    private boolean[] checkedSpeciality;
    private ReloadData reloadData;
    private EventBroker broker;
    private String HINT_TEXT = "Please choose";
    public static String SEARCH_RESULT = "SearchResult";


    public PatientHomeFragment() {
        // Required empty public constructor
        setArguments(new Bundle());
    }

    public static PatientHomeFragment newInstance(Context mContext, Activity activity) {
        PatientHomeFragment fragment = new PatientHomeFragment();
        fragment.setContext(mContext);
        fragment.setActivity(activity);
        return fragment;
    }

    private void setContext(Context src) {
        this.mContext = getContext();
    }

    private void setActivity(Activity src) {
        this.activity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_home, container, false);
        broker = EventBroker.getInstance();
        updateSpinner = this;
        reloadData = this;
        initView(root);
        setUpSpnSpeciality();
        modifyToolbar();
        initButton(root);
        getActivity().setTitle(FRAGMENT_NAME);
        return root;
    }

    public void initView(View root) {
        edtName = (EditText) root.findViewById(R.id.edtName);
        spnSpeciality = (Spinner) root.findViewById(R.id.spnSpeciality);
        edtLocation = (EditText) root.findViewById(R.id.edtLocation);
        rdbtngroupLocation = (RadioGroup) root.findViewById(R.id.rdbtngroupLoacation);
        rdbtngroupLocation.setOnCheckedChangeListener(PatientHomeFragment.this);
        rdbtnCurrentLocation = initRadioButton(root, R.id.rdbtnCurrentLocation, 1);
        rdbtnManualEnter = initRadioButton(root, R.id.rdbtnManuallyEnter, 2);
        rdbtngroupLocation.check(MANUALLY_ENTER);
    }

    public RadioButton initRadioButton(View root, int id, int newId) {
        RadioButton radioButton = (RadioButton) root.findViewById(id);
        radioButton.setId(newId);
        return radioButton;
    }

    public void initButton(View root) {
        btnSearch = (Button) root.findViewById(R.id.button_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checked) {
                    currentLocation = edtLocation.getText().toString();
                }
                showProgressDialog();
                UserController.doctorSearch(edtName.getText().toString(), getCityFromLocation(currentLocation), specialityAdapter.getSelectedString());
            }
        });
    }

    public void setUpSpnSpeciality() {
        //Place holder for speciality
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        showProgressDialog();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

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
                hideProgressDialog();
                if (exception != null) {
                    ErrorController.showDialogRefresh(getActivity(), "Error : " + exception.getMessage(), reloadData);
                    return;
                }
                setAdapter();
            }
        };
        task.execute();
    }

    public void setAdapter() {

        speciality = (ArrayList<String>) DataUtils.getListName(SpecialityModel.getInstace().getSpecialities());
        speciality.add(HINT_TEXT);
        reformData();
        specialityAdapter = new CheckBoxAdapter(getActivity(), R.layout.check_box_item, speciality, specialitySelected, updateSpinner);
        spnSpeciality.setAdapter(specialityAdapter);
        spnSpeciality.setSelection(specialityAdapter.getCount());

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case CURRENT_LOCATION:
                edtLocation.setEnabled(false);
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {
                    requestPermission();
                } else {
                    setCurrentLocation();
                }
                break;
            case MANUALLY_ENTER:
                if (gpsTracker != null) {
                    gpsTracker.stopUsingGPS();
                }
                checked = false;
                edtLocation.setEnabled(true);
                edtLocation.setText("");
                break;
            default:
                break;
        }
    }

    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() == 0) {
                ErrorController.showDialog(getActivity(), getContext().getResources().getString(R.string.AddressError));
                return null;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(addresses.get(0).getLocality());
            builder.append(", ");
            builder.append(addresses.get(0).getCountryName());
            return builder.toString();
        } catch (IOException e) {
            ErrorController.showDialog(getActivity(), getContext().getResources().getString(R.string.AddressError));
            return null;
        }
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setCurrentLocation();
            return;
        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Settings.MY_PERMISSION_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Settings.MY_PERMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCurrentLocation();
                }
                return;
            }
        }
    }

    public void setCurrentLocation() {
        gpsTracker = new GPSTracker(getContext());
        if (gpsTracker.canGetLocation()) {
            loadCurrentLocation(gpsTracker);
            return;
        }
        ErrorController.showDialog(getActivity(), getContext().getResources().getString(R.string.LocationError));
    }

    @Override
    public void callBackUpdateSpinner() {
        specialityAdapter = new CheckBoxAdapter(getActivity(), R.layout.check_box_item, speciality, specialityAdapter.getSelectedBoolean(), updateSpinner);
        spnSpeciality.setAdapter(specialityAdapter);
        spnSpeciality.setSelection(specialityAdapter.getCount());
    }

    public void loadCurrentLocation(GPSTracker gpsTracker) {
        Location location = gpsTracker.getLocation();
        if (location == null) {
            ErrorController.showDialog(getActivity(), getContext().getResources().getString(R.string.LocationError));
            return;
        }
        currentLocation = getAddress(location.getLatitude(), location.getLongitude());
        edtLocation.setText(currentLocation);
    }


    @Override
    public void callBackReload() {
        setUpSpnSpeciality();
    }


    private void reformData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            specialitySelected = bundle.getBooleanArray(PatientViewActivity.PATIENT);
        } else {
            specialitySelected = null;
        }
    }


    private Bundle createBundle() {
        Bundle bundle = getArguments();
        bundle.putBooleanArray(PatientViewActivity.PATIENT, specialityAdapter.getSelectedBoolean());

        return bundle;
    }

    @Override
    public void onDestroyView() {
        createBundle();
        super.onDestroyView();
    }

    private void modifyToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.setTitle(getString(R.string.DoctorSearch));
        toolbar.inflateMenu(R.menu.menu_blank);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void handleEvent(String event, Object data) {
        switch (event) {
            case EventConstant.SEARCH_SUCCESS:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(SEARCH_RESULT, EntityToJsonConverter.convertEntityListToJson((List<DoctorUserEntity>) data).toString());
                hideProgressDialog();
                startActivity(intent);

                break;
            case EventConstant.LOGIN_FAILED:
                hideProgressDialog();
                ErrorController.showDialog(getActivity(), "Login failed :" + data);
                break;
            case EventConstant.CONNECTION_ERROR:
                hideProgressDialog();
                if (data != null) {
                    ErrorController.showDialog(getActivity(), "Error " + data);
                } else {
                    ErrorController.showDialog(getActivity(), "Error " + getResources().getString(R.string.ConnectionError));
                }
                break;
            case EventConstant.INTERNAL_ERROR:
                hideProgressDialog();
                String internalError = getResources().getString(R.string.InternalError);
                ErrorController.showDialog(getActivity(), internalError + " : " + data);
        }
    }

    public void registerEvent() {
        broker.register(this, EventConstant.SEARCH_SUCCESS);
        broker.register(this, EventConstant.LOGIN_FAILED);
        broker.register(this, EventConstant.CONNECTION_ERROR);
        broker.register(this, EventConstant.INTERNAL_ERROR);
    }

    public void unregisterEvent() {
        broker.unRegister(this, EventConstant.SEARCH_SUCCESS);
        broker.unRegister(this, EventConstant.LOGIN_FAILED);
        broker.unRegister(this, EventConstant.CONNECTION_ERROR);
        broker.unRegister(this, EventConstant.INTERNAL_ERROR);
    }

    @Override
    public void onPause() {
        unregisterEvent();
        super.onPause();
    }

    @Override
    public void onResume() {
        registerEvent();
        super.onResume();
    }

    private String getCityFromLocation(String location) {
        String data;
        List<String> list = new ArrayList<String>(Arrays.asList(location.split(",")));
        data = list.get(0).replaceAll("City", "");
        data = data.replaceAll("city", "");
        data = data.replaceAll("Hanoi","Ha Noi");
        data = data.replaceAll("Danang","Da Nang");
        data = data.trim();
        return data;
    }
}
