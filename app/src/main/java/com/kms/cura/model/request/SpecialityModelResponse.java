package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.UserEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 6/15/2016.
 */
public class SpecialityModelResponse implements EntityModelResponse {
    List<SpecialityEntity> entityList;
    boolean gotResponse;
    boolean responseError = false;
    String error;

    public SpecialityModelResponse(List<SpecialityEntity> entityList) {
        this.entityList = entityList;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        responseError = true;
        gotResponse = true;
        this.error = error.getMessage();
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
                List<SpecialityEntity> listSpeciality = new ArrayList<>();
                JSONArray jsonArraySpeciality = jsonObject.getJSONArray(SpecialityEntity.SPECIALITY_LIST);
                for (int i = 0; i < jsonArraySpeciality.length(); i++) {
                    SpecialityEntity entity = gson.fromJson(
                            jsonArraySpeciality.getJSONObject(i).toString(), SpecialityEntity.class);
                    listSpeciality.add(entity);
                }
                entityList.addAll(listSpeciality);
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

    public List<SpecialityEntity> getEntityList() {
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
