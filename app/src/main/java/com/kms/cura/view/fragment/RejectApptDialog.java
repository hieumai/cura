package com.kms.cura.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.activity.ConditionInfoActivity;
import com.kms.cura.view.activity.SymptomInfoActivity;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by linhtnvo on 7/25/2016.
 */
public class RejectApptDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String TITLE_KEY = "title";
    public static final String TYPE_KEY = "type";
    private TextView tvTitle;
    private EditText edtComment;
    private AlertDialog dialog;
    private int dialogPositive;
    private Context mContext;
    private ProgressDialog pDialog;
    private AppointmentEntity appointmentEntity;
    public RejectApptDialog() {
    }

    public static RejectApptDialog newInstance(Context context, AppointmentEntity entity) {
        RejectApptDialog fragment = new RejectApptDialog();
        fragment.setAppointmentEntity(entity);
        fragment.setmContext(context);
        return fragment;
    }

    public AppointmentEntity getAppointmentEntity() {
        return appointmentEntity;
    }

    public void setAppointmentEntity(AppointmentEntity appointmentEntity) {
        this.appointmentEntity = appointmentEntity;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View parent = inflater.inflate(R.layout.fragment_reject_dialog, null);
        AlertDialog.Builder builder = createDialogBuilder(parent);
        edtComment = (EditText) parent.findViewById(R.id.edtComment);
        dialog = builder.create();
        return dialog;
    }

    private AlertDialog.Builder createDialogBuilder(View parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(parent);
        builder.setPositiveButton(getString(R.string.YES), this);
        builder.setNegativeButton(getString(R.string.no), this);
        return builder;
    }


    @Override
    public void onClick(final DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dismiss();
        }
        else {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            dialog.dismiss();
            showProgressDialog();
            final String cmt = edtComment.getText().toString();
            AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
                private Exception exception = null;

                @Override
                protected Void doInBackground(Object[] params) {
                    try {
                        DoctorUserEntity doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
                        appointmentEntity.setStatus(AppointmentEntity.REJECT_STT);
                        appointmentEntity.setDoctorCmt(cmt);
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
                        ErrorController.showDialog(getActivity(), "Error : " + exception.getMessage());
                    } else {
                        EventBroker.getInstance().pusblish(EventConstant.UPDATE_PATIENT_REQUEST_LIST, appointmentEntity.getApptDay() );
                        dialog.dismiss();
                        ((Activity)mContext).finish();
                    }
                }
            };
            task.execute();
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
