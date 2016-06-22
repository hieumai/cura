package com.kms.cura.controller;

import android.os.Bundle;

import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.model.FacilityModel;
import com.kms.cura.view.activity.RegisterDoctorActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 6/16/2016.
 */
public class FacilityController {
    public static void initData() throws Exception {
        FacilityModel.getInstace().init();
    }

    public static List<FacilityEntity> getFacilitySelected(Bundle bundle){
        boolean[] facilities = bundle.getBooleanArray(RegisterDoctorActivity.FACILITY);
        List<FacilityEntity> facilitySelected = new ArrayList<>();
        List<FacilityEntity> allFacility = FacilityModel.getInstace().getFacilities();
        for (int i = 0 ; i< allFacility.size();++i){
            if(facilities[i]){
                facilitySelected.add(allFacility.get(i));
            }
        }
        return facilitySelected;
    }
}
