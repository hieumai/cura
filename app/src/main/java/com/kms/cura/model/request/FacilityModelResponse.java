package com.kms.cura.model.request;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 6/16/2016.
 */
public class FacilityModelResponse implements EntityModelResponse {
    List<FacilityEntity> entityList;
    boolean gotResponse;
    boolean responseError = false;
    String error;

    public FacilityModelResponse(List<FacilityEntity> entityList) {
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
                List<FacilityEntity> listFacility = new ArrayList<>();
                JSONArray jsonArrayFacility = jsonObject.getJSONArray(FacilityEntity.FACILITY_LIST);
                for (int i = 0; i < jsonArrayFacility.length(); i++) {
                    FacilityEntity entity = gson.fromJson(
                            jsonArrayFacility.getJSONObject(i).toString(), FacilityEntity.class);
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

    public List<FacilityEntity> getEntityList() {
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
