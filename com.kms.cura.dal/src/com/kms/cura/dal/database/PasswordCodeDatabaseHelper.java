package com.kms.cura.dal.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.kms.cura.dal.mapping.PasswordCodeColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.UserEntity;

public class PasswordCodeDatabaseHelper extends DatabaseHelper {

	private static final String COUNT_COLUMN = "code_count";
	private int MILISECOND = 1000;
	private int SECOND_IN_DAY = 24*60*60;

	public PasswordCodeDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet)
			throws SQLException, ClassNotFoundException, IOException {
		return null;
	}

	public void updateNewCode(String user_id, String code, Timestamp timestamp) throws SQLException {
		try {
			con.setAutoCommit(false);
			deleteCode(user_id);
			insertCode(user_id, code, timestamp);
			con.commit();
		} catch (SQLException e) {
			if (con != null) {
				System.err.print("Transaction is being rolled back");
				con.rollback();
			}
			throw e;
		} finally {
			con.setAutoCommit(true);
		}
	}

	private void insertCode(String user_id, String code, Timestamp timestamp) throws SQLException {
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("insert into " + PasswordCodeColumn.TABLE_NAME);
		builder.append(" values (" + user_id);
		builder.append(", '" + code);
		builder.append("', '" + timestamp.toString() + "')");
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private void deleteCode(String user_id) throws SQLException {
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("delete from " + PasswordCodeColumn.TABLE_NAME);
		builder.append(" where " + PasswordCodeColumn.USER_ID + " = " + user_id);
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public boolean checkDuplicateCode(String code) throws SQLException {
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		ResultSet rs = null;
		builder.append("select count(*) as " + COUNT_COLUMN + " from " + PasswordCodeColumn.TABLE_NAME);
		builder.append(" where " + PasswordCodeColumn.CODE + " = '" + code + "'");
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			rs.next();
			return (rs.getInt(COUNT_COLUMN) == 0);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public String checkCode(String userID, String code) throws Exception {
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		ResultSet rs = null;
		builder.append("select * from " + PasswordCodeColumn.TABLE_NAME);
		builder.append(" where " + PasswordCodeColumn.USER_ID + " = " + userID);
		builder.append(" and " + PasswordCodeColumn.CODE + " = '" + code + "'");
		try { 
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			if (!rs.next()) {
				return UserEntity.CODE_INVALID;
			}
			Timestamp timeSent = new Timestamp(System.currentTimeMillis());
			Timestamp timeRequest = rs.getTimestamp(PasswordCodeColumn.REQUEST_TIME.getColumnName());
			long distance = (timeSent.getTime() - timeRequest.getTime()) / MILISECOND;
			if (distance < SECOND_IN_DAY) {
				return UserEntity.CODE_VALID;
			}
			return UserEntity.CODE_EXPIRED;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}
}
