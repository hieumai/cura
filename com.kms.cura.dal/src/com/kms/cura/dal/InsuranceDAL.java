package com.kms.cura.dal;

import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.mapping.DegreeColumn;
import com.kms.cura.dal.mapping.InsuranceColumn;
import com.kms.cura.entity.Entity;

public class InsuranceDAL extends EntityDAL {
	private static InsuranceDAL _instance;

	private InsuranceDAL() {
		// hide constructor
	}

	public static InsuranceDAL getInstance() {
		if (_instance == null) {
			_instance = new InsuranceDAL();
		}
		return _instance;
	}
	
	public List<Entity> getAll(DatabaseHelper dbh) throws ClassNotFoundException, SQLException {
		return super.getAll(InsuranceColumn.TABLE_NAME, dbh);
	}

	public Entity getByName(String name, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByName(InsuranceColumn.TABLE_NAME, name, dbh);
	}

	public Entity getByID(int id, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByID(InsuranceColumn.TABLE_NAME, id, dbh);
	}
}
