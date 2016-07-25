package com.kms.cura.dal.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.DoctorColumn;
import com.kms.cura.dal.mapping.MessageColumn;
import com.kms.cura.dal.mapping.PatientColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;

public class MessageDatabaseHelper extends DatabaseHelper {

	// ambiguous columns' name
	private final static String DOCTOR_NAME = "doctor_name";
	private final static String PATIENT_NAME = "patient_name";
	
	public MessageDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected MessageEntity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		DoctorUserEntity doctor = new DoctorUserEntity(resultSet.getString(MessageColumn.DOCTOR_ID.getColumnName()),
				resultSet.getString(DOCTOR_NAME));
		PatientUserEntity patient = new PatientUserEntity(resultSet.getString(MessageColumn.PATIENT_ID.getColumnName()),
				resultSet.getString(PATIENT_NAME));
		if (resultSet.getBoolean(MessageColumn.SENT_BY_DOCTOR.getColumnName())) {
			return new MessageEntity(doctor, patient, resultSet.getTimestamp(MessageColumn.TIME_SENT.getColumnName()),
					resultSet.getString(MessageColumn.MESSAGE.getColumnName()));
		} else {
			return new MessageEntity(patient, doctor, resultSet.getTimestamp(MessageColumn.TIME_SENT.getColumnName()),
					resultSet.getString(MessageColumn.MESSAGE.getColumnName()));
		}
	}

	public List<MessageEntity> queryMessageByPatient(PatientUserEntity patient) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		List<MessageEntity> messageEntities = new ArrayList<>();
		builder.append("select ");
		builder.append(MessageColumn.PATIENT_ID + ", ");
		builder.append(MessageColumn.DOCTOR_ID + ", ");
		builder.append(MessageColumn.SENT_BY_DOCTOR + ", ");
		builder.append(MessageColumn.TIME_SENT + ", ");
		builder.append(MessageColumn.MESSAGE + ", ");
		builder.append(MessageColumn.PATIENT_AVAILABLE + ", ");
		builder.append(MessageColumn.DOCTOR_AVAILABLE + ", ");
		builder.append("p." + PatientColumn.NAME + " as " + PATIENT_NAME + ", ");
		builder.append("d." + DoctorColumn.NAME + " as " + DOCTOR_NAME);
		builder.append(" from ");
		builder.append(MessageColumn.TABLE_NAME + " m");
		builder.append(" left join ");
		builder.append(DoctorColumn.TABLE_NAME + " d");
		builder.append(" on m." + MessageColumn.DOCTOR_ID);
		builder.append(" = d." + DoctorColumn.USER_ID);
		builder.append(" left join ");
		builder.append(PatientColumn.TABLE_NAME + " p");
		builder.append(" on m." + MessageColumn.PATIENT_ID);
		builder.append(" = p." + PatientColumn.USER_ID);
		builder.append(" where ");
		builder.append(MessageColumn.PATIENT_ID);
		builder.append(" = ");
		builder.append(patient.getId());
		builder.append(" and ");
		builder.append(MessageColumn.PATIENT_AVAILABLE + " = true" );
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				MessageEntity entity = getEntityFromResultSet(rs);
				messageEntities.add(entity);
			}
			return messageEntities;
		} finally {
			stmt.close();
		}
	}

	public List<MessageEntity> queryMessageByDoctor(DoctorUserEntity doctor) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		List<MessageEntity> messageEntities = new ArrayList<>();
		builder.append("select ");
		builder.append(MessageColumn.PATIENT_ID + ", ");
		builder.append(MessageColumn.DOCTOR_ID + ", ");
		builder.append(MessageColumn.SENT_BY_DOCTOR + ", ");
		builder.append(MessageColumn.TIME_SENT + ", ");
		builder.append(MessageColumn.MESSAGE + ", ");
		builder.append(MessageColumn.PATIENT_AVAILABLE + ", ");
		builder.append(MessageColumn.DOCTOR_AVAILABLE + ", ");
		builder.append("p." + PatientColumn.NAME + " as " + PATIENT_NAME + ", ");
		builder.append("d." + DoctorColumn.NAME + " as " + DOCTOR_NAME);
		builder.append(" from ");
		builder.append(MessageColumn.TABLE_NAME + " m");
		builder.append(" left join ");
		builder.append(PatientColumn.TABLE_NAME + " p");
		builder.append(" on m." + MessageColumn.PATIENT_ID);
		builder.append(" = p." + PatientColumn.USER_ID);
		builder.append(" left join ");
		builder.append(DoctorColumn.TABLE_NAME + " d");
		builder.append(" on m." + MessageColumn.DOCTOR_ID);
		builder.append(" = d." + DoctorColumn.USER_ID);
		builder.append(" where ");
		builder.append(MessageColumn.DOCTOR_ID);
		builder.append(" = ");
		builder.append(doctor.getId());
		builder.append(" and ");
		builder.append(MessageColumn.DOCTOR_AVAILABLE + " = true" );
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				MessageEntity entity = getEntityFromResultSet(rs);
				messageEntities.add(entity);
			}
			return messageEntities;
		} finally {
			stmt.close();
		}
	}

	public void deletePatientMessage(MessageEntity entity) throws SQLException {
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("update ");
		builder.append(MessageColumn.TABLE_NAME);
		builder.append(" set ");
		builder.append(MessageColumn.PATIENT_AVAILABLE + " = false");
		builder.append(" where ");
		if (entity.isSenderDoctor()) {
			builder.append(MessageColumn.DOCTOR_ID + " = " + entity.getSender().getId());
			builder.append(" and ");
			builder.append(MessageColumn.PATIENT_ID + " = " + entity.getReceiver().getId());
		} else {
			builder.append(MessageColumn.DOCTOR_ID + " = " + entity.getReceiver().getId());
			builder.append(" and ");
			builder.append(MessageColumn.PATIENT_ID + " = " + entity.getSender().getId());
		}
		builder.append(" and ");
		builder.append(MessageColumn.TIME_SENT + " = '" + entity.getTimeSent().toString() + "'");
		builder.append(" and ");
		builder.append(MessageColumn.MESSAGE + " = '" + entity.getMessage() + "'");
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(builder.toString());
			stmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			if (con != null) {
				System.err.print("Transaction is being rolled back");
				con.rollback();
			}
			throw e;
		} finally {
			stmt.close();
		}
	}
	
	public void deleteDoctorMessage(MessageEntity entity) throws SQLException {
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("update ");
		builder.append(MessageColumn.TABLE_NAME);
		builder.append(" set ");
		builder.append(MessageColumn.DOCTOR_AVAILABLE + " = false");
		builder.append(" where ");
		if (entity.isSenderDoctor()) {
			builder.append(MessageColumn.DOCTOR_ID + " = " + entity.getSender().getId());
			builder.append(" and ");
			builder.append(MessageColumn.PATIENT_ID + " = " + entity.getReceiver().getId());
		} else {
			builder.append(MessageColumn.DOCTOR_ID + " = " + entity.getReceiver().getId());
			builder.append(" and ");
			builder.append(MessageColumn.PATIENT_ID + " = " + entity.getSender().getId());
		}
		builder.append(" and ");
		builder.append(MessageColumn.TIME_SENT + " = '" + entity.getTimeSent().toString() + "'");
		builder.append(" and ");
		builder.append(MessageColumn.MESSAGE + " = '" + entity.getMessage() + "'");
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(builder.toString());
			stmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			if (con != null) {
				System.err.print("Transaction is being rolled back");
				con.rollback();
			}
			throw e;
		} finally {
			stmt.close();
		}
	}
}
