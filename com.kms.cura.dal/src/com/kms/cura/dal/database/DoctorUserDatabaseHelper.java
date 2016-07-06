package com.kms.cura.dal.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.WorkingHourEntity;
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
			if (stmt != null) {
				stmt.close();
			}
			if (stmt2 != null) {
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
		referenceRows.clear();
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
		List<WorkingHourEntity> workingHour = doctorUserEntity.getWorkingTime();
		List<OpeningHour> hours = workingHour.get(0).getWorkingTime();
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		String doctorID = doctorUserEntity.getId();
		String facilityID = workingHour.get(0).getFacilityEntity().getId();
		builder.append("INSERT INTO ");
		builder.append(Doctor_FacilityColumn.TABLE_NAME);
		builder.append(" VALUES (?, ?, ?, ?, ?);");
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.setInt(1, Integer.parseInt(doctorID));
			for (int i = 0; i < hours.size(); ++i) {
				OpeningHour hour = hours.get(i);
				stmt.setInt(2, Integer.parseInt(facilityID));
				stmt.setInt(3, hour.getDayOfTheWeek().getCode());
				stmt.setTime(4, hour.getOpenTime());
				stmt.setTime(5, hour.getCloseTime());
				stmt.executeUpdate();
			}
			for (int k = 1; k < workingHour.size(); ++k) {
				stmt.setInt(2, Integer.parseInt(workingHour.get(k).getFacilityEntity().getId()));
				stmt.setInt(3, -1);
				stmt.setTime(4, null);
				stmt.setTime(5, null);
				stmt.executeUpdate();
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	@Override
	protected UserEntity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		UserDatabaseHelper dbh = new UserDatabaseHelper();
		UserEntity userEntity = (UserEntity) dbh.queryByID(UserColumn.TABLE_NAME,
				resultSet.getInt(UserColumn.USER_ID.getColumnName()));
		ResultSet doctorSpecialityRS = null;
		ResultSet workingHourRS = null;
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
			WorkingHourDatabaseHelper dbhWorkingHour = new WorkingHourDatabaseHelper();
			int doctorID = resultSet.getInt(DoctorColumn.USER_ID.getColumnName());
			List<Integer> allFacilityID = getAllFacilityIDfromDoctorID(
					resultSet.getInt(DoctorColumn.USER_ID.getColumnName()));
			List<WorkingHourEntity> workingTime = new ArrayList<>();
			for (int id : allFacilityID) {
				workingTime.add(new WorkingHourEntity(dbhWorkingHour.querybyID(doctorID, id),
						(FacilityEntity) FacilityDAL.getInstance().getByID(id, new FacilityDatabaseHelper())));

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
						resultSet.getDouble(DoctorColumn.MAX_PRICE.getColumnName()), workingTime,
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
			if (workingHourRS != null) {
				workingHourRS.close();
			}
			if (doctorSpecialityRS != null) {
				doctorSpecialityRS.close();
			}
		}
	}

	public DoctorUserEntity searchDoctor(UserEntity entity) throws ClassNotFoundException, SQLException {
		return (DoctorUserEntity) queryUserEntitybyEmailPassword(DoctorColumn.TABLE_NAME, UserColumn.TABLE_NAME,
				entity.getEmail(), entity.getPassword(), DoctorColumn.USER_ID.getColumnName(),
				UserColumn.ID.getColumnName());
	}

	private List<Integer> getAllFacilityIDfromDoctorID(int doctorID) throws SQLException {
		List<Integer> allFacilityID = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT DISTINCT ");
		builder.append(Doctor_FacilityColumn.FACILITY_ID.getColumnName());
		builder.append(" FROM ");
		builder.append(Doctor_FacilityColumn.TABLE_NAME);
		builder.append(" WHERE ");
		builder.append(Doctor_FacilityColumn.DOCTOR_ID.getColumnName());
		builder.append(" = ?");
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.setInt(1, doctorID);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					allFacilityID.add(rs.getInt(Doctor_FacilityColumn.FACILITY_ID.getColumnName()));
				}
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return allFacilityID;
	}

	public void addNewFacilityWorkingHourForOneDoctor(int doctorID, List<WorkingHourEntity> changes) throws Exception {
		StringBuilder builder = new StringBuilder();
		PreparedStatement stmt = null;
		builder.append("INSERT INTO ");
		builder.append(Doctor_FacilityColumn.TABLE_NAME);
		builder.append(" VALUES (?, ?, ?, ?, ?);");
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.setInt(1, doctorID);
			for (int i = 0; i < changes.size(); ++i) {
				List<OpeningHour> hours = changes.get(i).getWorkingTime();
				for (OpeningHour hour : hours) {
					stmt.setInt(2, Integer.parseInt(changes.get(i).getFacilityEntity().getId()));
					stmt.setInt(3, hour.getDayOfTheWeek().getCode());
					stmt.setTime(4, hour.getOpenTime());
					stmt.setTime(5, hour.getCloseTime());
					if (stmt.executeUpdate() == 0) {
						throw new Exception("Add Working Hour failed");
					}
				}
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public void deleteFacilityWorkingHourForOneDoctorID(int doctorID, List<WorkingHourEntity> changes)
			throws Exception {
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM ");
		builder.append(Doctor_FacilityColumn.TABLE_NAME);
		builder.append(" WHERE ");
		builder.append(Doctor_FacilityColumn.DOCTOR_ID.getColumnName());
		builder.append("= ? AND ");
		builder.append(Doctor_FacilityColumn.FACILITY_ID.getColumnName());
		builder.append(" = ? AND ");
		builder.append(Doctor_FacilityColumn.WORKING_DAY.getColumnName());
		builder.append(" = ? AND ");
		builder.append(Doctor_FacilityColumn.START_WORKING_TIME.getColumnName());
		builder.append(" = ? AND ");
		builder.append(Doctor_FacilityColumn.END_WORKING_TIME.getColumnName());
		builder.append(" = ?");
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.setInt(1, doctorID);
			for (int i = 0; i < changes.size(); ++i) {
				List<OpeningHour> hours = changes.get(i).getWorkingTime();
				for (OpeningHour hour : hours) {
					stmt.setInt(2, Integer.parseInt(changes.get(i).getFacilityEntity().getId()));
					stmt.setInt(3, hour.getDayOfTheWeek().getCode());
					stmt.setTime(4, hour.getOpenTime());
					stmt.setTime(5, hour.getCloseTime());
					if (stmt.executeUpdate() == 0) {
						throw new Exception("Delete Working Hour failed");
					}
				}
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public Entity updateDoctor(DoctorUserEntity doctorUserEntity) throws NumberFormatException, Exception {
		boolean equal = true;
		DoctorUserEntity oldDoctor = searchDoctor(doctorUserEntity);
		// Update only working hour
		List<WorkingHourEntity> oldDoctorWH = oldDoctor.getWorkingTime();
		List<WorkingHourEntity> newDoctorWH = doctorUserEntity.getWorkingTime();
		if (oldDoctorWH.size() == newDoctorWH.size()) {
			for (int i = 0; i < oldDoctorWH.size(); ++i) {
				if (!oldDoctorWH.get(i).equals(newDoctorWH.get(i))) {
					equal = false;
					break;
				}
			}
			if (equal) {
				return oldDoctor;
			}
		}
		try {
			con.setAutoCommit(false);
			deleteFacilityWorkingHourForOneDoctorID(Integer.parseInt(oldDoctor.getId()), oldDoctorWH);
			addNewFacilityWorkingHourForOneDoctor(Integer.parseInt(oldDoctor.getId()), newDoctorWH);
			con.commit();
			return searchDoctor(doctorUserEntity);
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

}
