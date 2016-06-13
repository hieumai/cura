package com.kms.cura.dal;

import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.mapping.FacilityColumn;
import com.kms.cura.entity.Entity;

public class FacilityDAL extends EntityDAL {
	private static FacilityDAL _instance;

	private FacilityDAL() {
		// hide constructor
	}

	public static FacilityDAL getInstance() {
		if (_instance == null) {
			_instance = new FacilityDAL();
		}
		return _instance;
	}

	public List<Entity> getAll(DatabaseHelper dbh) throws ClassNotFoundException, SQLException {
		return super.getAll(FacilityColumn.TABLE_NAME, dbh);
	}

	public Entity getByName(String name, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByName(FacilityColumn.TABLE_NAME, name, dbh);
	}

	public Entity getByID(int id, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByID(FacilityColumn.TABLE_NAME, id, dbh);
	}

}
