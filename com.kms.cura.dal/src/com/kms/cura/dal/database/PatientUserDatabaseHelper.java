package com.kms.cura.dal.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.mapping.PatientColumn;
import com.kms.cura.dal.mapping.UserColumn;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;

public class PatientUserDatabaseHelper extends UserDatabaseHelper {

	public PatientUserDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	public static Map<String, Object> getColumnValueMapForEntity(PatientUserEntity entity) {
		if (entity == null) {
			// handle error;
			return null;
		}
		Map<String, Object> columnValueMap = new LinkedHashMap<String, Object>();
		for (PatientColumn patientColumn : PatientColumn.values()) {
			String columnName = patientColumn.getColumnName();
			switch (patientColumn) {
			case BIRTH:
				columnValueMap.put(columnName, entity.getBirth());
				break;
			case GENDER:
				columnValueMap.put(columnName, entity.getGender());
				break;
			case HEALTH_CONCERN:
				columnValueMap.put(columnName, entity.getHealthConcern());
				break;
			case INSURANCE:
				columnValueMap.put(columnName, entity.getInsurance());
				break;
			case LOCATION:
				columnValueMap.put(columnName, entity.getLocation());
				break;
			case NAME:
				columnValueMap.put(columnName, entity.getName());
				break;
			case USER_ID:
				columnValueMap.put(columnName, entity.getId());
				break;
			default:
				break;
			}

		}
		return columnValueMap;
	}

	@Override
	public UserEntity insertUser(UserEntity entity) throws SQLException, DALException, ClassNotFoundException {
		PreparedStatement stmt = null;
		UserEntity userEntity = super.insertUser(entity);
		ResultSet rs = null;
		if (!(entity instanceof PatientUserEntity)) {
			return null;
		}
		try {
			con.setAutoCommit(false);
			int userId = Integer.parseInt(userEntity.getId());
			entity.setId(String.valueOf(userId));
			stmt = getInsertStatementForUser((PatientUserEntity) entity);
			stmt.executeUpdate();
			stmt.close();
			con.commit();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM ");
			sb.append(PatientColumn.TABLE_NAME);
			sb.append(" WHERE ");
			sb.append(PatientColumn.USER_ID.getColumnName());
			sb.append(" = ?");

			stmt = con.prepareStatement(sb.toString());
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			rs.next();
			return getEntityFromResultSet(rs);
		} catch (SQLException e) {
			if (con != null) {
				System.err.print("Transaction is being rolled back");
				con.rollback();
			}
			throw e;
		} finally {
			con.setAutoCommit(true);
			stmt.close();
		}
	}

	public PreparedStatement getInsertStatementForUser(PatientUserEntity entity) throws SQLException {
		Map<String, Object> patientValueMap = getColumnValueMapForEntity(entity);
		String databaseName = PatientColumn.TABLE_NAME;
		String databaseMethod = "INSERT INTO";
		return createPreparedStatement(patientValueMap, databaseName, databaseMethod);
	}

	@Override
	protected UserEntity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		UserDatabaseHelper dbh = new UserDatabaseHelper();
		UserEntity userEntity = (UserEntity) dbh.queryByID(UserColumn.TABLE_NAME,
				resultSet.getInt(PatientColumn.USER_ID.getColumnName()));
		if (userEntity != null) {
			PatientUserEntity patient = new PatientUserEntity(resultSet.getString(PatientColumn.USER_ID.getColumnName()),
					resultSet.getString(PatientColumn.NAME.getColumnName()), userEntity.getEmail(),
					userEntity.getPassword(), resultSet.getString(PatientColumn.GENDER.getColumnName()),
					resultSet.getDate(PatientColumn.BIRTH.getColumnName()),
					resultSet.getString(PatientColumn.LOCATION.getColumnName()),
					resultSet.getString(PatientColumn.INSURANCE.getColumnName()),
					resultSet.getString(PatientColumn.HEALTH_CONCERN.getColumnName()));
			PatientHealthDatabaseHelper healthDbh = new PatientHealthDatabaseHelper();
			patient.setHealthEntities(new ArrayList<>(healthDbh.queryHealthByPatientID(userEntity.getId())));
			return patient;
		}
		return null;
	}
	
	

	public PatientUserEntity searchPatient(UserEntity entity) throws ClassNotFoundException, SQLException {
		return (PatientUserEntity) queryUserEntitybyEmailPassword(PatientColumn.TABLE_NAME, UserColumn.TABLE_NAME,
				entity.getEmail(), entity.getPassword(), PatientColumn.USER_ID.getColumnName(),
				UserColumn.ID.getColumnName());
	}
}
