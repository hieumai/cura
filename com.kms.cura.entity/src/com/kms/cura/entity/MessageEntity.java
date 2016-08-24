package com.kms.cura.entity;

import java.sql.Timestamp;

import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;

public class MessageEntity extends Entity {
	private UserEntity sender, receiver;
	private Timestamp timeSent;
	private String message;
	public final static String MESSAGE_LIST = "messageList";
	public final static String SENT_BY_DOCTOR = "sent_by_doctor";
	public final static String SENDER = "sender";
	public final static String RECEIVER = "receiver";
	public final static String TIME_SENT = "time_sent";
	public final static String MESSAGE = "message";
	public static final String MSG_TYPE = "msg";

	public MessageEntity(String id, UserEntity sender, UserEntity receiver, Timestamp timeSent, String message) {
		super(id, null);
		this.sender = sender;
		this.receiver = receiver;
		this.timeSent = timeSent;
		this.message = message;
	}

	@Override
	public boolean equals(Object obj) {
		MessageEntity src = (MessageEntity) obj;
		return (this.sender.getId().equals(src.getSender().getId())
				&& this.receiver.getId().equals(src.getReceiver().getId()) && this.timeSent.equals(src.getTimeSent())
				&& this.message.equals(src.getMessage()));
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	public UserEntity getSender() {
		return sender;
	}

	public void setSender(UserEntity sender) {
		this.sender = sender;
	}

	public UserEntity getReceiver() {
		return receiver;
	}

	public void setReceiver(UserEntity receiver) {
		this.receiver = receiver;
	}

	public boolean isSenderDoctor() {
		return sender instanceof DoctorUserEntity;
	}

	public Timestamp getTimeSent() {
		return timeSent;
	}

	public void setTimeSent(Timestamp timeSent) {
		this.timeSent = timeSent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
