package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toanbnguyen on 8/10/2016.
 */
public class CheckExistModelResponse implements EntityModelResponse {

    private boolean gotResponse;
    private boolean responseError = false;
    private String error;
    private boolean responseBoolean;

    public CheckExistModelResponse() {
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
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean(UserEntity.STATUS_KEY);
            if (!status) {
                error = jsonObject.getString(UserEntity.MESSAGE);
                responseError = true;
            } else {
                responseBoolean = jsonObject.getBoolean(UserEntity.EMAIL);
            }
        } catch (JSONException e) {
            responseError = true;
            error = e.getMessage();
        }
        gotResponse = true;
    }

    public boolean getResponseBoolean() {
        return responseBoolean;
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
