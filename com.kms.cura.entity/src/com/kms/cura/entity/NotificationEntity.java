package com.kms.cura.entity;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class NotificationEntity {
	public static String PATIENT_REQUEST_TYPE = "patient_request";
	public static String REG_ID = "regId";
	public static String REG_NAME = "regName";
	public static final String NOTI_LIST = "noti_list";
	public static final String NOTI_TYPE = "noti_type";
	public static final String USER_ID = "user_id";
	public static final String MSG_TYPE = "msg";
	public static final String APPT_TYPE = "appt";
	public static final String REF_ENTITY = "refEntity";
	private String id;
	private Entity refEntity;
	private boolean status;
	private String notiType;

	public NotificationEntity(String id, Entity refEntity, boolean status) {
		this.id = id;
		this.refEntity = refEntity;
		this.status = status;
	}
	
	public NotificationEntity(String id, Entity refEntity, boolean status, String notiType) {
		this.id = id;
		this.refEntity = refEntity;
		this.status = status;
		this.notiType = notiType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Entity getRefEntity() {
		return refEntity;
	}

	public void setRefEntity(Entity refEntity) {
		this.refEntity = refEntity;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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
	
	public boolean isMsgType(){
		return refEntity instanceof MessageEntity;
	}
}
