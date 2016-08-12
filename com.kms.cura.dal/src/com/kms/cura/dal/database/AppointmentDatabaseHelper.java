package com.kms.cura.dal.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kms.cura.dal.mapping.AppointmentColumn;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.FacilityEntity;
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
		return new AppointmentEntity(resultSet.getString(AppointmentColumn.APPT_ID.getColumnName()),
				patient, doctor, facility,
                resultSet.getDate(AppointmentColumn.APPT_DAY.getColumnName()),
                resultSet.getTime(AppointmentColumn.START_TIME.getColumnName()),
                resultSet.getTime(AppointmentColumn.END_TIME.getColumnName()),
                resultSet.getInt(AppointmentColumn.STATUS.getColumnName()),
                resultSet.getString(AppointmentColumn.PATIENT_CMT.getColumnName()),
                resultSet.getString(AppointmentColumn.DOCTOR_CMT.getColumnName()));
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

    private PreparedStatement getUpdateAppointmentQuery(AppointmentEntity entity) throws SQLException {
        AppointmentEntity appointmentEntity = entity.copy();
        appointmentEntity.setStatus(-1);
        return createUpdatePreparedStatement(getColumnValueMapForEntity(appointmentEntity), AppointmentColumn.TABLE_NAME, AppointmentColumn.STATUS.getColumnName(), entity.getStatus());
    }

    private Map<String, Object> getColumnValueMapForEntity(AppointmentEntity entity) {
        if (entity == null) {
            // handle error;
            return null;
        }
        Map<String, Object> columnValueMap = new LinkedHashMap<String, Object>();
		if (entity.getId() != null){
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
            createAppointment(entity);
			AppointmentEntity search = new AppointmentEntity(null, entity.getPatientUserEntity(), null, null, null, null,
                    null, -1, null, null);
            patientAppts = getAppointment(new AppointSearchEntity(search), entity.getPatientUserEntity(), null);
            con.commit();
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

    private void createAppointment(AppointmentEntity entity) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getinsertAppointmentQuery(entity);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

    }

    public List<AppointmentEntity> updateAppointment(AppointmentEntity entity, boolean patient) throws SQLException, ClassNotFoundException, IOException {
        List<AppointmentEntity> listAppts = null;
        AppointmentEntity search = null;
        PreparedStatement stmt = null;
        try {
            con.setAutoCommit(false);
            stmt = getUpdateAppointmentQuery(entity);
            stmt.executeUpdate();
            if (patient){
            	search = new AppointmentEntity(entity.getPatientUserEntity(), null, null, null, null,
                    null, -1, null, null);
            	listAppts = getAppointment(new AppointSearchEntity(search), entity.getPatientUserEntity(), null);
            }
            else{
            	search = new AppointmentEntity(null, entity.getDoctorUserEntity(), null, null, null,
                        null, -1, null, null);
            	listAppts = getAppointment(new AppointSearchEntity(search), null, entity.getDoctorUserEntity());
            }
            con.commit();
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

}
