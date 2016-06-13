package com.kms.cura.dal.mapping;

public enum InsuranceColumn {
	ID(EntityColumn.ID), NAME(EntityColumn.NAME);
	String columnName;

	private InsuranceColumn(String columnName) {
		this.columnName = columnName;
	}
	
	private InsuranceColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public final String getColumnName() {
		return columnName;
	}
	public static final String TABLE_NAME = "insurance";
}
