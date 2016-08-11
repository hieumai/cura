package com.kms.cura.dal.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.NotificationColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.NotificationEntity;

public class NotificationDatabaseHelper extends DatabaseHelper {

	public NotificationDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet)
			throws SQLException, ClassNotFoundException, IOException {
		return null;
	}

	protected NotificationEntity getNotificationEntityFromResultSet(ResultSet rs, String type) throws SQLException {
		NotificationEntity entity = null;
		if (type.equals(NotificationEntity.APPT_NOTI_TYPE)) {
			entity = getApptNotificationEntityFromResultSet(rs);
		}
		entity = getMsgNotificationEntityFromResultSet(rs);
		entity.setType(type);
		return entity;
	}

	protected NotificationEntity getMsgNotificationEntityFromResultSet(ResultSet rs) throws SQLException {
		return new NotificationEntity(rs.getString(NotificationColumn.NOTI_ID.getColumnName()),
				rs.getString(NotificationColumn.USER_ID.getColumnName()),
				rs.getString(NotificationColumn.REF_ID.getColumnName()),
				rs.getBoolean(NotificationColumn.STATUS.getColumnName()), null);
	}

	protected NotificationEntity getApptNotificationEntityFromResultSet(ResultSet rs) throws SQLException {
		return new NotificationEntity(rs.getString(NotificationColumn.NOTI_ID.getColumnName()),
				rs.getString(NotificationColumn.USER_ID.getColumnName()),
				rs.getString(NotificationColumn.REF_ID.getColumnName()),
				rs.getBoolean(NotificationColumn.STATUS.getColumnName()), null,
				rs.getString(NotificationColumn.APPT_NOTI_TYPE.getColumnName()));
	}

	public void createNotification(String refID, String userID, String type, String notiType) {
		String tableName = getTableName(type);
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ");
		builder.append(tableName);
		builder.append(" VALUES ");
		if (notiType == null) {
			builder.append("(?, ?, ?");
		}
		builder.append("(?, ?, ?, ?");
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(builder.toString());
			stmt.setString(1, userID);
			stmt.setString(2, refID);
			stmt.setBoolean(3, false);
			if (notiType != null){
				stmt.setString(4, notiType);
			}
			stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			if (con != null) {
				System.err.print("Transaction is being rolled back");
				try {
					con.rollback();
				} catch (SQLException e1) {
				}
			}
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
					con.setAutoCommit(true);
				} catch (SQLException e) {
				}
			}
		}
	}

	public void updateNotification(String id, String type) throws SQLException {
		String tableName = getTableName(type);
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE ");
		builder.append(tableName);
		builder.append(" SET ");
		builder.append(NotificationColumn.STATUS.getColumnName());
		builder.append(" = ? ");
		builder.append("WHERE ");
		builder.append(NotificationColumn.NOTI_ID.getColumnName());
		builder.append(" = ? ");
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(builder.toString());
			stmt.setBoolean(1, true);
			stmt.setString(2, id);
			stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			if (con != null) {
				System.err.print("Transaction is being rolled back");
				con.rollback();
			}
			throw e;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			con.setAutoCommit(true);
		}
	}

	public List<NotificationEntity> getByEntityType(String type) throws SQLException {
		String tableName = getTableName(type);
		List<NotificationEntity> notis = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM ");
		builder.append(tableName);
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					NotificationEntity entity = getNotificationEntityFromResultSet(rs, type);
					notis.add(entity);
				}
			}
			return notis;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private String getTableName(String type) {
		if (type.equals(NotificationEntity.APPT_NOTI_TYPE)) {
			return NotificationColumn.TABLE_NAME_APPT_TYPE;
		}
		return NotificationColumn.TABLE_NAME_MSG_TYPE;
	}

}
