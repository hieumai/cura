package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.user.UserEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toanbnguyen on 6/28/2016.
 */
public class ConditionModelResponse implements EntityModelResponse{
    List<ConditionEntity> conditionEntities;
    boolean gotResponse;
    boolean responseError = false;
    String error;

    public ConditionModelResponse(List<ConditionEntity> conditionEntities) {
        this.conditionEntities = conditionEntities;
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
                conditionEntities.clear();
                List<ConditionEntity> listCondition = new ArrayList<>();
                JSONArray jsonArrayCondition = jsonObject.getJSONArray(ConditionEntity.CONDITION_LIST);
                for(int i = 0; i < jsonArrayCondition.length(); i++)
                {
                    ConditionEntity entity = gson.fromJson(
                            jsonArrayCondition.getJSONObject(i).toString(), ConditionEntity.class);
                    listCondition.add(entity);
                }
                conditionEntities.addAll(listCondition);
            } else {
                error = jsonObject.getString(UserEntity.MESSAGE);
                responseError = true;
            }
        } catch (JSONException e) {
            responseError = true;
            error = e.getMessage();
        }
        gotResponse = true;
    }

    public List<ConditionEntity> getAllCondition() {
        return conditionEntities;
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
