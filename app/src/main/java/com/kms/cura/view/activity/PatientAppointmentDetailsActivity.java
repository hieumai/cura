package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.fragment.PatientAppointmentListTabFragment;
import com.kms.cura.view.fragment.RateDoctorDialogFragment;

import java.sql.Date;
import java.sql.Time;

public class PatientAppointmentDetailsActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener, EventHandler {
    private Button btnTag, btnSendMessage, btnCancelorRate;
    private ImageView btnBack;
    private LinearLayout lbRated, lbExtraComment;
    private ImageView ivPatientPicture;
    private TextView txtDoctorName, txtApptDate, txtApptTime, txtApptFacility, txtApptFacilityAddress, txtApptFacilityPhone, txtComment, txtlabelExtraCmt;
    private AppointmentEntity appointmentEntity;
    private int position;
    private boolean rated = true;
    public static String PATIENT_POSITION = "PATIENT_POSITION";
    private static String DIALOG = "dialog";
    private ProgressDialog pDialog;
    private boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_details);
        appointmentEntity = loadData();
        loadView();
    }

    private void loadView() {
        txtlabelExtraCmt = (TextView) findViewById(R.id.txtlbExtraComment);
        lbRated = (LinearLayout) findViewById(R.id.lbRated);
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
        if (appointmentEntity.getRate() == 0) {
            lbRated.setVisibility(View.INVISIBLE);
        } else {
            lbRated.setVisibility(View.VISIBLE);
            btnCancelorRate.setVisibility(View.GONE);
        }

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
            txtlabelExtraCmt.setText(R.string.rejectDoctorCmtLabel);
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
        } else if (id == R.id.btnSendMessage) {
            Intent intent = new Intent(this, NewMessageActivity.class);
            intent.putExtra(NewMessageActivity.KEY_SENDER, NewMessageActivity.KEY_PATIENT);
            intent.putExtra(NewMessageActivity.KEY_RECEIVER_ID, appointmentEntity.getDoctorUserEntity().getId());
            intent.putExtra(NewMessageActivity.KEY_RECEIVER_NAME, appointmentEntity.getDoctorUserEntity().getName());
            startActivity(intent);
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
    public void onClick(final DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            dialog.dismiss();
            showProgressDialog();
            AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
                private Exception exception = null;

                @Override
                protected Void doInBackground(Object[] params) {
                    try {
                        PatientUserEntity patient = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
                        appointmentEntity.setStatus(AppointmentEntity.PATIENT_CANCEL_STT);
                        PatientUserEntity patientUserEntity = new PatientUserEntity(patient.getId(),null,null,null,null,null,null,null,null,null);
                        appointmentEntity.setPatientUserEntity(patientUserEntity);
                        patient.setAppointmentList(AppointmentController.updateAppointment(appointmentEntity, patient));
                    } catch (Exception e) {
                        exception = e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if (exception != null) {
                        ErrorController.showDialog(PatientAppointmentDetailsActivity.this, "Error : " + exception.getMessage());
                    } else {
                        hideProgressDialog();
                        EventBroker.getInstance().pusblish(EventConstant.UPDATE_APPT_PATIENT_LIST, null);
                    }
                    finish();
                }
            };
            task.execute();
        } else {
            dialog.dismiss();
        }
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
        if (event.equals(EventConstant.APPOINTMENT_RATED)) {
            btnCancelorRate.setVisibility(View.GONE);
            lbRated.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        EventBroker.getInstance().unRegister(this, EventConstant.APPOINTMENT_RATED);
        super.onPause();
    }

    @Override
    protected void onResume() {
        EventBroker.getInstance().register(this, EventConstant.APPOINTMENT_RATED);
        super.onResume();
    }
}
