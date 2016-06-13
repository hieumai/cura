package com.kms.cura.dal.mapping;

public enum EntityColumn {
	ID("id"), NAME("name");

	String columnName;

	private EntityColumn(String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumnName() {
		return columnName;
	}
}
