package com.kms.cura.controller;

import android.content.Context;

import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.UserModel;

import java.sql.Date;
import java.util.List;

public class UserController {
    public static void registerPatient(String name, String email, String password) {
        UserEntity entity = new UserEntity(null, name, email, password);
        UserModel.getInstace().registerPatient(entity);
    }

    public static void registerDoctor(String id, String name, String email, String password, String phone, DegreeEntity degree,
                                      List<SpecialityEntity> speciality, List<FacilityEntity> facility, String gender, Date birth) {
        DoctorUserEntity entity = new DoctorUserEntity(id, name, email, password, phone, degree, speciality, facility, gender, birth);
        UserModel.getInstace().registerDoctor(entity);
    }

    public static void userLogin(String email, String password) {
        UserEntity entity = new UserEntity(null, null, email, password);
        UserModel.getInstace().userLogin(entity);
    }

    public static boolean saveNewUser(String email, String password) {
        return UserModel.getInstace().save(new UserEntity(null, null, email, password));
    }
}
