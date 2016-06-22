package com.kms.cura.model;

import com.android.volley.toolbox.StringRequest;
import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.model.request.DegreeModelResponse;
import com.kms.cura.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 6/16/2016.
 */
public class DegreeModel{
    private static DegreeModel instance;
    private static String tag_string_req = "string_req";
    private List<DegreeEntity> degrees = new ArrayList<>();
    private DegreeModel() {

    }

    /**
     * If run first time then need to run in separate thread
     * @return FacilityModel
     */
    public static DegreeModel getInstace() {
        if (instance == null) {
            instance = new DegreeModel();
        }
        return instance;
    }

    public List<DegreeEntity> getDegrees() {
        return degrees;
    }

    public void init() throws Exception {
        initData();
    }

    public void initData() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Settings.SERVER_URL);
        builder.append(Settings.GET_ALL_DEGREE);
        DegreeModelResponse response = new DegreeModelResponse(degrees);
        StringRequest stringRequest = RequestUtils.createRequestGET(builder.toString(),response);
        VolleyHelper.getInstance().addToRequestQueue(stringRequest,tag_string_req);
        while (!response.isGotResponse());
        if(!response.isResponseError()) {
            degrees = response.getEntityList();
            return;
        }
        throw new Exception(response.getError());
    }

}
