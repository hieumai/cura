package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.utils.CurrentUserProfile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linhtnvo on 8/25/2016.
 */
public class UnRegisterModelResponse implements EntityModelResponse {
    boolean gotResponse;
    boolean responseError = false;
    String error;
    @Override
    public void onErrorResponse(VolleyError error) {
        gotResponse = true;
        responseError = true;
    }

    @Override
    public void onResponse(String response) {
        JSONObject jsonObject = null;
        gotResponse = true;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean(UserEntity.STATUS_KEY);
            if (status){
                return;
            }
            error = jsonObject.getString(UserEntity.MESSAGE);
        } catch (JSONException e) {
            error = e.getMessage();
            responseError = true;
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

}
