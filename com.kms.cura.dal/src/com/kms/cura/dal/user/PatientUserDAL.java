package com.kms.cura.dal.user;

import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.database.PatientUserDatabaseHelper;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;

public class PatientUserDAL extends UserDAL {
	private static final String PATIENT_TABLE_NAME = "Patient";
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

	public List<Entity> getAll(DatabaseHelper dbh) throws ClassNotFoundException, SQLException {
		return super.getAll(PATIENT_TABLE_NAME, dbh);
	}

	@Override
	public PatientUserEntity createUser(UserEntity entity) throws ClassNotFoundException, SQLException, DALException {
		return (PatientUserEntity) super.createUser(entity);
	}

	@Override
	protected UserEntity insertUser(UserEntity entity) throws SQLException, DALException, ClassNotFoundException {
		if (!(entity instanceof PatientUserEntity)) {
			// do whatever with error
		}
		PatientUserDatabaseHelper dbh = new PatientUserDatabaseHelper();
		return dbh.insertUser((PatientUserEntity) entity);
	}

	public PatientUserEntity searchPatient(UserEntity entity) throws ClassNotFoundException, SQLException {
		PatientUserDatabaseHelper dbh = new PatientUserDatabaseHelper();
		PatientUserEntity patient = dbh.searchPatient(entity);
		return patient;
	}
}
