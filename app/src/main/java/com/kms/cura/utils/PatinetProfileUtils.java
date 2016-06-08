package com.kms.cura.utils;

import com.kms.cura.entity.user.PatientUserEntity;

import java.sql.Date;

/**
 * Created by linhtnvo on 6/8/2016.
 */
public class PatinetProfileUtils {
    private static PatinetProfileUtils mInstance;
    private PatientUserEntity entity;

    private PatinetProfileUtils() {
        //
    }

    public static PatinetProfileUtils getInstance() {
        if (mInstance == null) {
            mInstance = new PatinetProfileUtils();
        }
        return mInstance;
    }

    public void setData(PatientUserEntity src) {
        this.entity = src;
    }
    public PatientUserEntity getData(){
        return this.entity;
    }


}
