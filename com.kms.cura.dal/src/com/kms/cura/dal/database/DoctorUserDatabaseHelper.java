package com.kms.cura.dal.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kms.cura.dal.DegreeDAL;
import com.kms.cura.dal.FacilityDAL;
import com.kms.cura.dal.SpecialityDAL;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.mapping.DoctorColumn;
import com.kms.cura.dal.mapping.Doctor_FacilityColumn;
import com.kms.cura.dal.mapping.Doctor_SpecialityColumn;
import com.kms.cura.dal.mapping.UserColumn;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;

public class DoctorUserDatabaseHelper extends UserDatabaseHelper {

	public DoctorUserDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	public UserEntity insertUser(UserEntity entity) throws SQLException, DALException, ClassNotFoundException {
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		UserEntity userEntity = super.insertUser(entity);
		ResultSet rs = null;

		if (!(entity instanceof DoctorUserEntity)) {
			return null;
		}
		try {
			con.setAutoCommit(false);
			int userId = Integer.parseInt(userEntity.getId());
			entity.setId(String.valueOf(userId));
			stmt = getInsertStatementForUser((DoctorUserEntity) entity);
			stmt.executeUpdate();

			insertToDoctorReferenceTables((DoctorUserEntity) entity);
			con.commit();

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM ");
			sb.append(DoctorColumn.TABLE_NAME);
			sb.append(" WHERE ");
			sb.append(DoctorColumn.USER_ID.getColumnName());
			sb.append(" = ?");

			stmt2 = con.prepareStatement(sb.toString());
			stmt2.setInt(1, userId);
			rs = stmt2.executeQuery();
			con.commit();
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
			if(stmt != null){
				stmt.close();
			}
			if(stmt2 != null){
				stmt2.close();
			}
		}
	}

	private Map<String, Object> getColumnValueMapForEntity(DoctorUserEntity entity) {
		if (entity == null) {
			// handle error;
			return null;
		}
		Map<String, Object> columnValueMap = new LinkedHashMap<String, Object>();
		for (DoctorColumn doctortColumn : DoctorColumn.values()) {
			String columnName = doctortColumn.getColumnName();
			switch (doctortColumn) {
			case BIRTH:
				columnValueMap.put(columnName, entity.getBirth());
				break;
			case GENDER:
				columnValueMap.put(columnName, entity.getGender());
				break;
			case INSURANCE:
				columnValueMap.put(columnName, entity.getInsurance());
				break;
			case NAME:
				columnValueMap.put(columnName, entity.getName());
				break;
			case USER_ID:
				columnValueMap.put(columnName, entity.getId());
				break;
			case DEGREE_ID:
				columnValueMap.put(columnName, entity.getDegree().getId());
				break;
			case EXPERIENCE:
				columnValueMap.put(columnName, entity.getExperience());
				break;
			case MAX_PRICE:
				columnValueMap.put(columnName, entity.getMaxPrice());
				break;
			case MIN_PRICE:
				columnValueMap.put(columnName, entity.getMinPrice());
				break;
			case PHONE:
				columnValueMap.put(columnName, entity.getPhone());
				break;
			case RATING:
				columnValueMap.put(columnName, entity.getRating());
				break;
			default:
				break;
			}
		}
		return columnValueMap;
	}

	public PreparedStatement getInsertStatementForUser(DoctorUserEntity entity) throws SQLException {
		Map<String, Object> doctorValueMap = getColumnValueMapForEntity(entity);
		String databaseName = DoctorColumn.TABLE_NAME;
		String databaseMethod = "INSERT INTO";
		return createPreparedStatement(doctorValueMap, databaseName, databaseMethod);
	}

	protected void insertToDoctorReferenceTables(DoctorUserEntity entity) throws SQLException {
		List<ReferenceTableRow> referenceRows = new ArrayList<ReferenceTableRow>();
		insertWorkingHour(entity);
		for (SpecialityEntity speciality : entity.getSpeciality()) {
			ReferenceTableRow referenceRow = new ReferenceTableRow();
			referenceRow.setFirstRefKey(Doctor_SpecialityColumn.SPECIALITY_ID.getColumnName());
			referenceRow.setFirstValue(speciality.getId());
			referenceRow.setSecondRefKey(Doctor_SpecialityColumn.DOCTOR_ID.getColumnName());
			referenceRow.setSecondValue(entity.getId());
			referenceRows.add(referenceRow);
		}
		insertReferenceRowsToReferenceTable(Doctor_SpecialityColumn.TABLE_NAME, referenceRows);
	}

	protected void insertWorkingHour(DoctorUserEntity doctorUserEntity) throws SQLException {
		List<OpeningHour> hours = doctorUserEntity.getFacility().get(0).getOpeningHours();
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();

		builder.append("INSERT INTO ");
		builder.append(Doctor_FacilityColumn.TABLE_NAME);
		builder.append(" VALUES (?, ?, ?, ?, ?);");
		try{
			for(int i=0; i<hours.size();++i){
				stmt = con.prepareStatement(builder.toString());
				stmt.setInt(1,Integer.parseInt(doctorUserEntity.getId()));
				stmt.setInt(2,Integer.parseInt(doctorUserEntity.getFacility().get(0).getId()));
				stmt.setInt(3,hours.get(i).getDayOfTheWeek().getCode());
				stmt.setTime(4, hours.get(i).getOpenTime());
				stmt.setTime(5, hours.get(i).getCloseTime());
				stmt.executeUpdate();
			}
		}
		finally{
			stmt.close();
		}
	}

