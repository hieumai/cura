package com.kms.cura.dal.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.kms.cura.dal.mapping.SymptomColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SymptomEntity;

public class SymptomDatabaseHelper extends DatabaseHelper {

	public SymptomDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return new SymptomEntity(resultSet.getString(SymptomColumn.ID.getColumnName()),
				resultSet.getString(SymptomColumn.NAME.getColumnName()));
	}
}
