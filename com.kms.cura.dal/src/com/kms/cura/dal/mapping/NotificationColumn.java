package com.kms.cura.dal.mapping;

public enum NotificationColumn {
	NOTI_ID("id"),USER_ID("user_id"), REF_ID("ref_id"), STATUS("status"), APPT_NOTI_TYPE("appt_noti_type");

	String columnName;

	private NotificationColumn(String columnName) {
		this.columnName = columnName;
	}

	private NotificationColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME_APPT_TYPE = "appt_notification";
	public static final String TABLE_NAME_MSG_TYPE = "msg_notification";
}
