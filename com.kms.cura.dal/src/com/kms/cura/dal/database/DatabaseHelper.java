package com.kms.cura.dal.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.EntityColumn;
import com.kms.cura.dal.mapping.UserColumn;
import com.kms.cura.entity.Entity;

public abstract class DatabaseHelper {
	private static final String USE_CURA_DATABASE = "use cura";
	private static final String CONNECTION_NAME = "jdbc:mysql://localhost:3306/";
	private static final String CONNECTION_ID = "root";
	private static final String CONNECTION_PASSWORD = "root";
	protected Connection con;

	public DatabaseHelper() throws ClassNotFoundException, SQLException {
		PreparedStatement stmt = null;
		Class.forName(com.mysql.jdbc.Driver.class.getName());
		con = DriverManager.getConnection(CONNECTION_NAME, CONNECTION_ID, CONNECTION_PASSWORD);
		try {
			stmt = con.prepareStatement(USE_CURA_DATABASE);
			stmt.executeQuery();
		} finally {
			stmt.close();
		}

	}

	public void closeConnection() throws SQLException {
		con.close();
	}

	public List<Entity> queryAll(String tableName, DatabaseHelper dbh) throws SQLException, ClassNotFoundException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Entity> result = new ArrayList<Entity>();
		try {
			stmt = con.prepareStatement("SELECT * FROM " + tableName);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Entity entity = getEntityFromResultSet(rs);
				result.add(entity);
			}
			return result;
		} finally {
			if (rs.isAfterLast()) {
				rs.close();
				stmt.close();
			}
		}
	}

	public Entity queryByID(String tableName, int id) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(
					"SELECT * FROM " + tableName + " WHERE " + UserColumn.ID.getColumnName() + " = ?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			rs.next();
			Entity result = getEntityFromResultSet(rs);
			return result;
		} finally {
			rs.close();
			stmt.close();
		}
	}

	public Entity queryByName(String tableName, String name) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(
					"SELECT * FROM " + tableName + " WHERE " + EntityColumn.NAME.getColumnName() + " = ?");
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			rs.next();
			Entity result = getEntityFromResultSet(rs);
			return result;
		} finally {
			rs.close();
			stmt.close();
		}
	}

	protected ResultSet queryByReferenceID(String tableName, String referenceID, int referenceValue)
			throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		stmt = con.prepareStatement("SELECT * FROM " + tableName + " WHERE " + referenceID + " = ?");
		stmt.setInt(1, referenceValue);
		rs = stmt.executeQuery();
		rs.next();
		return rs;
	}

	public Entity insert(String tableName, Entity entity) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement("INSERT INTO " + tableName + " (" + EntityColumn.NAME.getColumnName()
					+ ") VALUES (" + entity.getName() + ")");
			stmt.executeUpdate();

			stmt2 = con.prepareStatement("SELECT FROM " + tableName + " WHERE " + EntityColumn.NAME.getColumnName()
					+ " =" + entity.getName());
			rs = stmt2.executeQuery();
			rs.next();
			Entity result = getEntityFromResultSet(rs);
			return result;
		} finally {
			rs.close();
			stmt.close();
			stmt2.close();
		}
	}

	protected abstract Entity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException;

	public void insertReferenceRowsToReferenceTable(String database, List<ReferenceTableRow> referenceRows)
			throws SQLException {
		PreparedStatement stmt = null;
		for (ReferenceTableRow referenceRow : referenceRows) {
			try {
				stmt = con.prepareStatement("INSERT INTO " + database + "(" + referenceRow.getFirstRefKey() + ","
						+ referenceRow.getSecondRefKey() + ") VALUES (?,?)");
				stmt.setInt(1, Integer.parseInt(referenceRow.getFirstValue()));
				stmt.setInt(2, Integer.parseInt(referenceRow.getSecondValue()));
				stmt.executeUpdate();
			} finally {
				stmt.close();
			}
		}
	}
}
