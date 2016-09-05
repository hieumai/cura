package com.kms.cura.controller;

import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.NotificationModel;
import android.content.Context;

import com.kms.cura.model.RegisterNotificationModel;

import java.util.List;

/**
 * Created by linhtnvo on 8/8/2016.
 */
public class NotificationController {
    public static void registerGCM(Context mContext, String regName, String regID ){
        RegisterNotificationModel.getInstance().registerGCM(regID, regName, mContext);
    }

    public static void unregisterGCM(String regName) throws Exception {
        RegisterNotificationModel.getInstance().unregisterGCM(regName);
    }
    
    public static List<NotificationEntity> getApptNotification (UserEntity entity) throws Exception {
        return NotificationModel.getInstace().getApptNotification(entity);
    }
    
    public static List<NotificationEntity> getMsgNotification (UserEntity entity) throws Exception {
        return NotificationModel.getInstace().getMsgNotification(entity);
    }
    public static void updateApptNoti (NotificationEntity entity) throws Exception {
        NotificationModel.getInstace().updateApptNoti(entity);
    }

    public static void updateMsgNoti(NotificationEntity entity) throws Exception {
        NotificationModel.getInstace().updateMsgNoti(entity);
    }
}
