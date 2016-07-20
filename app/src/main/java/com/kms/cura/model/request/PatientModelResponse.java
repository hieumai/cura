package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.utils.CurrentUserProfile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toanbnguyen on 7/14/2016.
 */
public class PatientModelResponse implements EntityModelResponse {
    PatientUserEntity entity;
    boolean gotResponse;
    boolean responseError = false;
    String error;

    public PatientModelResponse() {
    }

    @Override
    public void onResponse(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean(UserEntity.STATUS_KEY);
            if (status) {
                entity = JsonToEntityConverter.convertJsonStringToEntity(response, PatientUserEntity.getPatientUserType());
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

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.getMessage() == null) {
            EventBroker.getInstance().pusblish(EventConstant.CONNECTION_ERROR, null);
            return;
        }
        EventBroker.getInstance().pusblish(EventConstant.CONNECTION_ERROR, error.getMessage());
    }

    public PatientUserEntity getUpdatedPatient() {
        return entity;
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
}