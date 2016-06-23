package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.entity.InsuranceEntity;
import com.kms.cura.entity.user.UserEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duyhnguyen on 6/23/2016.
 */
public class InsuranceModelResponse implements EntityModelResponse {
    List<InsuranceEntity> entityList;
    boolean gotResponse;
    boolean responseError = false;
    String error;

    public InsuranceModelResponse(List<InsuranceEntity> entityList) {
        this.entityList = entityList;
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
                entityList.clear();
                List<InsuranceEntity> listFacility = new ArrayList<>();
                JSONArray jsonArrayFacility = jsonObject.getJSONArray(InsuranceEntity.INSURANCE_LIST);
                for (int i = 0; i < jsonArrayFacility.length(); i++) {
                    InsuranceEntity entity = gson.fromJson(
                            jsonArrayFacility.getJSONObject(i).toString(), InsuranceEntity.class);
                    listFacility.add(entity);
                }
                entityList.addAll(listFacility);
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

    public List<InsuranceEntity> getEntityList() {
        return entityList;
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