package com.kms.cura.view.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kms.cura.entity.NotificationEntity;

/**
 * Created by linhtnvo on 8/30/2016.
 */
public class NotificationDeleteListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(NotificationEntity.NOTI_TYPE, -1);
        if (id == -1){
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (id){
            case NotificationListener.INCOMPLETE_APPT_DELETE_NOTI_ID:
                editor.remove(NotificationListener.NUM_APPT_INCOMPLETE_NOTI);
                break;
            case NotificationListener.UPDATE_PATIENT_APPT_DELETE_NOTI_ID:
                editor.remove(NotificationListener.NUM_APPT_NOTI);
                break;
        }
        editor.commit();
    }
}
