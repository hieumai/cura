package com.kms.cura.utils;

import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by linhtnvo on 6/8/2016.
 */
public class DoctorProfileUtils {
    private static DoctorProfileUtils mInstance;
    private DoctorUserEntity entity;

    private DoctorProfileUtils() {
        //
    }

    public static DoctorProfileUtils getInstance() {
        if (mInstance == null) {
            mInstance = new DoctorProfileUtils();
        }
        return mInstance;
    }

    public void setData(DoctorUserEntity src) {
        this.entity = src;
    }
}