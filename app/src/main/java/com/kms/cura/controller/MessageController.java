package com.kms.cura.controller;

import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.MessageThreadEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.MessageModel;

import java.util.ArrayList;
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

    static public ArrayList<MessageEntity> getMessageByThread(UserEntity currentUser, String conversation) {
        List<MessageEntity> messageEntities = MessageModel.getInstance().getAllMessage();
        ArrayList<MessageThreadEntity> messageThreadEntities = MessageThreadEntity.getMessageThreadFromList(currentUser, messageEntities);
        for (MessageThreadEntity messageThreadEntity : messageThreadEntities) {
            if (messageThreadEntity.getConversationName(currentUser).equals(conversation)) {
                return messageThreadEntity.getMessageEntities();
            }
        }
        return null;
    }
}
