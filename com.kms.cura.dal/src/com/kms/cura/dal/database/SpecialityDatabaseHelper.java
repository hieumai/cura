package com.kms.cura.dal.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.kms.cura.dal.mapping.DegreeColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SpecialityEntity;

public class SpecialityDatabaseHelper extends DatabaseHelper{

	public SpecialityDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return new SpecialityEntity(resultSet.getString(DegreeColumn.ID.getColumnName()), resultSet.getString(DegreeColumn.NAME.getColumnName()));
	}

}
