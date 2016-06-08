package com.kms.cura.dal.mapping;

public enum SymptomColumn {
	ID(EntityColumn.ID), NAME(EntityColumn.NAME);
	String columnName;

	private SymptomColumn(String columnName) {
		this.columnName = columnName;
	}
	
	private SymptomColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public final String getColumnName() {
		return columnName;
	}
	public static final String TABLE_NAME = "symptoms";
}
