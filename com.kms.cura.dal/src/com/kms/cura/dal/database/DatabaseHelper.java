package com.kms.cura.dal.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;

import com.kms.cura.dal.mapping.EntityColumn;
import com.kms.cura.entity.Entity;
import com.mysql.jdbc.Statement;

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
		if (con != null) {
			con.close();
		}
	}

	public List<Entity> queryAll(String tableName) throws SQLException, ClassNotFoundException, IOException {
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

	public Entity queryByID(String tableName, int id) throws SQLException, ClassNotFoundException, IOException {
		return queryByID(tableName, EntityColumn.ID.getColumnName(), id);
	}

	public Entity queryByID(String tableName, String idColumnName, int id)
			throws SQLException, ClassNotFoundException, IOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM " + tableName + " WHERE " + idColumnName + " = ?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			rs.next();
			Entity result = getEntityFromResultSet(rs);
			return result;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public Entity queryByName(String tableName, String name) throws SQLException, ClassNotFoundException, IOException {
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
		stmt = con.prepareStatement("SELECT DISTINCT * FROM " + tableName + " WHERE " + referenceID + " = ?");
		stmt.setInt(1, referenceValue);
		rs = stmt.executeQuery();
		return rs;
	}

	protected abstract Entity getEntityFromResultSet(ResultSet resultSet)
			throws SQLException, ClassNotFoundException, IOException;

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

	protected PreparedStatement createInsertPreparedStatement(Map<String, Object> valueMap, String databaseName)
			throws SQLException {
		StringBuilder insertString = new StringBuilder("INSERT INTO " + databaseName + " (");
		StringBuilder valueString = new StringBuilder(") VALUES (");
		boolean isFirst = true;
		for (Entry<String, Object> entry : valueMap.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				insertString.append(",");
				valueString.append(",");
			}
			insertString.append(entry.getKey());
			valueString.append("?");
		}
		valueString.append(")");
		String statementString = insertString.toString() + valueString.toString();
		PreparedStatement stmt = con.prepareStatement(statementString, Statement.RETURN_GENERATED_KEYS);
		setPreparedStatementValue(valueMap, stmt);
		return stmt;
	}

	protected PreparedStatement createUpdatePreparedStatement(Map<String, Object> valueMap, String databaseName,
			List<String> columnUpdate, List<Object> valueUpdate) throws SQLException {
		StringBuilder updateString = new StringBuilder();
		Map<String, Object> map = new LinkedHashMap<>();
		updateString.append("UPDATE ");
		updateString.append(databaseName + " SET ");
		for (int i = 0; i < columnUpdate.size(); ++i) {
			map.put(columnUpdate.get(i), valueUpdate.get(i));
			if (i == columnUpdate.size() - 1) {
				updateString.append(columnUpdate.get(i) + " = ? WHERE ");
			} else {
				updateString.append(columnUpdate.get(i) + " = ?, ");
			}
		}
		map.putAll(valueMap);
		boolean isFirst = true;
		for (Entry<String, Object> entry : valueMap.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				updateString.append(" AND ");
			}
			updateString.append(entry.getKey());
			updateString.append(" = ");
			updateString.append("?");
		}
		PreparedStatement stmt = con.prepareStatement(updateString.toString());
		setPreparedStatementValue(map, stmt);
		return stmt;
	}

	protected PreparedStatement createSelectWherePreparedStatement(Map<String, Object> valueMap, String databaseName)
			throws SQLException {
		StringBuilder whereString = new StringBuilder("SELECT * FROM ");
		whereString.append(databaseName);
		whereString.append(" WHERE ");
		boolean isFirst = true;
		for (Entry<String, Object> entry : valueMap.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				whereString.append(" AND ");
			}
			whereString.append(entry.getKey());
			whereString.append(" = ");
			whereString.append("?");
		}
		PreparedStatement stmt = con.prepareStatement(whereString.toString());
		setPreparedStatementValue(valueMap, stmt);
		return stmt;
	}

	protected void setPreparedStatementValue(Map<String, Object> valueMap, PreparedStatement stmt) throws SQLException {
		int count = 1;
		for (Entry<String, Object> valueEntry : valueMap.entrySet()) {
			if (valueEntry.getValue() instanceof Integer) {
				stmt.setInt(count, (Integer) valueEntry.getValue());
			} else if (valueEntry.getValue() instanceof String) {
				stmt.setString(count, (String) valueEntry.getValue());
			} else if (valueEntry.getValue() instanceof Date) {
				stmt.setDate(count, (Date) valueEntry.getValue());
			} else if (valueEntry.getValue() instanceof Double) {
				stmt.setDouble(count, (Double) valueEntry.getValue());
			} else if (valueEntry.getValue() instanceof Time) {
				stmt.setTime(count, (Time) valueEntry.getValue());
			} else {
				// handle for null
				stmt.setString(count, null);
			}
			count++;
		}
	}

}
