package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
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
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.fragment.CalendarView;
import com.kms.cura.view.fragment.PatientAppointmentListTabFragment;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class DoctorAppointmentDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private Button btnTag, btnSendMessage, btnCancel, btnComplete;
    private ImageView btnBack;
    private LinearLayout lbExtraComment;
    private ImageView ivPatientPicture;
    private TextView txtPatientName, txtApptDate, txtApptTime, txtApptFacility, txtApptFacilityAddress, txtApptFacilityPhone, txtComment;
    private AppointmentEntity appointmentEntity;
    private int position;
    private ProgressDialog pDialog;
    public static String PATIENT_POSITION = "PATIENT_POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment_detail);
        appointmentEntity = loadData();
        if (appointmentEntity == null) {
            ErrorController.showDialog(this, getString(R.string.errorLoadingApptDetail));
            finish();
            return;
        }
        loadView();
    }

    private void loadView() {
        lbExtraComment = (LinearLayout) findViewById(R.id.lbExtraComment);
        ivPatientPicture = (ImageView) findViewById(R.id.ivPatientPicture);
        ivPatientPicture.setOnClickListener(this);
        txtPatientName = loadTextView(R.id.txtPatientName, appointmentEntity.getPatientUserEntity().getName());
        txtPatientName.setOnClickListener(this);
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
        String patientCmt = appointmentEntity.getPatientCmt();
        if (patientCmt == null) {
            lbExtraComment.setVisibility(View.GONE);
            return;
        }
        txtComment = (TextView) findViewById(R.id.txtComment);
        txtComment.setText(patientCmt);
    }

    private AppointmentEntity loadData() {
        position = getIntent().getIntExtra(CalendarView.APPT_POSITION, -1);
        if (position == -1) {
            return null;
        }
        AppointmentEntity entity = ((DoctorUserEntity) (CurrentUserProfile.getInstance().getEntity())).getAppointmentList().get(position);
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
        btnSendMessage.setOnClickListener(this);
        btnCancel = (Button) initButton(R.id.btnCancel);
        int status = appointmentEntity.getStatus();
        if (status == AppointmentEntity.INCOMPLETED_STT) {
            btnComplete = (Button) initButton(R.id.btnCompleteAppt);
            btnComplete.setVisibility(View.VISIBLE);
            btnCancel.setText(getResources().getString(R.string.CancelAppt));
        } else if (status == AppointmentEntity.ACCEPTED_STT) {
            btnCancel.setText(getResources().getString(R.string.CancelAppt));
        } else {
            btnCancel.setVisibility(View.GONE);
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
        Integer color = getTagId(appointmentEntity.getStatus());
        if (color == null) {
            btnTag.setVisibility(View.GONE);
            return;
        }
        btnTag.setBackgroundResource(color);
    }

    private Integer getTagId(int status) {
        switch (status) {
            case AppointmentEntity.COMPLETED_STT:
                return R.drawable.complete_tag;
            case AppointmentEntity.DOCTOR_CANCEL_STT:
                return R.drawable.cancel_tag;
            case AppointmentEntity.INCOMPLETED_STT:
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
        if (id == R.id.btnCancel) {
            showDialogCancel();
        } else if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnCompleteAppt) {
            showDialogComplete();
        } else if (id == R.id.txtPatientName) {
            PatientUserEntity patientUserEntity = appointmentEntity.getPatientUserEntity();
            Intent intent = new Intent(this, ViewPatientProfileActivity.class);
            intent.putExtra(ViewPatientProfileActivity.PATIENT_KEY, EntityToJsonConverter.convertEntityToJson(patientUserEntity).toString());
            startActivity(intent);
        } else if (id == R.id.btnSendMessage) {
            Intent intent = new Intent(this, NewMessageActivity.class);
            intent.putExtra(NewMessageActivity.KEY_SENDER, NewMessageActivity.KEY_DOCTOR);
            intent.putExtra(NewMessageActivity.KEY_RECEIVER_ID, appointmentEntity.getPatientUserEntity().getId());
            intent.putExtra(NewMessageActivity.KEY_RECEIVER_NAME, appointmentEntity.getPatientUserEntity().getName());
            startActivity(intent);
        }
    }

    private void completeAppt(DialogInterface dialog) {
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
                    DoctorUserEntity doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
                    appointmentEntity.setStatus(AppointmentEntity.COMPLETED_STT);
                    DoctorUserEntity doctor = new DoctorUserEntity(doctorUserEntity.getId(), null, null, null, null, null, null, null, null, null);
                    appointmentEntity.setDoctorUserEntity(doctor);
                    doctorUserEntity.setAppointmentList(AppointmentController.updateAppointment(appointmentEntity, doctorUserEntity));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception != null) {
                    ErrorController.showDialog(DoctorAppointmentDetailActivity.this, "Error : " + exception.getMessage());
                } else {
                    EventBroker.getInstance().pusblish(EventConstant.UPDATE_APPT_DOCTOR_LIST, appointmentEntity.getApptDay());
                }
                finish();
            }
        };
        task.execute();
    }

    private void cancelAppt(DialogInterface dialog) {
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
                    DoctorUserEntity doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
                    appointmentEntity.setStatus(AppointmentEntity.DOCTOR_CANCEL_STT);
                    DoctorUserEntity doctor = new DoctorUserEntity(doctorUserEntity.getId(), null, null, null, null, null, null, null, null, null);
                    appointmentEntity.setDoctorUserEntity(doctor);
                    doctorUserEntity.setAppointmentList(AppointmentController.updateAppointment(appointmentEntity, doctorUserEntity));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception != null) {
                    ErrorController.showDialog(DoctorAppointmentDetailActivity.this, "Error : " + exception.getMessage());
                } else {
                    EventBroker.getInstance().pusblish(EventConstant.UPDATE_APPT_DOCTOR_LIST, appointmentEntity.getApptDay());
                }
                finish();
            }
        };
        task.execute();
    }


    private void showDialogCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.CancelAppt));
        builder.setMessage(getString(R.string.CancelApptMsg));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.yes), DoctorAppointmentDetailActivity.this);
        builder.setNegativeButton(getString(R.string.no), this);
        builder.show();
    }

    private void showDialogComplete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.CompleteAppt));
        builder.setMessage(getString(R.string.CompleteApptMsg));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                completeAppt(dialog);
            }
        });
        builder.setNegativeButton(getString(R.string.no), this);
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            cancelAppt(dialog);
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
}
