package com.kms.cura.dal.mapping;

public enum OpeningHourColumn {
	WEEK_DAY("week_day"), FACILITY_ID("facility_id"), TIME_OPEN("time_open"), TIME_CLOSE("time_close");
	String columnName;

	private OpeningHourColumn(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "opening_hours";
}
