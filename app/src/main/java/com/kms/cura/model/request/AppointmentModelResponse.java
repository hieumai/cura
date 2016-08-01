package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.AppointmentModel;
import com.kms.cura.utils.CurrentUserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 7/15/2016.
 */
public class AppointmentModelResponse implements EntityModelResponse {
    boolean gotResponse;
    boolean responseError = false;
    String error;
    UserEntity userEntity;
    List<AppointmentEntity> appts;

    public AppointmentModelResponse (UserEntity userEntity){
        this.userEntity = userEntity;
    }
    public List<AppointmentEntity> getAppts() {
        return appts;
    }

    public void setAppts(List<AppointmentEntity> appts) {
        this.appts = appts;
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
                List<AppointmentEntity> listAppts = new ArrayList<>();
                JSONArray jsonArrayAppts = jsonObject.getJSONArray(AppointmentEntity.APPTS_LIST);
                for (int i = 0; i < jsonArrayAppts.length(); i++) {
                    AppointmentEntity entity = gson.fromJson(
                            jsonArrayAppts.getJSONObject(i).toString(), AppointmentEntity.class);
                    if (userEntity instanceof  PatientUserEntity) {
                        entity.setPatientUserEntity((PatientUserEntity) userEntity);
                    }
                    else{
                        entity.setDoctorUserEntity((DoctorUserEntity) userEntity);
                    }
                    listAppts.add(entity);
                }
                appts = new ArrayList<>();
                appts.addAll(listAppts);
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
