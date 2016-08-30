package com.kms.cura.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.view.service.PatientRequestNotiActionListener;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.fragment.DoctorApptDayVIewFragment;
import com.kms.cura.view.fragment.DoctorRequestListFragment;
import com.kms.cura.view.fragment.RejectApptDialog;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class AppointmentRequestDeatailActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    public static String COMMENT = "COMMENT";
    public static String DIALOG = "dialog";
    private LinearLayout btnViewSchedule, layoutComment;
    private Button btnReject, btnAccept, btnSendMessage;
    private TextView txtPatientName, txtApptDay, txtApptTime, txtAppFacility, txtComment;
    private AppointmentEntity appointmentEntity = null;
    private ImageButton btnBack;
    private ProgressDialog pDialog;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_request_deatail);
        if (!checkFromNoti()){
            setUpView();
            return;
        }
        id = getIntent().getStringExtra(PatientRequestNotiActionListener.REQUEST_ID);
        appointmentEntity = new AppointmentEntity(id, null, null, null, null, null,null,AppointmentEntity.ACCEPTED_STT,null,null);
        btnReject = (Button) initView(R.id.btnReject);
        btnReject.performClick();
    }

    private boolean checkFromNoti(){
        return getIntent().getBooleanExtra(PatientRequestNotiActionListener.FROM_NOTI, false);

    }

    private void setUpView() {
        layoutComment = (LinearLayout) findViewById(R.id.layoutComment);
        btnViewSchedule = (LinearLayout) initView(R.id.btnViewSchedule);
        btnSendMessage = (Button) initView(R.id.btnSendMessage);
        btnReject = (Button) initView(R.id.btnReject);
        btnAccept = (Button) initView(R.id.btnAccept);
        btnBack = (ImageButton) initView(R.id.btnBack);
        int position = getIntent().getIntExtra(DoctorRequestListFragment.REQUEST_POSITION, -1);
        if (position == -1) {
            return;
        }
        appointmentEntity = ((DoctorUserEntity) CurrentUserProfile.getInstance().getEntity()).getAppointmentList().get(position);
        txtPatientName = loadText(R.id.txtPatientName, appointmentEntity.getPatientUserEntity().getName());
        txtPatientName.setOnClickListener(this);
        txtAppFacility = loadText(R.id.txtApptFacility, appointmentEntity.getFacilityEntity().getName());
        txtApptDay = loadText(R.id.txtAppDay, getApptDate(appointmentEntity.getApptDay()));
        txtApptTime = loadText(R.id.txtApptTime, getApptTime(appointmentEntity.getStartTime(), appointmentEntity.getEndTime()));
        txtComment = loadText(R.id.txtComment, appointmentEntity.getPatientCmt());
    }


    private View initView(int id) {
        View view = findViewById(id);
        view.setOnClickListener(this);
        return view;
    }

    private TextView loadText(int id, String text) {
        TextView textView = (TextView) findViewById(id);
        if (text == null) {
            layoutComment.setVisibility(View.GONE);
        } else {
            textView.setText(text);
        }
        return textView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnReject) {
            RejectApptDialog dialog = RejectApptDialog.newInstance(AppointmentRequestDeatailActivity.this, appointmentEntity);
            dialog.show(getFragmentManager(), DIALOG);
        } else if (id == R.id.btnAccept) {
            if (checkConflict()) {
                createConflictDialog();
                return;
            }
            createAcceptDialog();
        } else if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnViewSchedule){
            Intent toSchedule = new Intent(this, ViewScheduleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(DoctorApptDayVIewFragment.SELECTED_DAY, appointmentEntity.getApptDay().getTime());
            toSchedule.putExtras(bundle);
            startActivity(toSchedule);
        }
        else if (id == R.id.txtPatientName){
            PatientUserEntity patientUserEntity = appointmentEntity.getPatientUserEntity();
            Intent intent = new Intent(AppointmentRequestDeatailActivity.this, ViewPatientProfileActivity.class);
            intent.putExtra(ViewPatientProfileActivity.PATIENT_KEY, EntityToJsonConverter.convertEntityToJson(patientUserEntity).toString());
            startActivity(intent);
        }
    }

    private boolean checkConflict() {
        List<AppointmentEntity> listAppts = ((DoctorUserEntity) CurrentUserProfile.getInstance().getEntity()).getAppointmentList();
        for (AppointmentEntity entity : listAppts) {
            Time start = entity.getStartTime();
            Time end = entity.getEndTime();
            Time apptStart = appointmentEntity.getStartTime();
            Time apptEnd = appointmentEntity.getEndTime();
            if (entity.getStatus() == AppointmentEntity.ACCEPTED_STT &&
                    Math.abs(entity.getApptDay().getTime() - appointmentEntity.getApptDay().getTime()) < DataUtils.MILISECOND_OF_DAY) {
                if (start.equals(apptStart) && end.equals(apptEnd)) {
                    return true;
                }
                if (apptStart.after(start) && apptStart.before(end)){
                    return true;
                }
                if (apptEnd.before(end) && apptEnd.after(start)){
                    return true;
                }
                if (apptStart.after(start) && apptEnd.before(end)){
                    return true;
                }
            }
        }
        return false;
    }

    private void createConflictDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.ConflictMsg));
        builder.setCancelable(true);
        builder.setNeutralButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void createAcceptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.AcceptApptMsg));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.YES), this);
        builder.setNegativeButton(getString(R.string.no), this);
        builder.show();
    }

    private String getApptTime(Time start, Time end) {
        StringBuilder builder = new StringBuilder();
        builder.append(start.toString().substring(0, 5));
        builder.append("-");
        builder.append(end.toString().substring(0, 5));
        return builder.toString();
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

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
            return;
        }
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
                    appointmentEntity.setStatus(AppointmentEntity.ACCEPTED_STT);
                    DoctorUserEntity doctor = new DoctorUserEntity(doctorUserEntity.getId(),null,null,null,null,null,null,null,null,null);
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
                    ErrorController.showDialog(AppointmentRequestDeatailActivity.this, "Error : " + exception.getMessage());
                } else {
                    EventBroker.getInstance().pusblish(EventConstant.UPDATE_PATIENT_REQUEST_LIST, appointmentEntity.getApptDay() );
                }
                finish();
            }
        };
        task.execute();
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
