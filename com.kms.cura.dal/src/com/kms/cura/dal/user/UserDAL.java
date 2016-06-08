package com.kms.cura.dal.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.EntityDAL;
import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.database.UserDatabaseHelper;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.mapping.PatientColumn;
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

	public List<Entity> getAll() throws ClassNotFoundException, SQLException {
		return super.getAll(USER_TABLE_NAME);
	}
	
	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		return new UserEntity(resultSet.getString("id"), "", resultSet.getString("email"),
				resultSet.getString("password"));
	}
	
	public Entity createUser(UserEntity entity) throws ClassNotFoundException, SQLException, DALException {
		UserDatabaseHelper dbh = null;
		try {
			dbh = new UserDatabaseHelper();
			ResultSet rs = null;
			Entity result = null;
			rs = insertUser(entity, dbh);
			if (rs != null && rs.next()) {
				result = getEntityFromResultSet(rs, dbh);
			}
			return result;
		} finally {
			if (dbh != null) {
				dbh.closeConnection();
			}
		}
	}

	protected ResultSet insertUser(UserEntity entity, UserDatabaseHelper dbh) throws SQLException, DALException {
		ResultSet rs;
		rs = dbh.insertUser(entity);
		return rs;
	}
	
	public int searchUser(UserEntity entity) throws ClassNotFoundException, SQLException{
		DatabaseHelper dbh = null;
		try {
			dbh = new DatabaseHelper();
			ResultSet rs = null;
			rs = dbh.queryByEmailPassword(USER_TABLE_NAME, entity.getEmail(), entity.getPassword());
			if(rs != null && rs.next()){
				return rs.getInt("id");
			}
			return -1;
		} finally {
			if (dbh != null) {
				dbh.closeConnection();
			}
		}
	}
	
	protected ResultSet searchUserbyID(String tableName, int id, String id_column, DatabaseHelper dbh) throws ClassNotFoundException, SQLException {
		ResultSet rs = null;
		rs = dbh.queryByID(tableName, id, id_column);
		return rs;
	}

}
