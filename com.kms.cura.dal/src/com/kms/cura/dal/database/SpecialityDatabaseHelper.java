package com.kms.cura.dal.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.DegreeColumn;
import com.kms.cura.dal.mapping.Doctor_SpecialityColumn;
import com.kms.cura.dal.mapping.SpecialityColumn;
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