	@Override
	protected UserEntity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		UserDatabaseHelper dbh = new UserDatabaseHelper();
		UserEntity userEntity = (UserEntity) dbh.queryByID(UserColumn.TABLE_NAME,
				resultSet.getInt(UserColumn.USER_ID.getColumnName()));
		ResultSet doctorSpecialityRS = null;
		ResultSet doctorFacilityRS = null;
		try {
			doctorSpecialityRS = dbh.queryByReferenceID(Doctor_SpecialityColumn.TABLE_NAME,
					Doctor_SpecialityColumn.DOCTOR_ID.getColumnName(),
					resultSet.getInt(DoctorColumn.USER_ID.getColumnName()));
			List<SpecialityEntity> specialties = new ArrayList<SpecialityEntity>();
			while (doctorSpecialityRS.next()) {
				SpecialityEntity temp = (SpecialityEntity) SpecialityDAL.getInstance().getByID(
						doctorSpecialityRS.getInt(Doctor_SpecialityColumn.SPECIALITY_ID.getColumnName()),
						new SpecialityDatabaseHelper());
				specialties.add(temp);
			}
			doctorFacilityRS = dbh.queryByReferenceID(Doctor_FacilityColumn.TABLE_NAME,
					Doctor_FacilityColumn.DOCTOR_ID.getColumnName(),
					resultSet.getInt(DoctorColumn.USER_ID.getColumnName()));
			List<FacilityEntity> facilities = new ArrayList<FacilityEntity>();
			while (doctorFacilityRS.next()) {
				facilities.add((FacilityEntity) FacilityDAL.getInstance().getByID(
						doctorFacilityRS.getInt(Doctor_FacilityColumn.FACILITY_ID.getColumnName()),
						new FacilityDatabaseHelper()));
			}
			if (userEntity != null) {
				DoctorUserEntity doctor = new DoctorUserEntity(
						resultSet.getString(DoctorColumn.USER_ID.getColumnName()),
						resultSet.getString(DoctorColumn.NAME.getColumnName()), userEntity.getEmail(),
						userEntity.getPassword(), resultSet.getString(DoctorColumn.PHONE.getColumnName()),
						(DegreeEntity) DegreeDAL.getInstance().getByID(
								resultSet.getInt(DoctorColumn.DEGREE_ID.getColumnName()), new DegreeDatabaseHelper()),
						specialties, resultSet.getDouble(DoctorColumn.RATING.getColumnName()),
						resultSet.getInt(DoctorColumn.EXPERIENCE.getColumnName()),
						resultSet.getDouble(DoctorColumn.MIN_PRICE.getColumnName()),
						resultSet.getDouble(DoctorColumn.MAX_PRICE.getColumnName()), facilities,
						resultSet.getString(DoctorColumn.GENDER.getColumnName()),
						resultSet.getDate(DoctorColumn.BIRTH.getColumnName()),
						resultSet.getString(DoctorColumn.INSURANCE.getColumnName()));
				return doctor;
			}
			return null;
		} finally {
			if (resultSet.isAfterLast()) {
				resultSet.close();
			}
			doctorFacilityRS.close();
			doctorSpecialityRS.close();
		}
	}

	public DoctorUserEntity searchDoctor(UserEntity entity) throws ClassNotFoundException, SQLException {
		return (DoctorUserEntity) queryUserEntitybyEmailPassword(DoctorColumn.TABLE_NAME, UserColumn.TABLE_NAME,
				entity.getEmail(), entity.getPassword(), DoctorColumn.USER_ID.getColumnName(),
				UserColumn.ID.getColumnName());
	}


	public List<OpeningHour> getWorkingHour(int doctorID, int facilityID) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT ");
		builder.append(Doctor_FacilityColumn.WORKING_DAY.getColumnName());
		builder.append(", ");
		builder.append(Doctor_FacilityColumn.START_WORKING_TIME.getColumnName());
		builder.append(", ");
		builder.append(Doctor_FacilityColumn.END_WORKING_TIME.getColumnName());
		builder.append(" FROM ");
		builder.append(Doctor_FacilityColumn.TABLE_NAME);
		builder.append(" WHERE ");
		builder.append(Doctor_FacilityColumn.DOCTOR_ID.getColumnName());
		builder.append(" = ? AND ");
		builder.append(Doctor_FacilityColumn.FACILITY_ID.getColumnName());
		builder.append(" = ?");
		try{
			stmt = con.prepareStatement(builder.toString());
			stmt.setInt(1, doctorID);
			stmt.setInt(2, facilityID);
			rs = stmt.executeQuery();
			if(rs != null){
				return getListfromResultSet(rs);
			}
		}
		finally{
			stmt.close();
		}
		return null;

	}
	
	private List<OpeningHour> getListfromResultSet(ResultSet rs) throws SQLException{
		List<OpeningHour> hours = new ArrayList<>();
		while(rs.next()){
			OpeningHour hour = new OpeningHour(DayOfTheWeek.getDayOfTheWeek((rs.getInt(Doctor_FacilityColumn.WORKING_DAY.getColumnName()))), 
					rs.getTime(Doctor_FacilityColumn.START_WORKING_TIME.getColumnName()), 
					rs.getTime(Doctor_FacilityColumn.END_WORKING_TIME.getColumnName()));
			hours.add(hour);
		}
		return hours;
	}
}
