package com.kms.cura.dal.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.DegreeDAL;
import com.kms.cura.dal.FacilityDAL;
import com.kms.cura.dal.SpecialityDAL;
import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.database.UserDatabaseHelper;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.mapping.DoctorColumn;
import com.kms.cura.dal.mapping.PatientColumn;
import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
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

	public List<Entity> getAll() throws ClassNotFoundException, SQLException {
		return super.getAll(DOCTOR_TABLE_NAME);
	}

	@Override
	public DoctorUserEntity createUser(UserEntity entity) throws ClassNotFoundException, SQLException, DALException {
		return (DoctorUserEntity) super.createUser(entity);
	}

	@Override
	protected ResultSet insertUser(UserEntity entity, UserDatabaseHelper dbh) throws SQLException, DALException {
		if (!(entity instanceof DoctorUserEntity)) {
			return null;
		}
		return dbh.insertDoctorUser((DoctorUserEntity) entity);
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet, DatabaseHelper dbh)
			throws SQLException, ClassNotFoundException {
		ResultSet userTableRS = dbh.queryByID("Users", resultSet.getInt("user_id"),"id");
		ResultSet doctorSpecialityRS = dbh.queryByReferenceID("Doctor_Specialties", "doctor_id",
				resultSet.getInt("user_id"));

		List<SpecialityEntity> specialties = new ArrayList<SpecialityEntity>();

		while (doctorSpecialityRS.next()) {
			specialties.add(
					(SpecialityEntity) SpecialityDAL.getInstance().getByID(doctorSpecialityRS.getInt("speciality_id")));
		}

		ResultSet doctorFacilityRS = dbh.queryByReferenceID("Doctor_Facilities", "doctor_id",
				resultSet.getInt("user_id"));
		List<FacilityEntity> facilities = new ArrayList<FacilityEntity>();

		while (doctorFacilityRS.next()) {
			facilities.add((FacilityEntity) FacilityDAL.getInstance().getByID(doctorFacilityRS.getInt("facility_id")));
		}
		if (userTableRS.next()) {
			return new DoctorUserEntity(resultSet.getString(DoctorColumn.USER_ID.getColumnName()),
					resultSet.getString(DoctorColumn.NAME.getColumnName()), userTableRS.getString("email"),
					userTableRS.getString("password"), resultSet.getString(DoctorColumn.PHONE.getColumnName()),
					(DegreeEntity) DegreeDAL.getInstance()
							.getByID(resultSet.getInt(DoctorColumn.DEGREE_ID.getColumnName())),
					specialties, resultSet.getDouble(DoctorColumn.RATING.getColumnName()),
					resultSet.getInt(DoctorColumn.EXPERIENCE.getColumnName()),
					resultSet.getDouble(DoctorColumn.MIN_PRICE.getColumnName()),
					resultSet.getDouble(DoctorColumn.MAX_PRICE.getColumnName()), facilities,
					resultSet.getString(DoctorColumn.GENDER.getColumnName()),
					resultSet.getDate(DoctorColumn.BIRTH.getColumnName()),
					resultSet.getString(DoctorColumn.LOCATION.getColumnName()),
					resultSet.getString(DoctorColumn.INSURANCE.getColumnName()));
		}
		return null;
	}

	public DoctorUserEntity searchDoctorbyID(int id) throws ClassNotFoundException, SQLException{
		DatabaseHelper dbh = null;
		try {
			dbh = new DatabaseHelper();
			ResultSet rs = super.searchUserbyID(DOCTOR_TABLE_NAME, id, DoctorColumn.USER_ID.getColumnName(),dbh);
	    	if(rs != null && rs.next()){
	    		return (DoctorUserEntity) getEntityFromResultSet(rs, dbh);
	    	}
	    	return null;
		} 
		finally{
			if(dbh != null){
				dbh.closeConnection();
			}
		}
    }
	
	
}
