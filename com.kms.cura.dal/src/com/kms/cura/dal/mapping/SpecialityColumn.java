package com.kms.cura.dal.mapping;

public enum SpecialityColumn {
	ID(EntityColumn.ID), NAME(EntityColumn.NAME);
	String columnName;

	private SpecialityColumn(String columnName) {
		this.columnName = columnName;
	}

	private SpecialityColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public final String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "specialties";

}
