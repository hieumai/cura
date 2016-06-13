package com.kms.cura.dal.user;

import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.EntityDAL;
import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.database.UserDatabaseHelper;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.UserEntity;

public class UserDAL extends EntityDAL {
	private static final String USER_TABLE_NAME = "Users";
	private static UserDAL _instance;

	protected UserDAL() {
		// hide constructor
	}

	public static UserDAL getInstance() {
		if (_instance == null) {
			_instance = new UserDAL();
		}
		return _instance;
	}

	public List<Entity> getAll(DatabaseHelper dbh) throws ClassNotFoundException, SQLException {
		return super.getAll(USER_TABLE_NAME, dbh);
	}

	public Entity createUser(UserEntity entity) throws ClassNotFoundException, SQLException, DALException {
		UserDatabaseHelper dbh = null;
		try {
			dbh = new UserDatabaseHelper();
			Entity result = null;
			result = insertUser(entity);
			return result;
		} finally {
			if (dbh != null) {
				dbh.closeConnection();
			}
		}
	}

	protected UserEntity insertUser(UserEntity Userentity) throws SQLException, DALException, ClassNotFoundException {
		UserEntity entity;
		UserDatabaseHelper dbh = new UserDatabaseHelper();
		entity = dbh.insertUser(Userentity);
		return entity;
	}

}
