package com.kms.cura.dal.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.kms.cura.dal.mapping.InsuranceColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.InsuranceEntity;

public class InsuranceDatabaseHelper extends DatabaseHelper {

	public InsuranceDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return new InsuranceEntity(resultSet.getString(InsuranceColumn.ID.getColumnName()),
				resultSet.getString(InsuranceColumn.NAME.getColumnName()));
	}

}