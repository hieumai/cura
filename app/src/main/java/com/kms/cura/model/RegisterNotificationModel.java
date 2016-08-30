package com.kms.cura.model;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.model.request.RegisterNotificationModelResponse;
import com.kms.cura.model.request.UnRegisterModelResponse;
import com.kms.cura.utils.RequestUtils;

/**
 * Created by linhtnvo on 8/8/2016.
 */
public class RegisterNotificationModel {
    private static RegisterNotificationModel instance;
    private static String tag_string_req = "string_req";
    private RegisterNotificationModel() {
    }

    public static RegisterNotificationModel getInstance(){
        if (instance == null){
            instance = new RegisterNotificationModel();
        }
        return instance;
    }

    public void registerGCM(String regID, String regName, Context mContext){
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.REGISTER_GCM);
        RegisterNotificationModelResponse response = new RegisterNotificationModelResponse(mContext);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NotificationEntity.REG_ID, regID);
        jsonObject.addProperty(NotificationEntity.REG_NAME, regName);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
               jsonObject.toString() ,response);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Settings.TIME_OUT, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    public void unregisterGCM(String regName) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.UNREGISTER_GCM);
        UnRegisterModelResponse response = new UnRegisterModelResponse();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NotificationEntity.REG_NAME, regName);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                jsonObject.toString() ,response);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Settings.TIME_OUT, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            return;
        }
        throw new Exception(response.getError());
    }
}
