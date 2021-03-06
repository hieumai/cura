package com.kms.cura.view.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.AppointmentController;

import com.kms.cura.controller.MessageController;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;

import com.kms.cura.view.activity.DoctorViewActivity;
import com.kms.cura.view.activity.PatientViewActivity;
import com.kms.cura.view.fragment.MessageListFragment;


/**
 * Created by linhtnvo on 8/9/2016.
 */
public class NotificationListener extends GcmListenerService {
    public static final String UPDATE = "UPDATE";
    public static final String ACCEPT_REQUEST = "ACCEPT_REQUEST";
    public static final String REQUEST_INFO = "REQUEST_INFO";
    public static final int PATIENT_REQUEST_ID = 0;
    public static final int PATIENT_REQUEST_ACCEPT_ID = 1;
    public static final int PATIENT_REQUEST_REJECT_ID = 2;
    public static final int NEW_MSG = 3;
    public static final int UPDATE_PATIENT_APPT_ID = 4;
    public static final int INCOMPLETE_APPT_ID = 5;
    public static final int UPDATE_PATIENT_APPT_DELETE_NOTI_ID = 6;
    public static final int INCOMPLETE_APPT_DELETE_NOTI_ID = 7;
    private boolean updatedDoctorRequest = false;
    private boolean updatedApptList = false;
    private final static String CANCEL_NOTI = "cancel_noti";
    private Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    public static final String NUM_APPT_NOTI = "NUM_APPT_NOTI";
    public static final String NUM_MSG_NOTI = "NUM_MSG_NOTI";
    public static final String NUM_APPT_INCOMPLETE_NOTI = "NUM_APPT_INCOMPLETE_NOTI";

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        if (CurrentUserProfile.getInstance().getEntity() == null) {
            return;
        }
        String type = bundle.getString(NotificationEntity.NOTI_TYPE);
        if (type.equals(NotificationEntity.PATIENT_REQUEST_TYPE)) {
            updateDoctorRequestList(bundle.getString(NotificationEntity.PATIENT_REQUEST_TYPE));
        } else if (type.equals(NotificationEntity.MSG_TYPE)) {
            updateMessageList(bundle.getString(NotificationEntity.MSG_TYPE));
        } else if (type.equals(NotificationEntity.UPDATE_APPT_TYPE) || type.equals(NotificationEntity.INCOMPLETE_APPT_TYPE)) {
            updateApptList();
        }
    }

    private void sendNotificationPatientRequest(String content) {
        AppointmentEntity appointmentEntity = new Gson().fromJson(content, AppointmentEntity.getAppointmentType());
        Intent intent = new Intent(this, DoctorViewActivity.class);
        intent.putExtra(DoctorViewActivity.NAVIGATE_TO, DoctorViewActivity.PATIENT_REQUEST);
        intent.putExtra(UPDATE, updatedDoctorRequest);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, PATIENT_REQUEST_ID, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Intent acceptRequest = new Intent(this, PatientRequestNotiActionListener.class);
        acceptRequest.putExtra(ACCEPT_REQUEST, 1);
        acceptRequest.putExtra(REQUEST_INFO, appointmentEntity.getId());
        acceptRequest.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pAcceptRequest = PendingIntent.getBroadcast(this, PATIENT_REQUEST_ACCEPT_ID, acceptRequest, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent rejectRequest = new Intent(this, PatientRequestNotiActionListener.class);
        rejectRequest.putExtra(ACCEPT_REQUEST, 0);
        rejectRequest.putExtra(REQUEST_INFO, appointmentEntity.getId());
        rejectRequest.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pRejectRequest = PendingIntent.getBroadcast(this, PATIENT_REQUEST_REJECT_ID, rejectRequest, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.appt)
                .setContentTitle(getString(R.string.appName))
                .setContentText(getString(R.string.notiMsgCollapse))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setStyle(createExpandStyleForRequestNoti(appointmentEntity));
        notificationBuilder.addAction(R.drawable.accept, getString(R.string.Accept), pAcceptRequest)
                .addAction(R.drawable.reject_icon, getString(R.string.Reject), pRejectRequest);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(PATIENT_REQUEST_ID, notificationBuilder.build());
        EventBroker.getInstance().pusblish(EventConstant.UPDATE_REQUEST_NOTI_NUMBER,true);
    }

    private NotificationCompat.BigTextStyle createExpandStyleForRequestNoti(AppointmentEntity appointmentEntity) {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(getRequestInfo(appointmentEntity))
                .setBigContentTitle(appointmentEntity.getPatientUserEntity().getName());
        return bigTextStyle;
    }

    private String getRequestInfo(AppointmentEntity entity) {
        StringBuilder builder = new StringBuilder();
        builder.append(DataUtils.getApptTime(entity.getStartTime(), entity.getEndTime()));
        builder.append("\n");
        builder.append(entity.getApptDay().toString());
        builder.append("\n");
        builder.append(entity.getFacilityEntity().getName());
        return builder.toString();
    }

    private void updateDoctorRequestList(final String content) {
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    DoctorUserEntity doctor = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
                    DoctorUserEntity doctorUserEntity = new DoctorUserEntity(doctor.getId(), null);
                    AppointmentEntity entity = new AppointmentEntity(null, null, doctorUserEntity, null, null, null, null, -1, null, null);
                    doctor.setAppointmentList(AppointmentController.getAppointment(new AppointSearchEntity(entity)));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updatedDoctorRequest = (exception == null);
                if (updatedDoctorRequest) {
                    EventBroker.getInstance().pusblish(EventConstant.UPDATE_PATIENT_REQUEST_LIST, null);
                }
                sendNotificationPatientRequest(content);
            }
        };
        task.execute();
    }


    private void sendUpdateAppt() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int notiNum = sharedPreferences.getInt(NUM_APPT_NOTI, -1);
        if (notiNum == -1){
            notiNum = 1;
        }
        else{
            ++notiNum;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NUM_APPT_NOTI, notiNum);
        editor.commit();
        String content = getContentForUpdateApptNoti(notiNum);
        Intent intent = new Intent(this, PatientViewActivity.class);
        intent.putExtra(PatientViewActivity.NAVIGATION_KEY, PatientViewActivity.PATIENT_APPT);
        intent.putExtra(UPDATE, updatedApptList);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, UPDATE_PATIENT_APPT_ID, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.appt)
                .setContentTitle(getString(R.string.appName))
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(getDeleteIntent(UPDATE_PATIENT_APPT_DELETE_NOTI_ID))
                .setDefaults(Notification.DEFAULT_VIBRATE);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(UPDATE_PATIENT_APPT_ID, notificationBuilder.build());

    }

    private String getContentForUpdateApptNoti (int notiNum){
        StringBuilder builder = new StringBuilder();
        builder.append(notiNum);
        builder.append(" ");
        if (notiNum == 1){
            builder.append(getString(R.string.apptUpdateNoti));
        }
        else{
            builder.append(getString(R.string.apptUpdateNotis));
        }
        return  builder.toString();
    }


    protected PendingIntent getDeleteIntent(int id)
    {
        Intent intent = new Intent(this, NotificationDeleteListener.class);
        intent.setAction(CANCEL_NOTI);
        intent.putExtra(NotificationEntity.NOTI_TYPE, id);
        return PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void updateApptList() {
        final boolean patient = CurrentUserProfile.getInstance().isPatient();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    if (patient){
                        PatientUserEntity currentUser = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
                        PatientUserEntity patientUserEntity = new PatientUserEntity(currentUser.getId(), null);
                        AppointmentEntity entity = new AppointmentEntity(null,patientUserEntity, null, null, null, null, null, -1, null, null);
                        currentUser.setAppointmentList(AppointmentController.getAppointment(new AppointSearchEntity(entity)));
                        return null;
                    }
                    DoctorUserEntity currentUser = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
                    DoctorUserEntity doctorUserEntity = new DoctorUserEntity(currentUser.getId(), null);
                    AppointmentEntity entity = new AppointmentEntity(null,null, doctorUserEntity, null, null, null, null, -1, null, null);
                    currentUser.setAppointmentList(AppointmentController.getAppointment(new AppointSearchEntity(entity)));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updatedApptList = (exception == null);
                if (updatedApptList && patient){
                    EventBroker.getInstance().pusblish(EventConstant.UPDATE_APPT_PATIENT_LIST, null);
                }
                if (updatedApptList && !patient){
                    EventBroker.getInstance().pusblish(EventConstant.UPDATE_APPT_DOCTOR_LIST, null);
                }
                sendNoti(patient);
            }
        };
        task.execute();
    }

    private void sendNoti(boolean patient){
        if (patient){
            sendUpdateAppt();
            return;
        }
        sendInCompleteAppt();
    }

    private void sendInCompleteAppt() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int notiNum = sharedPreferences.getInt(NUM_APPT_INCOMPLETE_NOTI, -1);
        if (notiNum == -1) {
            notiNum = 1;
        } else {
            ++notiNum;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NUM_APPT_INCOMPLETE_NOTI, notiNum);
        editor.commit();
        String content = getContentForInCompleteApptNoti(notiNum);
        Intent intent = new Intent(this, DoctorViewActivity.class);
        intent.putExtra(DoctorViewActivity.NAVIGATE_TO, DoctorViewActivity.DOCTOR_APPT);
        intent.putExtra(UPDATE, updatedApptList);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, INCOMPLETE_APPT_ID, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.appt)
                .setContentTitle(getString(R.string.appName))
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(getDeleteIntent(INCOMPLETE_APPT_DELETE_NOTI_ID))
                .setDefaults(Notification.DEFAULT_VIBRATE);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(INCOMPLETE_APPT_ID, notificationBuilder.build());
        EventBroker.getInstance().pusblish(EventConstant.UPDATE_APPT_NOTI_NUMBER, true);
    }

    private String getContentForInCompleteApptNoti(int notiNum) {
        StringBuilder builder = new StringBuilder();
        builder.append(notiNum);
        builder.append(" ");
        if (notiNum == 1) {
            builder.append(getString(R.string.incompleteApptNoti));
        } else {
            builder.append(getString(R.string.incompleteApptNotis));
        }
        return builder.toString();
    }

    private void updateMessageList(final String message) {
        new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    UserEntity userEntity = CurrentUserProfile.getInstance().getEntity();
                    MessageController.loadMessage(userEntity);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (exception == null) {
                    EventBroker.getInstance().pusblish(EventConstant.UPDATE_MESSAGE_LIST, null);
                    createMessageNoti(message);
                }
            }
        }.execute();
    }

    private void createMessageNoti(String message) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int notiNum = sharedPreferences.getInt(NUM_MSG_NOTI, -1);
        if (notiNum < 0) {
            notiNum = 1;
        } else {
            ++notiNum;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NUM_MSG_NOTI, notiNum);
        editor.commit();
        String content = getContentForMessageNoti(notiNum, message);
        Intent intentMsg;
        if (CurrentUserProfile.getInstance().isDoctor()) {
            intentMsg = new Intent(this, DoctorViewActivity.class);
            intentMsg.putExtra(DoctorViewActivity.NAVIGATE_TO, MessageListFragment.TO_MESSAGE);
        } else {
            intentMsg = new Intent(this, PatientViewActivity.class);
            intentMsg.putExtra(PatientViewActivity.NAVIGATION_KEY, MessageListFragment.TO_MESSAGE);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NEW_MSG, intentMsg, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.message_noti)
                .setContentTitle(getString(R.string.appName))
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(getDeleteIntent(NEW_MSG))
                .setDefaults(Notification.DEFAULT_VIBRATE);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NEW_MSG, notificationBuilder.build());
    }

    private String getContentForMessageNoti(int notiNum, String mes) {
        if (notiNum == 1)
            return notiNum + getString(R.string.new_msg_noti);
        return notiNum + getString(R.string.new_msg_notis);
    }
}
