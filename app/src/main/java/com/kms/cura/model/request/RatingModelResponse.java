package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.UserEntity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toanbnguyen on 8/24/2016.
 */
public class RatingModelResponse implements EntityModelResponse {
    private boolean gotResponse;
    private boolean responseError = false;
    private String error;
    private AppointmentEntity appt;

    public RatingModelResponse() {
    }

    public AppointmentEntity getAppt() {
        return appt;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        responseError =  true;
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
                appt = gson.fromJson(jsonObject.toString(), AppointmentEntity.class);
            }
            else {
                error = jsonObject.getString(UserEntity.MESSAGE);
                responseError = true;
            }
        } catch (JSONException e) {
            responseError = true;
            error = e.getMessage();
        }
        gotResponse = true;
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
