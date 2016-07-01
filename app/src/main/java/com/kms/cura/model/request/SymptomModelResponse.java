package com.kms.cura.model.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.entity.user.UserEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toanbnguyen on 6/28/2016.
 */
public class SymptomModelResponse implements EntityModelResponse{
    List<SymptomEntity> symptomEntities;
    boolean gotResponse;
    boolean responseError = false;
    String error;

    public SymptomModelResponse(List<SymptomEntity> symptomEntities) {
        this.symptomEntities = symptomEntities;
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
                symptomEntities.clear();
                symptomEntities = parseSymptom(gson, jsonObject);
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

    private List<SymptomEntity> parseSymptom(Gson gson, JSONObject jsonObject) throws JSONException {
        List<SymptomEntity> listSymptom = new ArrayList<>();
        JSONArray jsonArraySymptom = jsonObject.getJSONArray(SymptomEntity.SYMPTOM_LIST);
        for(int i = 0; i < jsonArraySymptom.length(); i++)
        {
            SymptomEntity entity = gson.fromJson(
                    jsonArraySymptom.getJSONObject(i).toString(), SymptomEntity.class);
            listSymptom.add(entity);
        }
        symptomEntities.addAll(listSymptom);
        return symptomEntities;
    }

    public List<SymptomEntity> getAllSymptom() {
        return symptomEntities;
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
