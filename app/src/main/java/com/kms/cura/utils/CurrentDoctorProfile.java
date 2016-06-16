package com.kms.cura.utils;

import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by linhtnvo on 6/8/2016.
 */
public class CurrentDoctorProfile {
    private static CurrentDoctorProfile mInstance;
    private DoctorUserEntity entity;

    private CurrentDoctorProfile() {
        //
    }

    public static CurrentDoctorProfile getInstance() {
        if (mInstance == null) {
            mInstance = new CurrentDoctorProfile();
        }
        return mInstance;
    }

    public void setData(DoctorUserEntity src) {
        this.entity = src;
    }

    public DoctorUserEntity getEntity() {
        return entity;
    }

}