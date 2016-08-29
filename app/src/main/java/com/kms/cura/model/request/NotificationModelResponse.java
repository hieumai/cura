package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.UserEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 8/23/2016.
 */
public class NotificationModelResponse implements EntityModelResponse {
    private static String ID = "id";
    private static String STATUS = "status";
    private static String APPT_NOTI_TYPE = "appt_noti_type";
    public NotificationModelResponse(){

    }
    private List<NotificationEntity> notificationEntities = new ArrayList<>();
    private boolean gotResponse;
    private boolean responseError = false;
    private String error;
    @Override
    public void onErrorResponse(VolleyError error) {
        responseError = true;
        this.error = error.getMessage();
        gotResponse = true;
    }

    @Override
    public void onResponse(String response) {
        JSONObject jsonObject = null;
        gotResponse = true;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean(UserEntity.STATUS_KEY);
            if (status) {
                List<NotificationEntity> listNotifs = new ArrayList<>();
                JSONArray jsonArrayAppts = jsonObject.getJSONArray(NotificationEntity.NOTI_LIST);
                for (int i = 0; i < jsonArrayAppts.length(); i++) {
                    JSONObject object = jsonArrayAppts.getJSONObject(i);
                    String type = object.getString(NotificationEntity.NOTI_TYPE);
                    NotificationEntity entity = null;
                    if (type.equals(NotificationEntity.MSG_TYPE)){
                        entity = new NotificationEntity(object.getString(ID), new Gson().fromJson(object.getJSONObject(NotificationEntity.REF_ENTITY).toString(), MessageEntity.class),
                                object.getBoolean(STATUS));
                    }
                    else{
                        entity = new NotificationEntity(object.getString(ID), new Gson().fromJson(object.getJSONObject(NotificationEntity.REF_ENTITY).toString(), AppointmentEntity.class),
                                object.getBoolean(STATUS), object.getString(APPT_NOTI_TYPE));
                    }
                    listNotifs.add(entity);
                }
                notificationEntities.clear();
                notificationEntities.addAll(listNotifs);
            }
            else{
                error = jsonObject.getString(UserEntity.MESSAGE);
                responseError = true;
            }
        }
        catch (Exception e) {
            responseError = true;
            error = e.getMessage();
        }
    }

    public boolean isGotResponse(){
        return gotResponse;
    }

    public boolean isResponseError() {
        return responseError;
    }

    public String getError() {
        return error;
    }

    public List<NotificationEntity> getNotificationEntities() {
        return notificationEntities;
    }
}
