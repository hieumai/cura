package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toanbnguyen on 7/25/2016.
 */
public class MessageModelResponse implements EntityModelResponse {

    private List<MessageEntity> messageEntities;
    private boolean gotResponse;
    private boolean responseError = false;
    private String error;

    public MessageModelResponse(List<MessageEntity> messageEntities) {
        this.messageEntities = messageEntities;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        responseError = true;
        this.error = error.getMessage();
        gotResponse = true;
    }

    @Override
    public void onResponse(String response) {
        JSONObject jsonObject = null;
        Gson gson = new Gson();
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean(UserEntity.STATUS_KEY);
            if (status) {
                messageEntities.clear();
                List<MessageEntity> messageList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray(MessageEntity.MESSAGE_LIST);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    MessageEntity entity = JsonToEntityConverter.convertJsonStringToEntity(object.toString(), MessageEntity.class);
                    UserEntity sender, receiver;
                    JSONObject jSonReceiver = object.getJSONObject(MessageEntity.RECEIVER);
                    JSONObject jSonSender = object.getJSONObject(MessageEntity.SENDER);
                    if (object.getInt(MessageEntity.SENT_BY_DOCTOR) == 1) {
                        receiver = (PatientUserEntity) gson.fromJson(jSonReceiver.toString(), PatientUserEntity.getPatientUserType());
                        sender = (DoctorUserEntity) gson.fromJson(jSonSender.toString(), DoctorUserEntity.getDoctorEntityType());
                    } else {
                        sender = (PatientUserEntity) gson.fromJson(jSonSender.toString(), PatientUserEntity.getPatientUserType());
                        receiver = (DoctorUserEntity) gson.fromJson(jSonReceiver.toString(), DoctorUserEntity.getDoctorEntityType());
                    }
                    entity.setSender(sender);
                    entity.setReceiver(receiver);
                    messageList.add(entity);
                }
                messageEntities.addAll(messageList);
            } else {
                error = jsonObject.getString(UserEntity.MESSAGE);
                responseError = true;
            }
        } catch (JSONException e) {
            responseError = true;
            error = e.getMessage();
        }
        gotResponse = true;
    }

    public List<MessageEntity> getMessageEntities() {
        return messageEntities;
    }

    public boolean isGotResponse() {
        return gotResponse;
    }

    public boolean isResponseError() {
        return responseError;
    }

    public String getError() {
        return error;
    }
}
