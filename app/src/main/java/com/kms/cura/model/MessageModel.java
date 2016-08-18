package com.kms.cura.model;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.request.MessageModelResponse;
import com.kms.cura.model.request.MessageUpdateModelResponse;
import com.kms.cura.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toanbnguyen on 7/25/2016.
 */
public class MessageModel {
    private static MessageModel instance;
    private static String tag_string_req = "string_req";
    private List<MessageEntity> messageEntities = new ArrayList<>();

    private MessageModel() {
    }

    public static MessageModel getInstance() {
        if (instance == null) {
            instance = new MessageModel();
        }
        return instance;
    }

    public List<MessageEntity> getAllMessage() {
        return messageEntities;
    }

    public void loadMessage(UserEntity entity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        if (entity instanceof PatientUserEntity) {
            builder.append(Settings.GET_MESSAGE_PATIENT);
        } else {
            builder.append(Settings.GET_MESSAGE_DOCTOR);
        }
        MessageModelResponse response = new MessageModelResponse(messageEntities);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(entity).toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            messageEntities = response.getMessageEntities();
            return;
        }
        throw new Exception(response.getError());
    }

    public boolean deleteMessage(UserEntity userEntity, MessageEntity entity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        if (userEntity instanceof PatientUserEntity) {
            builder.append(Settings.DELETE_PATIENT_MESSAGE);
        } else {
            builder.append(Settings.DELETE_DOCTOR_MESSAGE);
        }
        JsonElement element = EntityToJsonConverter.convertEntityToJson(entity);
        JsonObject object = (JsonObject) element;
        object.addProperty(MessageEntity.SENT_BY_DOCTOR, entity.isSenderDoctor());
        MessageUpdateModelResponse response = new MessageUpdateModelResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                object.toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return true;
        }
        throw new Exception(response.getError());
    }

    public boolean insertMessage(MessageEntity entity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.INSERT_MESSAGE);
        JsonElement element = EntityToJsonConverter.convertEntityToJson(entity);
        JsonObject object = (JsonObject) element;
        object.addProperty(MessageEntity.SENT_BY_DOCTOR, entity.isSenderDoctor());
        MessageUpdateModelResponse response = new MessageUpdateModelResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                object.toString(), response);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return true;
        }
        throw new Exception(response.getError());
    }

}
