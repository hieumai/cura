package com.kms.cura.model;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.model.request.ConditionModelResponse;
import com.kms.cura.utils.RequestUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toanbnguyen on 6/28/2016.
 */
public class ConditionModel {
    private static ConditionModel instance;
    private static String tag_string_req = "string_req";
    private List<ConditionEntity> conditionEntities = new ArrayList<>();
    private ConditionModel() {

    }

    public static ConditionModel getInstance() {
        if (instance == null) {
            instance = new ConditionModel();
        }
        return instance;
    }

    public List<ConditionEntity> getAllCondition() {
        return conditionEntities;
    }

    public void initData() throws Exception {
        StringBuilder conditionRequest = new StringBuilder();
        conditionRequest.append(Settings.SERVER_URL);
        conditionRequest.append(Settings.GET_ALL_CONDITION);
        ConditionModelResponse response = new ConditionModelResponse(conditionEntities);
        StringRequest stringRequest = RequestUtils.createRequestGET(conditionRequest.toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            conditionEntities = response.getAllCondition();
            return;
        }
        throw new Exception(response.getError());
    }

    public List<ConditionEntity> getAssociatedCondition(SymptomEntity entity) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.GET_ASSOCIATED_CONDITION);
        List<ConditionEntity> conditionEntities = new ArrayList<>();
        ConditionModelResponse response = new ConditionModelResponse(conditionEntities);
        StringRequest stringRequest = RequestUtils.createRequest(builder.toString(), Request.Method.POST,
                EntityToJsonConverter.convertEntityToJson(entity).toString(), response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            conditionEntities = response.getAllCondition();
            return conditionEntities;
        }
        throw new Exception(response.getError());
    }
}
