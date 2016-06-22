package com.kms.cura.controller;

import android.os.Bundle;

import com.android.volley.VolleyError;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.model.SpecialityModel;
import com.kms.cura.view.activity.RegisterDoctorActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhtnvo on 6/15/2016.
 */
public class SpecialityController {

    public static void initData() throws Exception {
        SpecialityModel.getInstace().init();

    }

    public static List<SpecialityEntity> getSpecialitySelected(Bundle bundle){
        boolean[] specialities = bundle.getBooleanArray(RegisterDoctorActivity.SPECIALITY);
        List<SpecialityEntity> specialitySelected = new ArrayList<>();
        List<SpecialityEntity> allSpecialities = SpecialityModel.getInstace().getSpecialities();
        for (int i = 0 ; i< allSpecialities.size();++i){
            if(specialities[i]){
                specialitySelected.add(allSpecialities.get(i));
            }
        }
        return specialitySelected;
    }

}
