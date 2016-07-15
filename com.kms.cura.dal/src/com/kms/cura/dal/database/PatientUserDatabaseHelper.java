package com.kms.cura.dal.database;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.exception.DuplicatedUserEmailException;
import com.kms.cura.dal.mapping.PatientColumn;
import com.kms.cura.dal.mapping.UserColumn;
import com.kms.cura.entity.ImageEntity;
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

	public PatientUserEntity insertPatientUser(UserEntity entity)
			throws SQLException, DALException, ClassNotFoundException, IOException {
		PreparedStatement stmt = null;
		if (!(entity instanceof PatientUserEntity)) {
			return null;
		}
		if (queryByEmail(entity.getEmail()) != null) {
			throw new DuplicatedUserEmailException(entity.getEmail());
		}
		try {
			con.setAutoCommit(false);
			super.insertUser(entity);
			con.commit();
			UserEntity newlyInsertedUser = queryByEmail(entity.getEmail());
			entity.setId(newlyInsertedUser.getId());
			stmt = getInsertStatementForUser((PatientUserEntity) entity);
			stmt.executeUpdate();
			con.commit();

			return queryPatientByID(newlyInsertedUser.getId());
		} catch (SQLException e) {
			if (con != null) {
				System.err.print("Transaction is being rolled back");
				con.rollback();
			}
			throw e;
		} finally {
			con.setAutoCommit(true);
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public PatientUserEntity queryPatientByID(String id) throws SQLException, ClassNotFoundException, IOException {
		return (PatientUserEntity) queryUserEntitybyId(PatientColumn.TABLE_NAME, id,
				PatientColumn.USER_ID.getColumnName());
	}

	public PreparedStatement getInsertStatementForUser(PatientUserEntity entity) throws SQLException {
		Map<String, Object> patientValueMap = getColumnValueMapForEntity(entity);
		String databaseName = PatientColumn.TABLE_NAME;
		return createInsertPreparedStatement(patientValueMap, databaseName);
	}

	public List<PatientUserEntity> getAllPatient() throws SQLException, ClassNotFoundException, IOException {
		List<PatientUserEntity> list = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM ");
		builder.append(PatientColumn.TABLE_NAME);
		builder.append(" LEFT JOIN ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(" ON ");
		builder.append(PatientColumn.TABLE_NAME);
		builder.append(".");
		builder.append(PatientColumn.USER_ID.getColumnName());
		builder.append(" = ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(".");
		builder.append(UserColumn.ID.getColumnName());
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					list.add(getEntityFromResultSet(rs));
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return list;
	}

	@Override
	protected PatientUserEntity getEntityFromResultSet(ResultSet resultSet)
			throws SQLException, ClassNotFoundException, IOException {

		String path = resultSet.getString(UserColumn.IMAGE_PATH.getColumnName());

		PatientUserEntity patient = new PatientUserEntity(resultSet.getString(PatientColumn.USER_ID.getColumnName()),
				resultSet.getString(PatientColumn.NAME.getColumnName()),
				resultSet.getString(UserColumn.EMAIL.getColumnName()),
				resultSet.getString(UserColumn.PASSWORD.getColumnName()),
				resultSet.getString(PatientColumn.GENDER.getColumnName()),
				resultSet.getDate(PatientColumn.BIRTH.getColumnName()),
				resultSet.getString(PatientColumn.LOCATION.getColumnName()),
				resultSet.getString(PatientColumn.INSURANCE.getColumnName()),
				resultSet.getString(PatientColumn.HEALTH_CONCERN.getColumnName()), path);
		PatientHealthDatabaseHelper healthDbh = new PatientHealthDatabaseHelper();
		patient.setHealthEntities(new ArrayList<>(healthDbh.queryHealthByPatientID(patient.getId())));
		return patient;
	}

	public PatientUserEntity searchPatient(UserEntity entity) throws ClassNotFoundException, SQLException, IOException {
		return (PatientUserEntity) queryUserEntitybyEmailPassword(PatientColumn.TABLE_NAME, entity.getEmail(),
				entity.getPassword(), PatientColumn.USER_ID.getColumnName());
	}

	public PatientUserEntity updatePatient(PatientUserEntity entity)
			throws ClassNotFoundException, SQLException, IOException {
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE ");
		builder.append(PatientColumn.TABLE_NAME);
		builder.append(" SET ");
		builder.append(PatientColumn.NAME);
		builder.append(" = ");
		builder.append(" ? ,");
		builder.append(PatientColumn.GENDER);
		builder.append(" = ");
		builder.append(" ? ,");
		builder.append(PatientColumn.BIRTH);
		builder.append(" = ");
		builder.append(" ? ,");
		builder.append(PatientColumn.LOCATION);
		builder.append(" = ");
		builder.append(" ? ,");
		builder.append(PatientColumn.INSURANCE);
		builder.append(" = ");
		builder.append(" ?");
		builder.append(" WHERE ");
		builder.append(PatientColumn.USER_ID);
		builder.append(" = ? ");
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.setString(1, entity.getName());
			stmt.setString(2, entity.getGender());
			stmt.setString(3, entity.getBirth().toString());
			stmt.setString(4, entity.getLocation());
			stmt.setString(5, entity.getInsurance());
			stmt.setString(6, entity.getId());
			stmt.executeUpdate();

		} finally {

			if (stmt != null) {
				stmt.close();
			}
		}
		return queryPatientByID(entity.getId());
	}
}
