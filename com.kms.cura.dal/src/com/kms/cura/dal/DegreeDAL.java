package com.kms.cura.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DegreeDatabaseHelper;
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

	public List<Entity> getAll() throws ClassNotFoundException, SQLException, IOException {
		return super.getAll(DegreeColumn.TABLE_NAME, new DegreeDatabaseHelper());
	}

}
