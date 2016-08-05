package com.kms.cura.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.entity.Entity;

public abstract class EntityDAL {

	protected List<Entity> getAll(String tableName, DatabaseHelper dbh) throws ClassNotFoundException, SQLException, IOException {
		List<Entity> result = null;
		try {
			result = dbh.queryAll(tableName);
			return result;
		} finally {
			if (dbh != null && result != null) {
				dbh.closeConnection();
			}
		}
	}

	protected Entity getByName(String tableName, String name, DatabaseHelper dbh)
			throws SQLException, ClassNotFoundException, IOException {
		try {
			Entity result = dbh.queryByName(tableName, name);
			return result;
		} finally {
			if (dbh != null) {
				dbh.closeConnection();
			}
		}
	}

	protected Entity getByID(String tableName, int id, DatabaseHelper dbh) throws SQLException, ClassNotFoundException, IOException {
		try {
			Entity result = dbh.queryByID(tableName, id);
			return result;
		} finally {
			if (dbh != null) {
				dbh.closeConnection();
			}
		}
	}
}
