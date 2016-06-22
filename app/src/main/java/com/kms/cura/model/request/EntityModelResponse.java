package com.kms.cura.model.request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.event.EventBroker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linhtnvo on 6/10/2016.
 */
public interface EntityModelResponse extends Response.Listener<String>, Response.ErrorListener {
    public final static String ERROR_JSON_EXCEPTION = "ERROR_JSON_EXCEPTION";
    public final static String ERROR_SERVER = "ERROR_SERVER";
    public final static String ERROR_RESPONSE = "ERROR_RESPONSE";
}