package com.kms.cura.controller;

import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.MessageModel;

import java.util.List;

/**
 * Created by toanbnguyen on 7/25/2016.
 */
public class MessageController {

    static public List<MessageEntity> loadMessage(UserEntity entity) throws Exception {
        MessageModel.getInstance().loadMessage(entity);
        return MessageModel.getInstance().getAllMessage();
    }

    static public boolean deleteMessage(UserEntity userEntity, MessageEntity entity) throws Exception {
        return MessageModel.getInstance().deleteMessage(userEntity, entity);
    }
}
