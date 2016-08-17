package com.kms.cura.dal;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.ConditionDatabaseHelper;
import com.kms.cura.entity.ConditionEntity;
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

	public List<Entity> getAll() throws ClassNotFoundException, SQLException, IOException {
		ConditionDatabaseHelper dbh = null;
		try{
			dbh = new ConditionDatabaseHelper();
			return dbh.queryAll();
		}
		finally{
			dbh.closeConnection();
		}
	}

	public List<ConditionEntity> getAssociatedCondition(SymptomEntity entity)
			throws SQLException, ClassNotFoundException {
		ConditionDatabaseHelper dbh = null;
		try{
			dbh = new ConditionDatabaseHelper();
			return dbh.queryAssociatedCondition(entity);
		}
		finally{
			dbh.closeConnection();
		}
	}
}
