package com.kms.cura.controller;

import android.os.Bundle;

import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.model.DegreeModel;
import com.kms.cura.view.activity.RegisterDoctorActivity;


/**
 * Created by linhtnvo on 6/16/2016.
 */
public class DegreeController {
    public static void initData() throws Exception {
        DegreeModel.getInstace().init();
    }

    public static DegreeEntity getDegreeSelected(Bundle bundle){
        int degree = bundle.getInt(RegisterDoctorActivity.DEGREE);
        return DegreeModel.getInstace().getDegrees().get(degree);
    }
}
