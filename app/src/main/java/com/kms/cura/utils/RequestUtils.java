package com.kms.cura.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kms.cura.model.request.EntityModelResponse;
import com.google.gson.JsonElement;
import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.UserEntity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linhtnvo on 6/7/2016.
 */
public class RequestUtils {
    private static RequestUtils mInstance;
    private static String RAW_TYPE = "application/json; charset=utf-8";

    private RequestUtils(){
        //
    }

    public static StringRequest createRequest(String url, int type, final String data, EntityModelResponse modelResponse){
        return new StringRequest(type, url, modelResponse , modelResponse){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return data.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return RAW_TYPE;
            }
        };
    }

    public static StringRequest createRequestGET(String url, EntityModelResponse modelResponse){
        return new StringRequest(Request.Method.GET,url,modelResponse,modelResponse);
    }

}
