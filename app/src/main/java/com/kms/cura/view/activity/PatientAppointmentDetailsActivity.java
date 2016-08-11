package com.kms.cura.view.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.fragment.PatientAppointmentListTabFragment;
import com.kms.cura.view.fragment.RateDoctorDialogFragment;

import java.sql.Date;
import java.sql.Time;

public class PatientAppointmentDetailsActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private Button btnTag, btnSendMessage, btnCancelorRate;
    private ImageView btnBack;
    private LinearLayout lbRated, lbExtraComment;
    private ImageView ivPatientPicture;
    private TextView txtDoctorName, txtApptDate, txtApptTime, txtApptFacility, txtApptFacilityAddress, txtApptFacilityPhone, txtComment;
    private AppointmentEntity appointmentEntity;
    private int position;
    private boolean rated = true;
    public static String PATIENT_POSITION = "PATIENT_POSITION";
    private static String DIALOG = "dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_details);
        appointmentEntity = loadData();
        loadView();
    }

    private void loadView() {
        lbRated = (LinearLayout) findViewById(R.id.lbRated);
        lbRated.setVisibility(View.INVISIBLE);
        lbExtraComment = (LinearLayout) findViewById(R.id.lbExtraComment);
        ivPatientPicture = (ImageView) findViewById(R.id.ivPatientPicture);
        ivPatientPicture.setOnClickListener(this);
        txtDoctorName = loadTextView(R.id.txtDoctorName, appointmentEntity.getDoctorUserEntity().getName());
        txtDoctorName.setOnClickListener(this);
        txtApptDate = loadTextView(R.id.txtAppDay, getApptDate(appointmentEntity.getApptDay()));
        txtApptTime = loadTextView(R.id.txtApptTime, getApptTime(appointmentEntity.getStartTime(), appointmentEntity.getEndTime()));
        FacilityEntity facilityEntity = appointmentEntity.getFacilityEntity();
        txtApptFacility = loadTextView(R.id.txtApptFacility, facilityEntity.getName());
        txtApptFacilityAddress = loadTextView(R.id.txtApptFacilityAddress, DataUtils.showUnicode(facilityEntity.getAddress()));
        txtApptFacilityPhone = loadTextView(R.id.txtApptFacilityPhone, facilityEntity.getPhone());
        loadComment();
        loadButton();
    }

    private void loadComment() {
        String doctorCmt = appointmentEntity.getDoctorCmt();
        String patientCmt = appointmentEntity.getPatientCmt();
        if (doctorCmt == null && patientCmt == null) {
            lbExtraComment.setVisibility(View.GONE);
            return;
        }
        txtComment = (TextView) findViewById(R.id.txtComment);
        if (doctorCmt != null) {
            txtComment.setText(doctorCmt);
            return;
        }
        txtComment.setText(patientCmt);
    }

    private AppointmentEntity loadData() {
        position = getIntent().getIntExtra(PatientAppointmentListTabFragment.APPT_POSITION, -1);
        if (position == -1) {
            return null;
        }
        AppointmentEntity entity = ((PatientUserEntity) (CurrentUserProfile.getInstance().getEntity())).getAppointmentList().get(position);
        return entity;
    }

    private TextView loadTextView(int id, String text) {
        TextView textView = (TextView) findViewById(id);
        textView.setText(text);
        return textView;
    }

    private void loadButton() {
        loadTagButton();
        btnBack = (ImageView) initButton(R.id.btnBack);
        btnSendMessage = (Button) initButton(R.id.btnSendMessage);
        btnCancelorRate = (Button) initButton(R.id.btnCancelOrRate);
        int status = appointmentEntity.getStatus();
        if (status == AppointmentEntity.COMPLETED_STT) {
            rated = false;
            btnCancelorRate.setText(getResources().getString(R.string.Rate));
        } else if (status == AppointmentEntity.PENDING_STT) {
            btnCancelorRate.setText(getResources().getString(R.string.CancelAppt));
        } else {
            btnCancelorRate.setVisibility(View.GONE);
        }
    }

    private View initButton(int id) {
        View button = findViewById(id);
        button.setOnClickListener(this);
        return button;
    }

    private void loadTagButton() {
        btnTag = (Button) findViewById(R.id.btnTag);
        String tag = appointmentEntity.getStatusName();
        btnTag.setText(tag);
        btnTag.setBackgroundResource(getTagId(appointmentEntity.getStatus()));
    }

    private Integer getTagId(int status) {
        switch (status) {
            case 0:
                return R.drawable.pending_tag;
            case 1:
                return R.drawable.complete_tag;
            case 2:
                return R.drawable.reject_tag;
            case 3:
                return R.drawable.cancel_tag;
            case 4:
                return R.drawable.cancel_tag;
            case 5:
                return R.drawable.complete_tag;
            case 6:
                return R.drawable.pending_tag;
            default:
                return null;
        }
    }


    private String getApptDate(Date date) {
        StringBuilder sb = new StringBuilder();
        sb.append(date.getDate());
        sb.append("/");
        sb.append(date.getMonth() + 1);
        sb.append("/");
        sb.append(date.getYear() + 1900);
        return sb.toString();
    }

    private String getApptTime(Time start, Time end) {
        StringBuilder builder = new StringBuilder();
        builder.append(start.toString().substring(0, 5));
        builder.append("-");
        builder.append(end.toString().substring(0, 5));
        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnCancelOrRate) {
            if (rated) {
                showDialogCancel();
                return;
            }
            RateDoctorDialogFragment rateDoctorDialogFragment = new RateDoctorDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(RateDoctorDialogFragment.APPT_POSITION, position);
            rateDoctorDialogFragment.setArguments(bundle);
            rateDoctorDialogFragment.show(getFragmentManager(),DIALOG);
        } else if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.txtDoctorName || id == R.id.ivPatientPicture) {
            Intent toProfileView = new Intent(this, ViewDoctorProfileActivity.class);
            toProfileView.putExtra(PATIENT_POSITION, position);
            startActivity(toProfileView);

        }
    }


    private void showDialogCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.CancelAppt));
        builder.setMessage(getString(R.string.CancelApptMsg));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.yes), PatientAppointmentDetailsActivity.this);
        builder.setNegativeButton(getString(R.string.no), this);
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            // Cancel Appointment
        } else {
            dialog.dismiss();
        }
    }
}
