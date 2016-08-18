package com.kms.cura.dal.mapping;

public enum PasswordCodeColumn {
	USER_ID("user_id"), CODE("code"), REQUEST_TIME("request_time");

	String columnName;

	private PasswordCodeColumn(String columnName) {
		this.columnName = columnName;
	}

	private PasswordCodeColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "password_code";
}
