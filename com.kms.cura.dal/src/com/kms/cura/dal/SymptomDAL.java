package com.kms.cura.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.mapping.InsuranceColumn;
import com.kms.cura.dal.mapping.SymptomColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SymptomEntity;

public class SymptomDAL extends EntityDAL {
	private static SymptomDAL _instance;

	private SymptomDAL() {
		// hide constructor
	}

	public static SymptomDAL getInstance() {
		if (_instance == null) {
			_instance = new SymptomDAL();
		}
		return _instance;
	}

	public List<Entity> getAll(DatabaseHelper dbh) throws ClassNotFoundException, SQLException {
		return super.getAll(SymptomColumn.TABLE_NAME, dbh);
	}

	public Entity getByName(String name, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByName(SymptomColumn.TABLE_NAME, name, dbh);
	}

	public Entity getByID(int id, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByID(SymptomColumn.TABLE_NAME, id, dbh);
	}
}
