package com.kms.cura.dal.mapping;

public enum ConditionColumn {
	ID(EntityColumn.ID), NAME(EntityColumn.NAME), DESCRIPTION("description");
	String columnName;

	private ConditionColumn(String columnName) {
		this.columnName = columnName;
	}

	private ConditionColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public final String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "conditions";
}
