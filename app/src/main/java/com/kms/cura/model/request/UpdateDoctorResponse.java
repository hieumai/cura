package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toanbnguyen on 8/26/2016.
 */
public class UpdateDoctorResponse implements EntityModelResponse {

    private DoctorUserEntity doctorUserEntity;
    private boolean gotResponse;
    private boolean responseError = false;
    private String error;

    public UpdateDoctorResponse() {
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
                doctorUserEntity = gson.fromJson(jsonObject.toString(), DoctorUserEntity.class);
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


    public DoctorUserEntity getUpdatedDoctor() {
        return doctorUserEntity;
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