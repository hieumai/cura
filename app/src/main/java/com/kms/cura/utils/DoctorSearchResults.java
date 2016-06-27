package com.kms.cura.utils;

import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;

import java.util.List;

/**
 * Created by duyhnguyen on 6/24/2016.
 */
public class DoctorSearchResults {
    private static DoctorSearchResults mInstance;
    private List<DoctorUserEntity> result;

    private DoctorSearchResults() {
        //
    }

    public static DoctorSearchResults getInstance() {
        if (mInstance == null) {
            mInstance = new DoctorSearchResults();
        }
        return mInstance;
    }

    public void setData(List<DoctorUserEntity> src) {
        this.result = src;
    }

    public List<DoctorUserEntity> getResult() {
        return result;
    }



}
