package com.kms.cura.dal.mapping;

public enum NotificationType {
	APPT_TYPE ("appt_type"), MSG_TYPE("msg_type");
	
	String typeName;
	
	private NotificationType(String typeName) {
		this.typeName = typeName;
	}

	public  String getNotiType() {
		return typeName;
	}
	

}
