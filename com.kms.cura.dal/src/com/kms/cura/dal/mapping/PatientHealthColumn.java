package com.kms.cura.dal.mapping;

public enum PatientHealthColumn {
	PATIENT_ID("patient_id"), CONDITION_ID("condition_id"), SYMPTOM_ID("symptom_id"), START_DATE("start_date"), END_DATE("end_date"), NOTE("note");
	
	String columnName;
	
	private PatientHealthColumn(String columnName) {
		this.columnName = columnName;
	}

	private PatientHealthColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public  String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "patient_health";
}
