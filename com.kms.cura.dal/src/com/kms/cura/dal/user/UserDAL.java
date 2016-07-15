package com.kms.cura.dal.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.EntityDAL;
import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.database.UserDatabaseHelper;
import com.kms.cura.dal.mapping.UserColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.UserEntity;

public class UserDAL extends EntityDAL {
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

	public List<Entity> getAll(DatabaseHelper dbh) throws ClassNotFoundException, SQLException, IOException {
		return super.getAll(UserColumn.TABLE_NAME, dbh);
	}

	public UserEntity updatePhoto(UserEntity user) throws ClassNotFoundException, SQLException, IOException {
		UserDatabaseHelper dbh = new UserDatabaseHelper();
		try {
			return dbh.updateProfile(user);
		} finally {
			dbh.closeConnection();
		}
	}
	
	public UserEntity updatePassword(UserEntity user) throws ClassNotFoundException, SQLException, IOException {
		UserDatabaseHelper dbh = new UserDatabaseHelper();
		try {
			return dbh.updatePassword(user);
		} finally {
			dbh.closeConnection();
		}
	}
}
