package com.kms.cura.controller;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.JsonObject;
import com.kms.cura.R;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.Settings;
import com.kms.cura.model.VolleyHelper;
import com.kms.cura.model.request.RegisterModelResponse;
import com.kms.cura.model.request.RegisterNotificationModelResponse;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.RequestUtils;

/**
 * Created by linhtnvo on 8/8/2016.
 */
public class RegisterIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    public static final String CURRENT_USER_ID = "current_user_id";
    private static final String[] TOPICS = {"global"};
    private String userID;

    public RegisterIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userID = intent.getStringExtra(CURRENT_USER_ID);
        if (userID == null) {
            Intent registrationIncomplete = new Intent(Settings.REGISTRATION_INCOMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationIncomplete);
            return;
        }
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(Settings.PROJECT_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token);


            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(Settings.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Intent registrationIncomplete = new Intent(Settings.REGISTRATION_INCOMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationIncomplete);
            sharedPreferences.edit().putBoolean(Settings.SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token) {
        String regName = UserEntity.USER + userID;
        NotificationController.registerGCM(this, regName, token);
    }

}
