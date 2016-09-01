package com.kms.cura.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.model.Settings;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.utils.ImagePicker;
import com.kms.cura.view.adapter.StringSexListAdapter;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DoctorBasicSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private static String ACTIVITY_NAME = "Basic";
    private static final int PICK_IMAGE = 505;
    private static final int MAX_IMAGE_SIZE = 25000000; //2.5 MB
    private int day, month, year;
    private Spinner spnSex_settings, spnInsurance_settings;
    private EditText name, city;
    private ImageButton calendar;
    private Button save, upload;
    private TextView tvBirth;
    private DoctorUserEntity doctorUserEntity;
    private Toolbar toolbar;
    private Bitmap bitmap;
    private boolean setNewProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
        setContentView(R.layout.activity_doctor_settings_basic);
        initToolbar();
        DataUtils.loadProfile(doctorUserEntity, (ImageView) findViewById(R.id.photo_profile), this);
        initBirthTextView();
        initEditText();
        initButtons();
        initSexSpinner();
        setNewProfile = false;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.basic_settings_toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ACTIVITY_NAME);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initButtons() {
        calendar = (ImageButton) findViewById(R.id.button_settings_calendar);
        calendar.setOnClickListener(this);
        save = (Button) findViewById(R.id.button_settings_save);
        save.setOnClickListener(this);
        upload = (Button) findViewById(R.id.button_upload_image);
        upload.setOnClickListener(this);
    }

    private void initBirthTextView() {
        Date birth = doctorUserEntity.getBirth();
        if (birth != null) {
            day = birth.getDay();
            month = birth.getMonth() + 1;
            year = birth.getYear() + 1900;
        } else {
            day = 0;
            month = 0;
            year = 1900;
        }
        tvBirth = (TextView) findViewById(R.id.setting_birthday);
        setDateString(day, month, year);
        tvBirth.setOnClickListener(this);
    }

    private void initSexSpinner() {
        ArrayList<String> sex = new ArrayList<>();
        sex.add(getString(R.string.male));
        sex.add(getString(R.string.female));
        StringSexListAdapter adapter = new StringSexListAdapter(this, R.layout.string_list_item2, sex);
        spnSex_settings = (Spinner) findViewById(R.id.spnSex_Settings);
        spnSex_settings.setAdapter(adapter);
        if (doctorUserEntity.getGender() != null && doctorUserEntity.getGender().equals("F")) {
            spnSex_settings.setSelection(1);
        } else {
            spnSex_settings.setSelection(0);
        }
    }

    private void initEditText() {
        name = (EditText) findViewById(R.id.editText_settings_name);
        name.setText(doctorUserEntity.getName());
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_settings_calendar) {
            DatePickerDialog dateDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, myDateListener, year, month - 1, day);
            dateDialog.show();
        } else if (v.getId() == R.id.button_settings_save) {
            new AsyncTask<Object, Void, Void>() {
                private Exception exception = null;
                private ProgressDialog pDialog;

                @Override
                protected void onPreExecute() {
                    pDialog = new ProgressDialog(DoctorBasicSettingsActivity.this);
                    pDialog.setMessage(getResources().getString(R.string.UpdatingMessage));
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                @Override
                protected Void doInBackground(Object[] params) {
                    try {
                        if (setNewProfile) {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            UserController.savePhoto(doctorUserEntity, encoded);
                        }
                        DoctorUserEntity entityUpdated = getDoctorUserEntity();
                        CurrentUserProfile.getInstance().setData(UserController.updateDoctorBasic(entityUpdated));
                    } catch (Exception e) {
                        exception = e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    pDialog.dismiss();
                    if (exception == null) {
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();

                    } else {
                        ErrorController.showDialog(getApplicationContext(), "Error : " + exception.getMessage());
                    }
                }
            }.execute();
        } else if (v.getId() == R.id.button_upload_image) {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
            startActivityForResult(chooseImageIntent, PICK_IMAGE);
        } else if (v.getId() == R.id.setting_birthday) {
            DatePickerDialog dateDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, myDateListener, year, month - 1, day);
            dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dateDialog.show();
        }

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            setSpecificDate(day, month + 1, year);
        }
    };

    private void setSpecificDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        setDateString(day, month, year);
    }

    public void setDateString(int day, int month, int year) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        if (month < 10) {
            builder.append("0");
        }
        builder.append(month + "/");
        if (day < 10) {
            builder.append('0');
        }
        builder.append(day + "/");
        builder.append(year);
        tvBirth.setText(builder.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE:
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {
                    requestPermission();
                }
                bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                if (bitmap != null && bitmap.getByteCount() > MAX_IMAGE_SIZE) {
                    Toast.makeText(this, R.string.ImageSizeError + " " + bitmap.getByteCount(), Toast.LENGTH_SHORT).show();
                } else if (bitmap != null && bitmap.getByteCount() != 0) {
                    ImageView profile = (ImageView) findViewById(R.id.photo_profile);
                    profile.setImageBitmap(bitmap);
                    setNewProfile = true;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Settings.MY_PERMISSION_READ_EXTERNAL_STORAGE);
    }

    public DoctorUserEntity getDoctorUserEntity() throws ParseException {
        DoctorUserEntity entityUpdated = doctorUserEntity.cloneDoctorBasic();
        entityUpdated.setName(name.getText().toString());
        if (spnSex_settings.getSelectedItemPosition() == 0) {
            entityUpdated.setGender(RegisterDoctorActivity.SEX_MALE);
        } else {
            entityUpdated.setGender(RegisterDoctorActivity.SEX_FEMALE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date parsedDate = formatter.parse(tvBirth.getText().toString());
        entityUpdated.setBirth(new java.sql.Date(parsedDate.getTime()));
        return entityUpdated;
    }
}