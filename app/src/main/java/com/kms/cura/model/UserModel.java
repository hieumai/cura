package com.kms.cura.model;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonElement;
import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.utils.RequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel extends EntityModel {
    private static UserModel instance;
    private static String tag_string_req = "string_req";

    private UserModel() {
    }

    public static UserModel getInstace() {
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

    public void registerPatient(final UserEntity entity, final Context mContext) {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.CREATE_PATIENT_API);
        StringRequest stringRequest = RequestUtils.getInstance().createRequest(mContext,builder.toString(), Request.Method.POST,entity);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


}
