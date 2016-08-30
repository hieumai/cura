package com.kms.cura.model.request;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.Settings;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linhtnvo on 8/8/2016.
 */
public class RegisterNotificationModelResponse implements EntityModelResponse{
    private Context mContext;

    public RegisterNotificationModelResponse(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Intent registrationIncomplete = new Intent(Settings.REGISTRATION_INCOMPLETE);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(registrationIncomplete);
    }

    @Override
    public void onResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean(UserEntity.STATUS_KEY);
            if (status) {
                Intent registrationComplete = new Intent(Settings.REGISTRATION_COMPLETE);
                registrationComplete.putExtra(NotificationEntity.REG_ID, jsonObject.getString(NotificationEntity.REG_ID));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(registrationComplete);
            }
        }catch (JSONException e){
            Intent registrationIncomplete = new Intent(Settings.REGISTRATION_INCOMPLETE);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(registrationIncomplete);
        }
    }
}
