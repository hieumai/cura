package com.kms.cura.dal.mapping;

public enum DegreeColumn {
	ID(EntityColumn.ID), NAME(EntityColumn.NAME);
	String columnName;

	private DegreeColumn(String columnName) {
		this.columnName = columnName;
	}
	
	private DegreeColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public final String getColumnName() {
		return columnName;
	}
	public static final String TABLE_NAME = "degree";

}
