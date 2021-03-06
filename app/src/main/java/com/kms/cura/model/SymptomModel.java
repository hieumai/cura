package com.kms.cura.model;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.model.request.SymptomModelResponse;
import com.kms.cura.utils.RequestUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toanbnguyen on 6/28/2016.
 */
public class SymptomModel {
    private static SymptomModel instance;
    private static String tag_string_req = "string_req";
    private List<SymptomEntity> symptomEntities = new ArrayList<>();
    private SymptomModel() {

    }

    public static SymptomModel getInstance() {
        if (instance == null) {
            instance = new SymptomModel();
        }
        return instance;
    }

    public List<SymptomEntity> getAllSymptom() {
        return symptomEntities;
    }

    public void initData() throws Exception {
        StringBuilder symptomRequest = new StringBuilder();
        symptomRequest.append(Settings.SERVER_URL);
        symptomRequest.append(Settings.GET_ALL_SYMPTOM);
        SymptomModelResponse response = new SymptomModelResponse(symptomEntities);
        StringRequest stringRequest = RequestUtils.createRequestGET(symptomRequest.toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            symptomEntities = response.getAllSymptom();
            return;
        }
        throw new Exception(response.getError());
    }

    public List<SymptomEntity> getAssociatedSymtom(ConditionEntity entity) throws Exception{
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.GET_ASSOCIATED_SYMPTOM);
        List<SymptomEntity> symptomEntities = new ArrayList<>();
        SymptomModelResponse response = new SymptomModelResponse(symptomEntities);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(entity).toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            symptomEntities = response.getAllSymptom();
            return symptomEntities;
        }
        throw new Exception(response.getError());
    }
}

