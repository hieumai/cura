package com.kms.cura.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.FacilityDatabaseHelper;
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

	public List<Entity> getAll() throws ClassNotFoundException, SQLException, IOException {
		FacilityDatabaseHelper dbh = new FacilityDatabaseHelper();
		try{
			dbh = new FacilityDatabaseHelper();
			return super.getAll(FacilityColumn.TABLE_NAME, dbh);
		}
		finally{
			dbh.closeConnection();
		}
	}
}
