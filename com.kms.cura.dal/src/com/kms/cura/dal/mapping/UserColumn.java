package com.kms.cura.dal.mapping;

public enum UserColumn {
	ID(EntityColumn.ID), USER_ID("user_id"), NAME("name"), EMAIL("email"), PASSWORD("password"), IMAGE_PATH("imagePath");

	String columnName;

	private UserColumn(String columnName) {
		this.columnName = columnName;
	}

	private UserColumn(EntityColumn entityColumn) {
		this.columnName = entityColumn.getColumnName();
	}

	public String getColumnName() {
		return columnName;
	}

	public static final String TABLE_NAME = "Users";
	
	@Override
	public String toString() {
		return columnName;
	}
}
