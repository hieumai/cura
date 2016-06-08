package com.kms.cura.dal.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.database.UserDatabaseHelper;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.mapping.PatientColumn;
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

    public List<Entity> getAll() throws ClassNotFoundException, SQLException {
	return super.getAll(PATIENT_TABLE_NAME);
    }

    @Override
    public PatientUserEntity createUser(UserEntity entity) throws ClassNotFoundException, SQLException, DALException {
	return (PatientUserEntity) super.createUser(entity);
    }

    @Override
    protected ResultSet insertUser(UserEntity entity, UserDatabaseHelper dbh) throws SQLException, DALException {
	if (!(entity instanceof PatientUserEntity)) {
	    // do whatever with error
	}
	return dbh.insertPatientUser((PatientUserEntity) entity);
    }

    @Override
    protected Entity getEntityFromResultSet(ResultSet resultSet, DatabaseHelper dbh) throws SQLException {
	ResultSet userTableRS = dbh.queryByID("Users", resultSet.getInt(PatientColumn.USER_ID.getColumnName()));
	if (userTableRS.next()) {
	    return new PatientUserEntity(resultSet.getString(PatientColumn.USER_ID.getColumnName()),
		    resultSet.getString(PatientColumn.NAME.getColumnName()), userTableRS.getString("email"),
		    userTableRS.getString("password"), resultSet.getString(PatientColumn.GENDER.getColumnName()),
		    resultSet.getDate(PatientColumn.BIRTH.getColumnName()),
		    resultSet.getString(PatientColumn.LOCATION.getColumnName()),
		    resultSet.getString(PatientColumn.INSURANCE.getColumnName()),
		    resultSet.getString(PatientColumn.HEALTH_CONCERN.getColumnName()));
	}
	return null;
    }
    
    public PatientUserEntity searchPatientbyID(int id) throws ClassNotFoundException, SQLException{
    	DatabaseHelper dbh = null;
		try {
			dbh = new DatabaseHelper();
			ResultSet rs = super.searchUserbyID(PATIENT_TABLE_NAME, id, PatientColumn.USER_ID.getColumnName(),dbh);
	    	if(rs != null && rs.next()){
	    		return (PatientUserEntity) getEntityFromResultSet(rs, dbh);
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
