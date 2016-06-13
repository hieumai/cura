package com.kms.cura.dal.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.exception.DuplicatedUserEmailException;
import com.kms.cura.dal.mapping.UserColumn;
import com.kms.cura.entity.user.UserEntity;

public class UserDatabaseHelper extends DatabaseHelper {
	public UserDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	public UserEntity insertUser(UserEntity entity) throws SQLException, DALException, ClassNotFoundException {
		PreparedStatement stmt = null, stmt2 = null;
		ResultSet rs = null;
		if (!(entity instanceof UserEntity)) {
			return null;
		}
		try {
			con.setAutoCommit(false);

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM ");
			sb.append(UserColumn.TABLE_NAME);
			sb.append(" WHERE ");
			sb.append(UserColumn.EMAIL.getColumnName());
			sb.append(" = ?");

			stmt = con.prepareStatement(sb.toString());
			stmt.setString(1, entity.getEmail());
			rs = stmt.executeQuery();
			if (rs.next()) {
				throw new DuplicatedUserEmailException(entity.getEmail());
			}

			sb.setLength(0);
			sb.append("INSERT INTO ");
			sb.append(UserColumn.TABLE_NAME);
			sb.append(" (");
			sb.append(UserColumn.EMAIL.getColumnName());
			sb.append(",");
			sb.append(UserColumn.PASSWORD.getColumnName());
			sb.append(")");
			sb.append(" VALUES (?,?)");

			stmt2 = con.prepareStatement(sb.toString());
			stmt2.setString(1, entity.getEmail());
			stmt2.setString(2, entity.getPassword());
			stmt2.executeUpdate();

			con.commit();

			stmt.setString(1, entity.getEmail());
			rs = stmt.executeQuery();
			con.commit();
			UserDatabaseHelper dbh = new UserDatabaseHelper();
			rs.next();
			return dbh.getEntityFromResultSet(rs);
		} catch (SQLException e) {
			if (con != null) {
				System.err.print("Transaction is being rolled back");
				con.rollback();
			}
			throw e;

		} finally {
			con.setAutoCommit(true);
			stmt.close();
			stmt2.close();
		}
	}

	protected PreparedStatement createPreparedStatement(Map<String, Object> valueMap, String databaseName,
			String databaseMethod) throws SQLException {
		StringBuilder insertString = new StringBuilder(databaseMethod + " " + databaseName + " (");
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
		PreparedStatement stmt = con.prepareStatement(statementString);
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
			} else {
				// handle for null
				stmt.setString(count, null);
			}
			count++;
		}
	}

	@Override
	protected UserEntity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return new UserEntity(resultSet.getString(UserColumn.ID.getColumnName()), "",
				resultSet.getString(UserColumn.EMAIL.getColumnName()),
				resultSet.getString(UserColumn.PASSWORD.getColumnName()));
	}

}
