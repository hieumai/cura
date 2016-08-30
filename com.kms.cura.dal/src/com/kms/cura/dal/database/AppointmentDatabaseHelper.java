package com.kms.cura.dal.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.Notification;

import com.kms.cura.dal.database.NotificationHelper;
import com.kms.cura.dal.mapping.AppointmentColumn;
import com.kms.cura.dal.mapping.AppointmentNotiType;
import com.kms.cura.dal.mapping.NotificationType;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;

public class AppointmentDatabaseHelper extends DatabaseHelper {

	public AppointmentDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return null;
	}

	protected AppointmentEntity geAppointmentEntityFromResultSet(ResultSet resultSet,
			PatientUserEntity patientUserEntity, DoctorUserEntity doctorUserEntity)
			throws SQLException, ClassNotFoundException, IOException {
		DoctorUserEntity doctor = null;
		PatientUserEntity patient = null;
		if (doctorUserEntity == null) {
			DoctorUserDatabaseHelper doctorUserDatabaseHelper = new DoctorUserDatabaseHelper();
			try {
				doctor = (DoctorUserEntity) doctorUserDatabaseHelper
						.queryDoctorByID(String.valueOf(resultSet.getInt(AppointmentColumn.DOCTOR_ID.getColumnName())));
			} finally {
				doctorUserDatabaseHelper.closeConnection();
			}
		}
		if (patientUserEntity == null) {
			PatientUserDatabaseHelper patientUserDatabaseHelper = new PatientUserDatabaseHelper();
			try {
				patient = (PatientUserEntity) patientUserDatabaseHelper.queryPatientByID(
						String.valueOf(resultSet.getInt(AppointmentColumn.PATIENT_ID.getColumnName())));
			} finally {
				patientUserDatabaseHelper.closeConnection();
			}
		}
		FacilityDatabaseHelper facilityDatabaseHelper = new FacilityDatabaseHelper();
		FacilityEntity facility = null;
		try {
			facility = facilityDatabaseHelper
					.queryByID(resultSet.getInt(AppointmentColumn.FACILITY_ID.getColumnName()));
		} finally {
			facilityDatabaseHelper.closeConnection();
		}
		return new AppointmentEntity(resultSet.getString(AppointmentColumn.APPT_ID.getColumnName()), patient, doctor,
				facility, resultSet.getDate(AppointmentColumn.APPT_DAY.getColumnName()),
				resultSet.getTime(AppointmentColumn.START_TIME.getColumnName()),
				resultSet.getTime(AppointmentColumn.END_TIME.getColumnName()),
				resultSet.getInt(AppointmentColumn.STATUS.getColumnName()),
				resultSet.getString(AppointmentColumn.PATIENT_CMT.getColumnName()),
				resultSet.getString(AppointmentColumn.DOCTOR_CMT.getColumnName()),
                resultSet.getFloat(AppointmentColumn.RATE.getColumnName()),
                resultSet.getString(AppointmentColumn.RATE_CMT.getColumnName()));
	}

	public List<AppointmentEntity> getAppointment(AppointSearchEntity criteria, PatientUserEntity patientUserEntity,
			DoctorUserEntity doctorUserEntity) throws SQLException, ClassNotFoundException, IOException {
		List<AppointmentEntity> listAppts = new ArrayList<>();
		AppointmentEntity entity = criteria.getAppointmentEntity();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = getAppointmentQuery(entity);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					listAppts.add(geAppointmentEntityFromResultSet(rs, patientUserEntity, doctorUserEntity));
				}
			}
			return listAppts;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private PreparedStatement getAppointmentQuery(AppointmentEntity entity) throws SQLException {
		return createSelectWherePreparedStatement(getColumnValueMapForEntity(entity), AppointmentColumn.TABLE_NAME);
	}

	private PreparedStatement getinsertAppointmentQuery(AppointmentEntity entity) throws SQLException {
		return createInsertPreparedStatement(getColumnValueMapForEntity(entity), AppointmentColumn.TABLE_NAME);
	}

	private PreparedStatement getUpdateAppointmentQuery(AppointmentEntity entity, List<String> columnUpdate,
			List<Object> valueUpdate) throws SQLException {
		AppointmentEntity appointmentEntity = entity.copy();
		appointmentEntity.setStatus(-1);
		return createUpdatePreparedStatement(getColumnValueMapForEntity(appointmentEntity),
				AppointmentColumn.TABLE_NAME, columnUpdate, valueUpdate);
	}

	private Map<String, Object> getColumnValueMapForEntity(AppointmentEntity entity) {
		if (entity == null) {
			// handle error;
			return null;
		}
		Map<String, Object> columnValueMap = new LinkedHashMap<String, Object>();
		if (entity.getId() != null) {
			columnValueMap.put(AppointmentColumn.APPT_ID.getColumnName(), entity.getId());
		}
		if (entity.getPatientUserEntity() != null) {
			columnValueMap.put(AppointmentColumn.PATIENT_ID.getColumnName(), entity.getPatientUserEntity().getId());
		}
		if (entity.getDoctorUserEntity() != null) {
			columnValueMap.put(AppointmentColumn.DOCTOR_ID.getColumnName(), entity.getDoctorUserEntity().getId());
		}
		if (entity.getFacilityEntity() != null) {
			columnValueMap.put(AppointmentColumn.FACILITY_ID.getColumnName(), entity.getFacilityEntity().getId());
		}
		if (entity.getApptDay() != null) {
			columnValueMap.put(AppointmentColumn.APPT_DAY.getColumnName(), entity.getApptDay());
		}
		if (entity.getStartTime() != null) {
			columnValueMap.put(AppointmentColumn.START_TIME.getColumnName(), entity.getStartTime());
		}
		if (entity.getEndTime() != null) {
			columnValueMap.put(AppointmentColumn.END_TIME.getColumnName(), entity.getEndTime());
		}
		if (entity.getStatus() != -1) {
			columnValueMap.put(AppointmentColumn.STATUS.getColumnName(), entity.getStatus());
		}
		if (entity.getPatientCmt() != null) {
			columnValueMap.put(AppointmentColumn.PATIENT_CMT.getColumnName(), entity.getPatientCmt());
		}
		if (entity.getDoctorCmt() != null) {
			columnValueMap.put(AppointmentColumn.DOCTOR_CMT.getColumnName(), entity.getDoctorCmt());
		}
		return columnValueMap;
	}

	public List<AppointmentEntity> bookAppointment(AppointmentEntity entity)
			throws SQLException, ClassNotFoundException, IOException {
		List<AppointmentEntity> patientAppts;
		try {
			con.setAutoCommit(false);
			int newID = createAppointment(entity);
			NotificationHelper notificationHelper = new NotificationHelper();
			AppointmentEntity search = new AppointmentEntity(null, entity.getPatientUserEntity(), null, null, null,
					null, null, -1, null, null);
			patientAppts = getAppointment(new AppointSearchEntity(search), entity.getPatientUserEntity(), null);
			con.commit();
			entity.setId(String.valueOf(newID));
			createNewAppointmentNotification(String.valueOf(newID), entity);
			notificationHelper.sendAppointmentRequest(entity.getDoctorID(), entity);
			return patientAppts;
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

	private int createAppointment(AppointmentEntity entity) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = getinsertAppointmentQuery(entity);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	public List<AppointmentEntity> updateAppointment(AppointmentEntity entity, boolean patient)
			throws SQLException, ClassNotFoundException, IOException {
		List<AppointmentEntity> listAppts = null;
		AppointmentEntity search = null;
		PreparedStatement stmt = null;
		List<String> columnUpdate = new ArrayList<>();
		List<Object> valueUpdate = new ArrayList<>();
		columnUpdate.add(AppointmentColumn.STATUS.getColumnName());
		int status = entity.getStatus();
		valueUpdate.add(status);
		if (status == AppointmentEntity.REJECT_STT) {
			columnUpdate.add(AppointmentColumn.DOCTOR_CMT.getColumnName());
			valueUpdate.add(entity.getDoctorCmt());
			entity.setDoctorCmt(null);
		}
		try {
			con.setAutoCommit(false);
			stmt = getUpdateAppointmentQuery(entity, columnUpdate, valueUpdate);
			stmt.executeUpdate();
			PatientUserEntity patientUserEntity = entity.getPatientUserEntity();
			DoctorUserEntity doctorUserEntity = entity.getDoctorUserEntity();
			if (patient) {
				search = new AppointmentEntity(null, patientUserEntity, null, null, null, null, null, -1, null, null);
				listAppts = getAppointment(new AppointSearchEntity(search), patientUserEntity, null);
			} else {
				search = new AppointmentEntity(null, null, doctorUserEntity, null, null, null, null, -1, null, null);
				listAppts = getAppointment(new AppointSearchEntity(search), null, doctorUserEntity);
			}
			con.commit();
			if (!patient && (status == AppointmentEntity.ACCEPTED_STT || status == AppointmentEntity.REJECT_STT
					|| status == AppointmentEntity.DOCTOR_CANCEL_STT)) {
				createUpdateAppointmentNotification(entity.getId(), doctorUserEntity.getId());
			}
			return listAppts;
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

	private void createNewAppointmentNotification(String newID, AppointmentEntity entity) {
		NotificationDatabaseHelper databaseHelper = null;
		try {
			databaseHelper = new NotificationDatabaseHelper();
			databaseHelper.createNotification(newID, entity.getDoctorUserEntity().getId(),
					NotificationType.APPT_TYPE.getNotiType(), AppointmentNotiType.NEW_APPT.getNotiType());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO log file for server later
		} finally {
			try {
				databaseHelper.closeConnection();
			} catch (SQLException e) {
				return;
			}
		}
	}

	private void createUpdateAppointmentNotification(String id, String userID) {
		NotificationDatabaseHelper databaseHelper = null;
		try {
			databaseHelper = new NotificationDatabaseHelper();
			databaseHelper.createNotification(id, userID, NotificationType.APPT_TYPE.getNotiType(),
					AppointmentNotiType.UPDATE_STT.getNotiType());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO log file for server later
		} finally {
			try {
				databaseHelper.closeConnection();
			} catch (SQLException e) {
				return;
			}
		}
	}

	public AppointmentEntity querybyID(int id) throws ClassNotFoundException, SQLException, IOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM " + AppointmentColumn.TABLE_NAME + " WHERE "
					+ AppointmentColumn.APPT_ID.getColumnName() + " = ?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			rs.next();
			AppointmentEntity result = geAppointmentEntityFromResultSet(rs, null, null);
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

    public AppointmentEntity updateRating(AppointmentEntity entity) throws SQLException, ClassNotFoundException, IOException {
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	StringBuilder builder = new StringBuilder();
    	builder.append("update " + AppointmentColumn.TABLE_NAME);
    	builder.append(" set " + AppointmentColumn.RATE.getColumnName() + " = " + entity.getRate());
    	builder.append(", " + AppointmentColumn.RATE_CMT.getColumnName() + " = '" + entity.getRate_comment() + "'");
    	builder.append(" where " + AppointmentColumn.APPT_ID.getColumnName() + " = " + entity.getId());
    	try {
    		con.setAutoCommit(false);
    		stmt = con.prepareStatement(builder.toString());
    		stmt.executeUpdate();
    		rs = queryByReferenceID(AppointmentColumn.TABLE_NAME, AppointmentColumn.APPT_ID.getColumnName(), Integer.parseInt(entity.getId()));
    		rs.next();
    		return geAppointmentEntityFromResultSet(rs, null, null);
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
}
