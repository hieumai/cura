package com.kms.cura.dal.mapping;

public enum DoctorColumn {
	USER_ID(UserColumn.USER_ID), NAME(UserColumn.NAME), PHONE("phone"), DEGREE_ID(
			"degree_id"), RATING("rating"), EXPERIENCE("experience"), MIN_PRICE("price_min"), MAX_PRICE(
					"price_max"), GENDER("gender"), BIRTH("birth"), LOCATION("location"), INSURANCE("insurance");

	String columnName;

	private DoctorColumn(String columnName) {
		this.columnName = columnName;
	}

	private DoctorColumn(UserColumn userColumn) {
		this.columnName = userColumn.getColumnName();
	}

	public final String getColumnName() {
		return columnName;
	}
	
	public static final String TABLE_NAME = "Doctor";
}