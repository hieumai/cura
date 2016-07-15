package com.kms.cura.dal.mapping;

public enum PhotoColumn {
	ID("ID"), USER_ID(UserColumn.ID), PATH("path");
	
	String columnName;

	private PhotoColumn(String columnName) {
		this.columnName = columnName;
	}

	private PhotoColumn(UserColumn userColumn) {
		this.columnName = userColumn.getColumnName();
	}

	public final String getColumnName() {
		return columnName;
	}
	public static final String TABLE_NAME = "photo";

}
