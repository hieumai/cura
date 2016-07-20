package com.kms.cura.dal.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.AppointmentDAL;
import com.kms.cura.dal.database.PatientHealthDatabaseHelper;
import com.kms.cura.dal.database.PatientUserDatabaseHelper;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;

public class PatientUserDAL extends UserDAL {
	private static PatientUserDAL _instance;

	private PatientUserDAL() {
		// hide constructor
	}

	public static PatientUserDAL getInstance() {
		if (_instance == null) {
			_instance = new PatientUserDAL();
		}
		return _instance;
	}

	public List<Entity> getAll() throws ClassNotFoundException, SQLException {
		PatientUserDatabaseHelper dbh = new PatientUserDatabaseHelper();
		try{
			List<PatientUserEntity> list = dbh.getAllPatient();
			List<Entity> patientUserEntities = new ArrayList<>();
			for (PatientUserEntity entity : list) {
				patientUserEntities.add(getAllReferenceAttributeforPatient((PatientUserEntity) entity));
			}
			return patientUserEntities;
		}
		finally{
			dbh.closeConnection();
		}
	}

	private PatientUserEntity getAllReferenceAttributeforPatient(PatientUserEntity patientUserEntity)
			throws ClassNotFoundException, SQLException {
		if (patientUserEntity == null) {
			return null;
		}
		AppointmentEntity entity = new AppointmentEntity(patientUserEntity, null, null, null, null, null, -1,null,null);
		AppointSearchEntity criteria = new AppointSearchEntity(entity);
		List<AppointmentEntity> list = AppointmentDAL.getInstance().getAppointment(criteria, patientUserEntity, null);
		patientUserEntity.addAllAppointmentList(list);
		return patientUserEntity;
	}

	public PatientUserEntity createUser(UserEntity entity) throws ClassNotFoundException, SQLException, DALException {
		if (!(entity instanceof PatientUserEntity)) {
			return null;
		}
		PatientUserDatabaseHelper databaseHelper = new PatientUserDatabaseHelper();
		try {
			return getAllReferenceAttributeforPatient(databaseHelper.insertPatientUser(entity));
		} finally {
			databaseHelper.closeConnection();
		}
	}

	public PatientUserEntity searchPatient(UserEntity entity) throws ClassNotFoundException, SQLException {
		PatientUserDatabaseHelper dbh = new PatientUserDatabaseHelper();
		try {
			PatientUserEntity patient = dbh.searchPatient(entity);
			return getAllReferenceAttributeforPatient(patient);
		} finally {
			dbh.closeConnection();
		}
	}

	public PatientUserEntity getByID(int id) throws SQLException, ClassNotFoundException {
		PatientUserDatabaseHelper databaseHelper = new PatientUserDatabaseHelper();
		try {
			return getAllReferenceAttributeforPatient(
					(PatientUserEntity) databaseHelper.queryPatientByID(String.valueOf(id)));
		} finally {
			databaseHelper.closeConnection();
		}
	}

	public PatientUserEntity updatePatientHealth(PatientUserEntity patient) throws Exception {
		PatientHealthDatabaseHelper dbh = new PatientHealthDatabaseHelper();
		try {
			dbh.updatePatientHealth(patient);
			patient.setHealthEntities(dbh.queryHealthByPatientID(patient.getId()));
			return patient;
		} finally {
			dbh.closeConnection();
		}
	}

}
