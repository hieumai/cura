package com.kms.cura.dal.mapping;

public enum Doctor_SpecialityColumn {
	DOCTOR_ID("doctor_id"), SPECIALITY_ID("speciality_id");
	String columnName;

	private Doctor_SpecialityColumn(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "doctor_specialties";

}
