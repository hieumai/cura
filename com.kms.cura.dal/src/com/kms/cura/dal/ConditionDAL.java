package com.kms.cura.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.mapping.ConditionColumn;
import com.kms.cura.dal.mapping.InsuranceColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SymptomEntity;

public class ConditionDAL extends EntityDAL {
	private static ConditionDAL _instance;

	private ConditionDAL() {
		// hide constructor
	}

	public static ConditionDAL getInstance() {
		if (_instance == null) {
			_instance = new ConditionDAL();
		}
		return _instance;
	}

	public List<Entity> getAll(DatabaseHelper dbh) throws ClassNotFoundException, SQLException {
		return super.getAll(ConditionColumn.TABLE_NAME, dbh);
	}

	public Entity getByName(String name, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByName(ConditionColumn.TABLE_NAME, name, dbh);
	}

	public Entity getByID(int id, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByID(ConditionColumn.TABLE_NAME, id, dbh);
	}
}
