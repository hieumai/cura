package com.kms.cura.utils;

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
    public static RequestUtils getInstance(){
        if(mInstance == null){
            mInstance =new RequestUtils();
        }
        return mInstance;
    }
    public StringRequest createRequest(Context mContext, String url, int type, final UserEntity entity){
        return new StringRequest(type,url,successResponse(mContext),unsuccessResponse(mContext)){
            @Override
            public byte[] getBody() throws AuthFailureError {
                JsonElement element= EntityToJsonConverter.convertEntityToJson(entity);
                return element.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return RAW_TYPE;
            }
        };
    }

    private Response.Listener<String> successResponse(final Context mContext) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean(UserEntity.STATUS_KEY);
                    if (status) {
                        ErrorController.showDialog(mContext, "success");
                    } else {
                        String message = jsonObject.getString(UserEntity.MESSAGE);
                        ErrorController.showDialog(mContext, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ErrorController.showDialog(mContext, mContext.getResources().getString(R.string.InternalError));
                }
            }
        };
    }

    private Response.ErrorListener unsuccessResponse(final Context mContext) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorController.showDialog(mContext, error.toString());
            }
        };
    }
}
