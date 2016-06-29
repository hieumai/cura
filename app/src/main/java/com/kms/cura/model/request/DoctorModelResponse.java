package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duyhnguyen on 6/24/2016.
 */
public class DoctorModelResponse implements EntityModelResponse {
    List<DoctorUserEntity> entityList = new ArrayList<DoctorUserEntity>();
    boolean gotResponse;
    boolean responseError = false;
    String error;

    public DoctorModelResponse() {

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
                List<DoctorUserEntity> doctors = new ArrayList<>();
                JSONArray jsonArrayFacility = jsonObject.getJSONArray(DoctorUserEntity.DOCTOR_LIST);
                for (int i = 0; i < jsonArrayFacility.length(); i++) {
                    DoctorUserEntity entity = gson.fromJson(jsonArrayFacility.getJSONObject(i).toString(), DoctorUserEntity.class);
                    doctors.add(entity);
                }
                entityList.addAll(doctors);

                EventBroker.getInstance().pusblish(EventConstant.SEARCH_SUCCESS, entityList);
            } else {
                error = jsonObject.getString(UserEntity.MESSAGE);
                responseError = true;
            }

        } catch (JSONException e) {
            responseError = true;
            error = e.getMessage();
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.getMessage() == null) {
            EventBroker.getInstance().pusblish(EventConstant.CONNECTION_ERROR, null);
            return;
        }
        EventBroker.getInstance().pusblish(EventConstant.CONNECTION_ERROR, error.getMessage());
    }
}