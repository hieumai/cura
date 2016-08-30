package com.kms.cura.view.service;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.WindowManager;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.activity.AppointmentRequestDeatailActivity;

/**
 * Created by linhtnvo on 8/16/2016.
 */
public class PatientRequestNotiActionListener extends BroadcastReceiver {
    private ProgressDialog pDialog;
    public static String FROM_NOTI = "FROM_NOTI";
    public static String REQUEST_ID = "REQUEST_ID";
    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(NotificationListener.ACCEPT_REQUEST,-1);
        String id = intent.getStringExtra(NotificationListener.REQUEST_INFO);
        if (id == null){
            ErrorController.showSystemDialog(context, context.getString(R.string.errorMsgAcceptRejectNoti));
            return;
        }
        if (status == 0){
            rejectRequest(context, id);
            return;
        }
        acceptRequest(context, id);
    }

    private void acceptRequest(final Context context, final String id) {
        pDialog = new ProgressDialog(context);
        pDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        pDialog.setMessage(context.getString(R.string.loading));
        pDialog.setCancelable(false);
        showProgressDialog();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    DoctorUserEntity doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
                    AppointmentEntity entity = new AppointmentEntity(id, null, null, null, null, null,null,AppointmentEntity.ACCEPTED_STT,null,null);
                    DoctorUserEntity doctor = new DoctorUserEntity(doctorUserEntity.getId(),null,null,null,null,null,null,null,null,null);
                    entity.setDoctorUserEntity(doctor);
                    doctorUserEntity.setAppointmentList(AppointmentController.updateAppointment(entity, doctorUserEntity));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception != null) {
                    ErrorController.showSystemDialog(context, "Error : " + exception.getMessage());
                } else {
                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(NotificationListener.PATIENT_REQUEST_ID);
                    EventBroker.getInstance().pusblish(EventConstant.UPDATE_PATIENT_REQUEST_LIST, null );
                }
            }
        };
        task.execute();
    }

    private void rejectRequest(final Context context, String id) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationListener.PATIENT_REQUEST_ID);
        Intent intent = new Intent(context, AppointmentRequestDeatailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(FROM_NOTI, true);
        intent.putExtra(REQUEST_ID, id);
        context.startActivity(intent);

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
