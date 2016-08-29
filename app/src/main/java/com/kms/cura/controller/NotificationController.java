package com.kms.cura.controller;

import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.NotificationModel;

import org.json.JSONException;

import java.util.List;

/**
 * Created by linhtnvo on 8/23/2016.
 */
public class NotificationController {
    public static List<NotificationEntity> getApptNotification (UserEntity entity) throws Exception {
        return NotificationModel.getInstace().getApptNotification(entity);
    }

    public static List<NotificationEntity> getMsgNotification (UserEntity entity) throws Exception {
        return NotificationModel.getInstace().getMsgNotification(entity);
    }
}
