package com.kms.cura.entity;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class NotificationEntity {
	public static final String NOTI_LIST = "noti_list";
	public static final String NOTI_TYPE = "noti_type";
	public static final String APPT_NOTI_TYPE = "APPT_NOTI_TYPE";
	public static final String MSG_NOTI_TYPE = "MSG_NOTI_TYPE";
	public static final String APPT_UPDATE_STT = "update_appt";
	public static final String NEW_APPT = "new_appt";
	private String id;
	private String userID;
	private String refID;
	private boolean status;
	private String type;
	private String notiType;

	public NotificationEntity(String id, String userID, String refID, boolean status, String type) {
		this.id = id;
		this.userID = userID;
		this.refID = refID;
		this.status = status;
		this.type = type;
	}
	
	public NotificationEntity(String id, String userID, String refID, boolean status, String type, String notiType) {
		this.id = id;
		this.userID = userID;
		this.refID = refID;
		this.status = status;
		this.type = type;
		this.notiType = notiType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getRefID() {
		return refID;
	}

	public void setRefID(String refID) {
		this.refID = refID;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getNotiType() {
		return notiType;
	}

	public void setNotiType(String notiType) {
		this.notiType = notiType;
	}

	public static Type getNotificationType() {
		return new TypeToken<NotificationEntity>() {
		}.getType();
	}

	public static Type getNotificationListType() {
		return new TypeToken<List<NotificationEntity>>() {
		}.getType();
	}
}
