package com.kms.cura.dal.mapping;

public enum PatientColumn {
	USER_ID(UserColumn.USER_ID), NAME(UserColumn.NAME), GENDER("gender"), BIRTH("birth"), LOCATION(
			"location"), INSURANCE("insurance"), HEALTH_CONCERN("health_concern");

	String columnName;

	private PatientColumn(String columnName) {
		this.columnName = columnName;
	}

	private PatientColumn(UserColumn userColumn) {
		this.columnName = userColumn.getColumnName();
	}

	public String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "patient";

}