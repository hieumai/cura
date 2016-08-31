package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.kms.cura.entity.user.UserEntity;

import org.json.JSONObject;

/**
 * Created by linhtnvo on 8/26/2016.
 */
public class UpdateNotificationModelResponse implements EntityModelResponse {
    private boolean gotResponse;
    private boolean responseError = false;
    private String error;
    @Override
    public void onErrorResponse(VolleyError error) {
        gotResponse = true;
        responseError = true;
        this.error = error.getMessage();
    }

    @Override
    public void onResponse(String response) {
        JSONObject jsonObject = null;
        gotResponse = true;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean(UserEntity.STATUS_KEY);
            if (status) {
                return;
            }
            else{
                responseError = true;
                error = jsonObject.getString(UserEntity.MESSAGE);
            }
        } catch (Exception e) {
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
}
