package com.kms.cura.model;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.request.DoctorModelResponse;
import com.kms.cura.model.request.LoginUserModelResponse;
import com.kms.cura.model.request.RegisterModelResponse;
import com.kms.cura.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

public class UserModel extends EntityModel {
    private static UserModel instance;
    private static String tag_string_req = "string_req";
    private List<DoctorUserEntity> doctors = new ArrayList<>();

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

    public void doctorSearch(DoctorUserEntity entity) {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_LOCAL_URL);
        builder.append(Settings.SEARCH_DOCTOR_API);
        DoctorModelResponse doctorResponse = new DoctorModelResponse(doctors);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(entity).toString(), doctorResponse);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
}
