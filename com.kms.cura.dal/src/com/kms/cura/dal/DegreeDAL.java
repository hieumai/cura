package com.kms.cura.dal;

import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.mapping.DegreeColumn;
import com.kms.cura.entity.Entity;

public class DegreeDAL extends EntityDAL {
	private static DegreeDAL _instance;

	private DegreeDAL() {
		// hide constructor
	}

	public static DegreeDAL getInstance() {
		if (_instance == null) {
			_instance = new DegreeDAL();
		}
		return _instance;
	}

	public List<Entity> getAll(DatabaseHelper dbh) throws ClassNotFoundException, SQLException {
		return super.getAll(DegreeColumn.TABLE_NAME, dbh);
	}

	public Entity getByName(String name, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByName(DegreeColumn.TABLE_NAME, name, dbh);
	}

	public Entity getByID(int id, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return super.getByID(DegreeColumn.TABLE_NAME, id, dbh);
	}

}
