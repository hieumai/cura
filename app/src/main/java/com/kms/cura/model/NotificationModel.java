package com.kms.cura.model;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.model.request.NotificationModelResponse;
import com.kms.cura.utils.RequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by linhtnvo on 8/23/2016.
 */
public class NotificationModel {
    public static final String APPT_TYPE = "appt_type";
    public static final String MSG_TYPE = "msg_type";
    private static String tag_string_req = "string_req";
    private static NotificationModel instance;
    private NotificationModel() {

    }

    public static NotificationModel getInstace() {
        if (instance == null) {
            instance = new NotificationModel();
        }
        return instance;
    }

    public List<NotificationEntity> getApptNotification(UserEntity entity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.GET_NOTIFICATION);
        NotificationModelResponse response = new NotificationModelResponse();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NotificationEntity.NOTI_TYPE, APPT_TYPE);
            jsonObject.put(NotificationEntity.USER_ID, entity.getId());
        } catch (JSONException e) {
            throw e;
        }
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                jsonObject.toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getNotificationEntities();
        }
        throw new Exception(response.getError());
    }

    public List<NotificationEntity> getMsgNotification(UserEntity entity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.GET_NOTIFICATION);
        NotificationModelResponse response = new NotificationModelResponse();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NotificationEntity.NOTI_TYPE, MSG_TYPE);
            jsonObject.put(NotificationEntity.USER_ID, entity.getId());
        } catch (JSONException e) {
            throw e;
        }
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                jsonObject.toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getNotificationEntities();
        }
        throw new Exception(response.getError());
    }
}
