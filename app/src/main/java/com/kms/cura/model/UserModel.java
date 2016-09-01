package com.kms.cura.model;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.DoctorSearchEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.request.CheckExistModelResponse;
import com.kms.cura.model.request.DoctorModelResponse;
import com.kms.cura.model.request.LoginUserModelResponse;
import com.kms.cura.model.request.PasswordCodeModelResponse;
import com.kms.cura.model.request.PatientModelResponse;
import com.kms.cura.model.request.RegisterModelResponse;
import com.kms.cura.model.request.UpdateDoctorResponse;
import com.kms.cura.utils.RequestUtils;

import java.util.List;
import org.json.JSONObject;

public class UserModel extends EntityModel {
    private static UserModel instance;
    private static String tag_string_req = "string_req";

    private UserModel() {
    }

    public static UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    /**
     * Call to server api to save new entity
     *
     * @param user
     * @return true if save successfully, otherwise false
     */
    public boolean save(UserEntity user) {
        // call to api to save new user
        return false;
    }

    public void registerPatient(final UserEntity entity) {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.CREATE_PATIENT_API);

        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(),
                Request.Method.POST, EntityToJsonConverter.convertEntityToJson(entity).toString(),
                new RegisterModelResponse());
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    public void registerDoctor(DoctorUserEntity entity) {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.CREATE_DOCTOR_API);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(),
                Request.Method.POST, EntityToJsonConverter.convertEntityToJson(entity).toString(),
                new RegisterModelResponse());
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    public void userLogin(final UserEntity entity) {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.USER_LOGIN);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(entity).toString(), new LoginUserModelResponse());
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


    public void doctorSearch(DoctorSearchEntity entity) {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.SEARCH_DOCTOR_API);
        DoctorModelResponse doctorResponse = new DoctorModelResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(entity).toString(), doctorResponse);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    public PatientUserEntity updatePatientHealth(PatientUserEntity entity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.UPDATE_PATIENT_HEALTH);
        PatientModelResponse response = new PatientModelResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(entity).toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getUpdatedPatient();
        }
        throw new Exception(response.getError());
    }

    public UserEntity savePhoto(UserEntity entity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.UPDATE_PROFILE_PHOTO);
        PatientModelResponse response = new PatientModelResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(entity).toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getUpdatedPatient();
        }
        throw new Exception(response.getError());
    }

    public PatientUserEntity updatePatient(PatientUserEntity patient) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.UPDATE_PATIENT);
        PatientModelResponse response = new PatientModelResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(patient).toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getUpdatedPatient();
        }
        throw new Exception(response.getError());
    }

    public void updatePassword(UserEntity user) {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.UPDATE_PASSWORD);
        DoctorModelResponse doctorResponse = new DoctorModelResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(user).toString(), doctorResponse);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
    
    public void getDoctorByFacility(FacilityEntity facilityEntity) {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.GET_DOCTOR_BY_FACILITY);
        DoctorModelResponse response =  new DoctorModelResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(facilityEntity).toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    public boolean checkEmailExist(String email) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.CHECK_EMAIL);
        CheckExistModelResponse response = new CheckExistModelResponse();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(UserEntity.EMAIL, email);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                jsonObject.toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getResponseBoolean();
        }
        throw new Exception(response.getError());
    }

    public String sendResetCode(String email) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.SEND_CODE);
        PasswordCodeModelResponse response = new PasswordCodeModelResponse();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(UserEntity.EMAIL, email);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                jsonObject.toString(), response);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getResponseString();
        }
        throw new Exception(response.getError());
    }

    public String checkCode(String userID, String code) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.CHECK_CODE);
        PasswordCodeModelResponse response = new PasswordCodeModelResponse();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(UserEntity.ID, userID);
        jsonObject.addProperty(UserEntity.CODE, code);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                jsonObject.toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getResponseString();
        }
        throw new Exception(response.getError());
    }

    public DoctorUserEntity updateDoctorBasic(DoctorUserEntity doctorUserEntity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.UPDATE_DOCTOR_BASIC);
        UpdateDoctorResponse response = new UpdateDoctorResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(doctorUserEntity).toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getUpdatedDoctor();
        }
        throw new Exception(response.getError());
    }

    public DoctorUserEntity updateDoctorProfessional(DoctorUserEntity doctorUserEntity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.UPDATE_DOCTOR_PROFESSIONAL);
        UpdateDoctorResponse response = new UpdateDoctorResponse();
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(doctorUserEntity).toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse()) ;
        if (!response.isResponseError()) {
            return response.getUpdatedDoctor();
        }
        throw new Exception(response.getError());
    }
}
