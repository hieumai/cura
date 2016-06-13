package com.kms.cura.dal.mapping;

public enum Doctor_FacilityColumn {
	DOCTOR_ID("doctor_id"), FACILITY_ID("facility_id");
	String columnName;

	private Doctor_FacilityColumn(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "doctor_facilities";

}
