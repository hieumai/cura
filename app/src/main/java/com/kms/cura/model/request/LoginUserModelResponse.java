package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.utils.CurrentUserProfile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linhtnvo on 6/10/2016.
 */
public class LoginUserModelResponse implements EntityModelResponse {
    @Override
    public void onResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean(UserEntity.STATUS_KEY);
            if (status) {
                int type = jsonObject.getInt(UserEntity.TYPE);
                if(type == PatientUserEntity.PATIENT_TYPE){
                    PatientUserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(response,PatientUserEntity.getPatientUserType());
                    CurrentUserProfile.getInstance().setData(entity);
                    EventBroker.getInstance().pusblish(EventConstant.LOGIN_SUCCESS,EventConstant.TYPE_PATIENT);
                }
                else{
                    DoctorUserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(response,DoctorUserEntity.getDoctorEntityType());
                    CurrentUserProfile.getInstance().setData(entity);
                    EventBroker.getInstance().pusblish(EventConstant.LOGIN_SUCCESS,EventConstant.TYPE_DOCTOR);
                }
                EventBroker.getInstance().pusblish(EventConstant.LOGIN_SUCCESS, "");
            } else {
                String message = jsonObject.getString(UserEntity.MESSAGE);
                EventBroker.getInstance().pusblish(EventConstant.LOGIN_FAILED, message);
            }
        } catch (JSONException e) {
            EventBroker.getInstance().pusblish(EventConstant.INTERNAL_ERROR, e.getMessage());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(error.getMessage() == null){
            EventBroker.getInstance().pusblish(EventConstant.CONNECTION_ERROR,null);
            return;
        }
        EventBroker.getInstance().pusblish(EventConstant.CONNECTION_ERROR, error.getMessage());
    }
}