package com.kms.cura.model;

import com.android.volley.toolbox.StringRequest;
import com.kms.cura.entity.InsuranceEntity;
import com.kms.cura.model.request.FacilityModelResponse;
import com.kms.cura.model.request.InsuranceModelResponse;
import com.kms.cura.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duyhnguyen on 6/23/2016.
 */
public class InsuranceModel {
    private static InsuranceModel instance;
    private static String tag_string_req = "string_req";
    private List<InsuranceEntity> insurances = new ArrayList<>();
    private InsuranceModel() {

    }

    /**
     * If run first time then need to run in separate thread
     * @return FacilityModel
     */
    public static InsuranceModel getInstace() {
        if (instance == null) {
            instance = new InsuranceModel();
        }
        return instance;
    }

    public List<InsuranceEntity> getFacilities() {
        return insurances;
    }

    public void init() throws Exception {
        initData();
    }

    public void initData() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
//        builder.append(Settings.GET_ALL_);
        InsuranceModelResponse response = new InsuranceModelResponse(insurances);
        StringRequest stringRequest = RequestUtils.createRequestGET(builder.toString(),response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest,tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            insurances = response.getEntityList();
            return;
        }
        throw new Exception(response.getError());
    }

}