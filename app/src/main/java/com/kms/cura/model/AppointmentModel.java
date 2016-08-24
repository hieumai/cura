package com.kms.cura.model;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.request.AppointmentModelResponse;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.RequestUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by linhtnvo on 7/15/2016.
 */
public class AppointmentModel {
    private static AppointmentModel instance;
    private static String tag_string_req = "string_req";
    private AppointmentModel() {

    }

    public static AppointmentModel getInstance() {
        if (instance == null) {
            instance = new AppointmentModel();
        }
        return instance;
    }

    public List<AppointmentEntity> bookAppointment(AppointmentEntity entity, PatientUserEntity patient) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.CREATE_APPT);
        AppointmentModelResponse response = new AppointmentModelResponse(patient);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                new Gson().toJson(entity,AppointmentEntity.getAppointmentType()),response);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Settings.TIME_OUT, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            return response.getAppts();
        }
        throw new Exception(response.getError());
    }

    public List<AppointmentEntity> getAppointment(AppointSearchEntity search) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.GET_APPT);
        AppointmentModelResponse response = null;
        PatientUserEntity entity = search.getAppointmentEntity().getPatientUserEntity();
        if (entity != null) {
            response = new AppointmentModelResponse(entity);
        }
        else{
            response = new AppointmentModelResponse(search.getAppointmentEntity().getDoctorUserEntity());
        }
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                new Gson().toJson(search,AppointSearchEntity.getAppointmentSearchType()),response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            return response.getAppts();
        }
        throw new Exception(response.getError());
    }

    public List<AppointmentEntity> updateAppointment(AppointmentEntity appointmentEntity, UserEntity entity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.UPDATE_APPT);
        AppointmentModelResponse response = new AppointmentModelResponse(entity);
        String apptUpdate = new Gson().toJson(appointmentEntity,AppointmentEntity.getAppointmentType());
        JSONObject jsonObject = new JSONObject(apptUpdate);
        jsonObject.put(AppointmentEntity.UPDATE_TYPE, entity instanceof PatientUserEntity);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                jsonObject.toString(),response);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            return response.getAppts();
        }
        throw new Exception(response.getError());
    }

}
