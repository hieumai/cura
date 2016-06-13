package com.kms.cura.controller;

import android.content.Context;

import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.UserModel;

public class UserController {
    public static void registerPatient(Context mContext, String name, String email, String password) {
        UserEntity entity = new UserEntity(null, name, email, password);
        UserModel.getInstace().registerPatient(entity, mContext);
    }

    public static boolean saveNewUser(String email, String password) {
        return UserModel.getInstace().save(new UserEntity(null, null, email, password));
    }
}
