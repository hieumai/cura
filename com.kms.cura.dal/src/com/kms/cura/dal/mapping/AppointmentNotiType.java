package com.kms.cura.dal.mapping;

public enum AppointmentNotiType {
	UPDATE_STT ("update_stt"), NEW_APPT("new_appt");
	
	String typeName;
	
	private AppointmentNotiType(String typeName) {
		this.typeName = typeName;
	}

	public  String getNotiType() {
		return typeName;
	}

}
