package com.kms.cura.entity;

import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toanbnguyen on 7/20/2016.
 */
public class MessageThreadEntity {
    private ArrayList<MessageEntity> messageEntities;

    public MessageThreadEntity() {
        this.messageEntities = new ArrayList<>();
    }

    public void addMessage(MessageEntity entity) {
        messageEntities.add(entity);
    }

    public MessageEntity getMessage(int index) {
        return messageEntities.get(index);
    }

    public String getConversationName(UserEntity entity) {
        if (messageEntities.isEmpty()) {
            return null;
        }
        MessageEntity messageEntity = messageEntities.get(0);
        if (entity instanceof PatientUserEntity) {
            if (messageEntity.isSenderDoctor()) {
                return messageEntity.getSender().getName();
            }
            return messageEntity.getReceiver().getName();
        }
        if (entity instanceof DoctorUserEntity) {
            if (!messageEntity.isSenderDoctor()) {
                return messageEntity.getSender().getName();
            }
            return messageEntity.getReceiver().getName();
        }
        return null;
    }

    public String getFirstLine() {
        if (messageEntities.isEmpty()) {
            return null;
        }
        return messageEntities.get(0).getMessage();
    }

    public static ArrayList<MessageThreadEntity> getMessageThreadFromList(UserEntity currentUser, List<MessageEntity> messageEntities) {
        ArrayList<MessageThreadEntity> messageThreadEntities = new ArrayList<>();
            for (MessageEntity messageEntity : messageEntities) {
                addMessageToThread(currentUser, messageThreadEntities, messageEntity);
            }
        return messageThreadEntities;
    }

    private static void addMessageToThread(UserEntity currentUser, ArrayList<MessageThreadEntity> messageThreadEntities, MessageEntity messageEntity) {
        String threadName;
        if ((messageEntity.isSenderDoctor() && currentUser instanceof PatientUserEntity) || (!messageEntity.isSenderDoctor() && currentUser instanceof DoctorUserEntity)) {
            threadName = messageEntity.getSender().getName();
        } else {
            threadName = messageEntity.getReceiver().getName();
        }
        int messageIndex = getIndex(threadName, currentUser, messageThreadEntities);
        if (messageIndex == -1) {
            // not existed yet
            MessageThreadEntity messageThreadEntity = new MessageThreadEntity();
            messageThreadEntity.addMessage(messageEntity);
            messageThreadEntities.add(messageThreadEntity);
        } else {
            messageThreadEntities.get(messageIndex).addMessage(messageEntity);
        }
    }

    private static int getIndex(String name, UserEntity entity, ArrayList<MessageThreadEntity> messageThreadEntities) {
        for (MessageThreadEntity messageThreadEntity : messageThreadEntities) {
            if (name.equals(messageThreadEntity.getConversationName(entity))) {
                return messageThreadEntities.indexOf(messageThreadEntity);
            }
        }
        return -1;
    }

    public int getSize() {
        return messageEntities.size();
    }
}
