package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.utils.DoctorSearchResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duyhnguyen on 6/24/2016.
 */
public class DoctorModelResponse implements EntityModelResponse {
    List<DoctorUserEntity> entityList;
    boolean gotResponse;
    boolean responseError = false;
    String error;

    public DoctorModelResponse(List<DoctorUserEntity> entityList) {
        this.entityList = entityList;
    }

    @Override
    public void onResponse(String response) {
        JSONObject jsonObject = null;
        Gson gson = new Gson();
        System.out.println(response.toString());
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
                DoctorSearchResults.getInstance().setData(entityList);

                EventBroker.getInstance().pusblish(EventConstant.SEARCH_SUCCESS, EventConstant.TYPE_DOCTOR);
            } else {
                error = jsonObject.getString(UserEntity.MESSAGE);
                responseError = true;
            }

        } catch (JSONException e) {
            responseError = true;
            error = e.getMessage();
            System.out.println(error.toString());
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("error");
        if (error.getMessage() == null) {
            EventBroker.getInstance().pusblish(EventConstant.CONNECTION_ERROR, null);
            return;
        }
        System.out.println(error.getMessage().toString());
        EventBroker.getInstance().pusblish(EventConstant.CONNECTION_ERROR, error.getMessage());
    }
}