package com.kms.cura.dal.user;

import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.database.DoctorUserDatabaseHelper;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;

public class DoctorUserDAL extends UserDAL {
	private static final String DOCTOR_TABLE_NAME = "Doctor";
	private static DoctorUserDAL _instance;

	private DoctorUserDAL() {
		// hide constructor
	}

	public static DoctorUserDAL getInstance() {
		if (_instance == null) {
			_instance = new DoctorUserDAL();
		}
		return _instance;
	}

	public List<Entity> getAll(DatabaseHelper dbh) throws ClassNotFoundException, SQLException {
		return super.getAll(DOCTOR_TABLE_NAME, dbh);
	}

	@Override
	public DoctorUserEntity createUser(UserEntity entity) throws ClassNotFoundException, SQLException, DALException {
		return (DoctorUserEntity) super.createUser(entity);
	}

	@Override
	protected UserEntity insertUser(UserEntity entity) throws SQLException, DALException, ClassNotFoundException {
		if (!(entity instanceof DoctorUserEntity)) {
			return null;
		}
		DoctorUserDatabaseHelper dbh = new DoctorUserDatabaseHelper();
		return dbh.insertUser((DoctorUserEntity) entity);
	}

	public DoctorUserEntity searchDoctor(UserEntity entity) throws ClassNotFoundException, SQLException {
		DoctorUserDatabaseHelper dbh = new DoctorUserDatabaseHelper();
		return dbh.searchDoctor(entity);
	}
	
	public List<DoctorUserEntity> searchDoctorFunction (DoctorUserEntity doctor) throws SQLException, ClassNotFoundException{
		DoctorUserDatabaseHelper dbh = new DoctorUserDatabaseHelper();
		return dbh.searchDoctorFunction(doctor);
	}
}
