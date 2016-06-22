package com.kms.cura.model;

import com.android.volley.toolbox.StringRequest;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.model.request.FacilityModelResponse;
import com.kms.cura.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 6/16/2016.
 */
public class FacilityModel{
    private static FacilityModel instance;
    private static String tag_string_req = "string_req";
    private List<FacilityEntity> facilities = new ArrayList<>();
    private FacilityModel() {

    }

    /**
     * If run first time then need to run in separate thread
     * @return FacilityModel
     */
    public static FacilityModel getInstace() {
        if (instance == null) {
            instance = new FacilityModel();
        }
        return instance;
    }

    public List<FacilityEntity> getFacilities() {
        return facilities;
    }

    public void init() throws Exception {
        initData();
    }

    public void initData() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.GET_ALL_FACILITY);
        FacilityModelResponse response = new FacilityModelResponse(facilities);
        StringRequest stringRequest = RequestUtils.createRequestGET(builder.toString(),response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest,tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            facilities = response.getEntityList();
            return;
        }
        throw new Exception(response.getError());
    }

}
