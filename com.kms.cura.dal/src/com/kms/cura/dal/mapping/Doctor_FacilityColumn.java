package com.kms.cura.dal.mapping;

public enum Doctor_FacilityColumn {
	DOCTOR_ID("doctor_id"), FACILITY_ID("facility_id"), WORKING_DAY("working_day"), START_WORKING_TIME("start_working_time")
	,END_WORKING_TIME("end_working_time");
	String columnName;

	private Doctor_FacilityColumn(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "doctor_facilities";

}
