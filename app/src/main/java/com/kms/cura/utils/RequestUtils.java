package com.kms.cura.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.kms.cura.model.request.EntityModelResponse;

/**
 * Created by linhtnvo on 6/7/2016.
 */
public class RequestUtils {
    private static RequestUtils mInstance;
    private static String RAW_TYPE = "application/json; charset=utf-8";

    private RequestUtils() {
        //
    }

    public static StringRequest createRequest(String url, int type, final String data, EntityModelResponse modelResponse) {
        return new StringRequest(type, url, modelResponse, modelResponse) {
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

    public static StringRequest createRequestGET(String url, EntityModelResponse modelResponse) {
        return new StringRequest(Request.Method.GET, url, modelResponse, modelResponse);
    }

}
