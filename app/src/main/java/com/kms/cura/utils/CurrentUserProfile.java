package com.kms.cura.utils;

import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;

/**
 * Created by linhtnvo on 6/8/2016.
 */
public class CurrentUserProfile {
    private static CurrentUserProfile mInstance;
    private UserEntity entity;

    private CurrentUserProfile() {
        //
    }

    public static CurrentUserProfile getInstance() {
        if (mInstance == null) {
            mInstance = new CurrentUserProfile();
        }
        return mInstance;
    }

    public void setData(UserEntity src) {
        this.entity = src;
    }

    public UserEntity getEntity() {
        return entity;
    }

    public boolean isDoctor() {
        return (entity instanceof DoctorUserEntity);
    }

    public boolean isPatient() {
        return (entity instanceof PatientUserEntity);
    }

}
