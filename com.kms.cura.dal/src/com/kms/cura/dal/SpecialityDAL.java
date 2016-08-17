package com.kms.cura.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.database.SpecialityDatabaseHelper;
import com.kms.cura.dal.mapping.SpecialityColumn;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SpecialityEntity;

public class SpecialityDAL extends EntityDAL {
	private static SpecialityDAL _instance;

	private SpecialityDAL() {
		// hide constructor
	}

	public static SpecialityDAL getInstance() {
		if (_instance == null) {
			_instance = new SpecialityDAL();
		}
		return _instance;
	}

	public List<Entity> getAll() throws ClassNotFoundException, SQLException, IOException {
		SpecialityDatabaseHelper dbh = new SpecialityDatabaseHelper();
		try{
			return super.getAll(SpecialityColumn.TABLE_NAME, dbh);
		}
		finally{
			dbh.closeConnection();
		}
	}
	
	
	public List<SpecialityEntity> getByCondition(ConditionEntity entity) throws ClassNotFoundException, SQLException {
		SpecialityDatabaseHelper dbh = null;
		try{
			dbh = new SpecialityDatabaseHelper();
			return dbh.querySpecialityByCondition(entity);
		}
		finally{
			dbh.closeConnection();
		}
	}
	
}
