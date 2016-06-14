package com.kms.cura.dal.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.kms.cura.dal.mapping.DegreeColumn;
import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.entity.Entity;

public class DegreeDatabaseHelper extends DatabaseHelper {

	public DegreeDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return new DegreeEntity(resultSet.getString(DegreeColumn.ID.getColumnName()), resultSet.getString(DegreeColumn.NAME.getColumnName()));	}

}
