package com.kms.cura.dal.mapping;

public enum MessageColumn {
	PATIENT_ID("patient_id"), DOCTOR_ID("doctor_id"), SENT_BY_DOCTOR("sent_by_doctor"), TIME_SENT("time_sent"), 
		MESSAGE("message"), PATIENT_AVAILABLE("patient_available"), DOCTOR_AVAILABLE("doctor_available");
	
	String columnName;
	
	private MessageColumn(String columnName) {
		this.columnName = columnName;
	}

	private MessageColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public  String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "messages";
}
