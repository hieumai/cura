package com.kms.cura.dal.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.entity.Entity;

public class DatabaseHelper {
	private static final String USE_CURA_DATABASE = "use cura";
	private static final String CONNECTION_NAME = "jdbc:mysql://localhost:3306/";
	private static final String CONNECTION_ID = "root";
	private static final String CONNECTION_PASSWORD = "root";
	protected Connection con;
	protected PreparedStatement stmt;

	public DatabaseHelper() throws ClassNotFoundException, SQLException {
		Class.forName(com.mysql.jdbc.Driver.class.getName());
		con = DriverManager.getConnection(CONNECTION_NAME, CONNECTION_ID, CONNECTION_PASSWORD);
		stmt = con.prepareStatement(USE_CURA_DATABASE);
		stmt.executeQuery();
	}

	public void closeConnection() throws SQLException {
		con.close();
	}

	public ResultSet queryAll(String tableName) throws SQLException {
		ResultSet rs = null;
		stmt = con.prepareStatement("SELECT * FROM " + tableName);
		rs = stmt.executeQuery();
		return rs;
	}

	public ResultSet queryByID(String tableName, int id) throws SQLException {
		ResultSet rs = null;
		stmt = con.prepareStatement("SELECT * FROM " + tableName + " WHERE id = ?");
		stmt.setInt(1, id);
		rs = stmt.executeQuery();
		return rs;
	}
	
	public ResultSet queryByID(String tableName, int id, String id_column) throws SQLException {
		ResultSet rs = null;
		stmt = con.prepareStatement("SELECT * FROM " + tableName + " WHERE "+id_column+" = ?");
		stmt.setInt(1, id);
		rs = stmt.executeQuery();
		return rs;
	}
	public ResultSet queryByName(String tableName, String name) throws SQLException {
		ResultSet rs = null;
		stmt = con.prepareStatement("SELECT * FROM " + tableName + " WHERE name = ?");
		stmt.setString(1, name);
		rs = stmt.executeQuery();
		return rs;
	}

	public ResultSet queryByReferenceID(String tableName, String referenceID, int referenceValue) throws SQLException {
		ResultSet rs = null;
		stmt = con.prepareStatement("SELECT * FROM " + tableName + " WHERE " + referenceID + " = ?");
		stmt.setInt(1, referenceValue);
		rs = stmt.executeQuery();
		return rs;
	}
	public ResultSet queryByEmailPassword(String tableName, String email, String password) throws SQLException {
		ResultSet rs = null;
		stmt = con.prepareStatement("SELECT * FROM " + tableName + " WHERE email = ? AND password = ?");
		stmt.setString(1, email);
		stmt.setString(2, password);
		rs = stmt.executeQuery();
		return rs;
	}
	
	
	public ResultSet insert(String tableName, Entity entity) throws SQLException {
		ResultSet rs = null;
		stmt = con.prepareStatement("INSERT INTO " + tableName + " (name) VALUES (" + entity.getName() + ")");
		stmt.executeUpdate();
		stmt = con.prepareStatement("SELECT FROM " + tableName + " WHERE name =" + entity.getName());
		rs = stmt.executeQuery();
		return rs;
	}

	public void insertReferenceRowsToReferenceTable(String database, List<ReferenceTableRow> referenceRows)
			throws SQLException {
		for (ReferenceTableRow referenceRow : referenceRows) {
			stmt = con.prepareStatement("INSERT INTO " + database + "(" + referenceRow.getFirstRefKey() + ","
					+ referenceRow.getSecondRefKey() + ") VALUES (?,?)");
			stmt.setInt(1, Integer.parseInt(referenceRow.getFirstRefValue()));
			stmt.setInt(2, Integer.parseInt(referenceRow.getSecondRefValue()));
			stmt.executeUpdate();
		}
	}
}
