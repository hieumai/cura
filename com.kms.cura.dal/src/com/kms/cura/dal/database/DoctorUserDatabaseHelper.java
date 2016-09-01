package com.kms.cura.dal.database;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.exception.DuplicatedUserEmailException;
import com.kms.cura.dal.mapping.DoctorColumn;
import com.kms.cura.dal.mapping.Doctor_FacilityColumn;
import com.kms.cura.dal.mapping.Doctor_SpecialityColumn;
import com.kms.cura.dal.mapping.FacilityColumn;
import com.kms.cura.dal.mapping.SpecialityColumn;
import com.kms.cura.dal.mapping.UserColumn;
import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.entity.DoctorSearchEntity;
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

	public DoctorUserEntity insertDoctorUser(UserEntity entity)
			throws SQLException, DALException, ClassNotFoundException, IOException {
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		if (!(entity instanceof DoctorUserEntity)) {
			return null;
		}
		if (queryByEmail(entity.getEmail()) != null) {
			throw new DuplicatedUserEmailException(entity.getEmail());
		}
		try {
			con.setAutoCommit(false);
			super.insertUser(entity);
			UserEntity newlyInsertedUser = queryByEmail(entity.getEmail());
			entity.setId(newlyInsertedUser.getId());
			stmt = getInsertStatementForUser((DoctorUserEntity) entity);
			stmt.executeUpdate();
			insertToDoctorReferenceTables((DoctorUserEntity) entity);
			con.commit();

			return queryDoctorByID(newlyInsertedUser.getId());
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
		return createInsertPreparedStatement(doctorValueMap, databaseName);
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
	protected DoctorUserEntity getEntityFromResultSet(ResultSet resultSet)
			throws SQLException, ClassNotFoundException, IOException {
		ResultSet doctorSpecialityRS = null;
		ResultSet workingHourRS = null;

		try {

			String path = resultSet.getString(UserColumn.IMAGE_PATH.getColumnName());

			doctorSpecialityRS = queryByReferenceID(Doctor_SpecialityColumn.TABLE_NAME,
					Doctor_SpecialityColumn.DOCTOR_ID.getColumnName(),
					resultSet.getInt(DoctorColumn.USER_ID.getColumnName()));
			List<SpecialityEntity> specialties = new ArrayList<SpecialityEntity>();
			while (doctorSpecialityRS.next()) {
				SpecialityDatabaseHelper specialityDatabaseHelper = new SpecialityDatabaseHelper();
				try {
					SpecialityEntity specialityEntity = specialityDatabaseHelper.queryByID(
							doctorSpecialityRS.getInt(Doctor_SpecialityColumn.SPECIALITY_ID.getColumnName()));
					specialties.add(specialityEntity);
				} finally {
					specialityDatabaseHelper.closeConnection();
				}
			}
			WorkingHourDatabaseHelper dbhWorkingHour = new WorkingHourDatabaseHelper();
			FacilityDatabaseHelper facilityDBHelper = new FacilityDatabaseHelper();
			List<WorkingHourEntity> workingTime = new ArrayList<>();
			try {
				int doctorID = resultSet.getInt(DoctorColumn.USER_ID.getColumnName());
				List<Integer> allFacilityID = getAllFacilityIDfromDoctorID(
						resultSet.getInt(DoctorColumn.USER_ID.getColumnName()));
				for (int facilityId : allFacilityID) {
					workingTime.add(new WorkingHourEntity(dbhWorkingHour.querybyID(doctorID, facilityId),
							facilityDBHelper.queryByID(facilityId)));
				}
			} finally {
				dbhWorkingHour.closeConnection();
				facilityDBHelper.closeConnection();
			}
			DegreeDatabaseHelper degreeDatabaseHelper = new DegreeDatabaseHelper();
			DegreeEntity degree = null;
			try {
				degree = degreeDatabaseHelper.queryByID(resultSet.getInt(DoctorColumn.DEGREE_ID.getColumnName()));
			} finally {
				degreeDatabaseHelper.closeConnection();
			}
			DoctorUserEntity doctor = new DoctorUserEntity(resultSet.getString(DoctorColumn.USER_ID.getColumnName()),
					resultSet.getString(DoctorColumn.NAME.getColumnName()),
					resultSet.getString(UserColumn.EMAIL.getColumnName()), 
					resultSet.getString(UserColumn.PASSWORD.getColumnName()),
					resultSet.getString(DoctorColumn.PHONE.getColumnName()), degree, specialties,
					resultSet.getDouble(DoctorColumn.RATING.getColumnName()),
					resultSet.getInt(DoctorColumn.EXPERIENCE.getColumnName()),
					resultSet.getDouble(DoctorColumn.MIN_PRICE.getColumnName()),
					resultSet.getDouble(DoctorColumn.MAX_PRICE.getColumnName()), workingTime,
					resultSet.getString(DoctorColumn.GENDER.getColumnName()),
					resultSet.getDate(DoctorColumn.BIRTH.getColumnName()),
					resultSet.getString(DoctorColumn.INSURANCE.getColumnName()), path);
			return doctor;
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

	public DoctorUserEntity searchDoctor(UserEntity entity) throws ClassNotFoundException, SQLException, IOException {
		return (DoctorUserEntity) queryUserEntitybyEmailPassword(DoctorColumn.TABLE_NAME, entity.getEmail(),
				entity.getPassword(), DoctorColumn.USER_ID.getColumnName());
	}

	public List<DoctorUserEntity> searchDoctorFunction(DoctorSearchEntity search)
			throws SQLException, ClassNotFoundException, IOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<DoctorUserEntity> result = new ArrayList<DoctorUserEntity>();

		try {
			stmt = getSearchStatement(search);

			rs = stmt.executeQuery();

			while (rs.next()) {
				result.add((DoctorUserEntity) getEntityFromResultSet(rs));
			}
			return result;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	protected PreparedStatement getSearchStatement(DoctorSearchEntity search) throws SQLException {
		DoctorUserEntity doctor = search.getDoctor();
		PreparedStatement stmt = null;
		int count = 1;
		stmt = con.prepareStatement(getSearchQuery(search));

		String name = doctor.getName();
		if (name == null) {
			name = "";
		}
		stmt.setString(count, "%" + name + "%");
		count++;

		List<SpecialityEntity> specialities = doctor.getSpeciality();
		if (specialities != null && specialities.size() > 0 && !doctor.getSpeciality().get(0).equals("")) {
			for (SpecialityEntity specialty : doctor.getSpeciality()) {
				stmt.setString(count, specialty.getName());
				count++;
			}
		}

		String city = doctor.getWorkingTime().get(0).getFacilityEntity().getCity();
		stmt.setString(count, "%" + city + "%");
		count++;

		return stmt;
	}

	protected String getSearchQuery(DoctorSearchEntity search) {
		DoctorUserEntity doctor = search.getDoctor();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT ");

		sb.append(DoctorColumn.USER_ID.getColumnName());
		sb.append(", ");
		sb.append(UserColumn.TABLE_NAME);
		sb.append(".");
		sb.append(UserColumn.EMAIL.getColumnName());
		sb.append(", ");
		sb.append(UserColumn.TABLE_NAME);
		sb.append(".");
		sb.append(UserColumn.PASSWORD.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.TABLE_NAME);
		sb.append(".");
		sb.append(DoctorColumn.NAME.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.TABLE_NAME);
		sb.append(".");
		sb.append(DoctorColumn.PHONE.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.DEGREE_ID.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.EXPERIENCE.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.MIN_PRICE.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.MAX_PRICE.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.RATING.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.GENDER.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.BIRTH.getColumnName());
		sb.append(", ");
		sb.append(DoctorColumn.INSURANCE.getColumnName());
		sb.append(", ");
		sb.append(UserColumn.IMAGE_PATH.getColumnName());

		sb.append(" FROM ");
		sb.append(DoctorColumn.TABLE_NAME);

		sb.append(" JOIN ");
		sb.append(Doctor_FacilityColumn.TABLE_NAME);
		sb.append(" ON ");
		sb.append(DoctorColumn.USER_ID.getColumnName());
		sb.append(" = ");
		sb.append(Doctor_FacilityColumn.DOCTOR_ID.getColumnName());
		sb.append(" JOIN ");
		sb.append(FacilityColumn.TABLE_NAME);
		sb.append(" ON ");
		sb.append(Doctor_FacilityColumn.FACILITY_ID.getColumnName());
		sb.append(" = ");
		sb.append(FacilityColumn.ID.getColumnName());

		sb.append(" JOIN ");
		sb.append(UserColumn.TABLE_NAME);
		sb.append(" ON ");
		sb.append(UserColumn.TABLE_NAME);
		sb.append(".");
		sb.append(UserColumn.ID.getColumnName());
		sb.append(" = ");
		sb.append(DoctorColumn.TABLE_NAME);
		sb.append(".");
		sb.append(DoctorColumn.USER_ID.getColumnName());

		sb.append(" JOIN ");
		sb.append(Doctor_SpecialityColumn.TABLE_NAME);
		sb.append(" ON ");
		sb.append(DoctorColumn.TABLE_NAME);
		sb.append(".");
		sb.append(DoctorColumn.USER_ID.getColumnName());
		sb.append(" = ");
		sb.append(Doctor_SpecialityColumn.TABLE_NAME);
		sb.append(".");
		sb.append(Doctor_SpecialityColumn.DOCTOR_ID.getColumnName());

		sb.append(" JOIN ");
		sb.append(SpecialityColumn.TABLE_NAME);
		sb.append(" ON ");
		sb.append(Doctor_SpecialityColumn.TABLE_NAME);
		sb.append(".");
		sb.append(Doctor_SpecialityColumn.SPECIALITY_ID.getColumnName());
		sb.append(" = ");
		sb.append(SpecialityColumn.TABLE_NAME);
		sb.append(".");
		sb.append(SpecialityColumn.ID.getColumnName());

		sb.append(" WHERE ");
		sb.append(DoctorColumn.TABLE_NAME);
		sb.append(".");
		sb.append(DoctorColumn.NAME.getColumnName());
		sb.append(" LIKE ?");

		List<SpecialityEntity> specialities = doctor.getSpeciality();
		if (specialities != null && specialities.size() > 0 && !doctor.getSpeciality().get(0).equals("")) {
			for (int i = 0; i < doctor.getSpeciality().size(); i++) {
				if (i == 0) {
					sb.append(" AND ( ");
				} else {
					sb.append(" OR ");
				}

				sb.append(SpecialityColumn.TABLE_NAME);
				sb.append(".");
				sb.append(SpecialityColumn.NAME.getColumnName());
				sb.append(" LIKE ");
				sb.append(" ? ");
			}
			sb.append(")");
		}

		sb.append(" AND ");
		sb.append(FacilityColumn.CITY_KEYWORD.getColumnName());
		sb.append(" LIKE ? ");
		return sb.toString();
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

	public DoctorUserEntity queryDoctorByID(String id) throws SQLException, ClassNotFoundException, IOException {
		return (DoctorUserEntity) queryUserEntitybyId(DoctorColumn.TABLE_NAME, id,
				DoctorColumn.USER_ID.getColumnName());
	}

	public List<DoctorUserEntity> getAllDoctor() throws SQLException, ClassNotFoundException, IOException {
		List<DoctorUserEntity> list = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM ");
		builder.append(DoctorColumn.TABLE_NAME);
		builder.append(" LEFT JOIN ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(" ON ");
		builder.append(DoctorColumn.TABLE_NAME);
		builder.append(".");
		builder.append(DoctorColumn.USER_ID.getColumnName());
		builder.append(" = ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(".");
		builder.append(UserColumn.ID.getColumnName());
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					list.add(getEntityFromResultSet(rs));
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return list;
	}

	public List<DoctorUserEntity> queryDoctorByFacility(FacilityEntity facilityEntity)
			throws SQLException, ClassNotFoundException, IOException {

		List<DoctorUserEntity> list = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("select * from ");
		builder.append(DoctorColumn.TABLE_NAME + " d");
		builder.append(" left join ");
		builder.append(
				"(select distinct " + Doctor_FacilityColumn.DOCTOR_ID + ", " + Doctor_FacilityColumn.FACILITY_ID);
		builder.append(" from ");
		builder.append(Doctor_FacilityColumn.TABLE_NAME + ") f");
		builder.append(" on d." + DoctorColumn.USER_ID + " = f." + Doctor_FacilityColumn.DOCTOR_ID);
		builder.append(" left join ");
		builder.append(UserColumn.TABLE_NAME + " u");
		builder.append(" on u." + UserColumn.ID + " = d." + DoctorColumn.USER_ID);
		builder.append(" where f." + Doctor_FacilityColumn.FACILITY_ID + " = " + facilityEntity.getId());
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					list.add(getEntityFromResultSet(rs));
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return list;
	}

	public DoctorUserEntity updateDoctorBasic(DoctorUserEntity entity)
			throws SQLException, ClassNotFoundException, IOException {
		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("update " + DoctorColumn.TABLE_NAME);
		builder.append(" set " + DoctorColumn.NAME.getColumnName() + " = '" + entity.getName() + "'");
		builder.append(", " + DoctorColumn.GENDER.getColumnName() + " = '" + entity.getGender() + "'");
		builder.append(", " + DoctorColumn.BIRTH.getColumnName() + " = '" + entity.getBirth().toString() + "'");
		builder.append(", " + DoctorColumn.DEGREE_ID.getColumnName() + " = " + entity.getDegree().getId());
		builder.append(", " + DoctorColumn.EXPERIENCE.getColumnName() + " = " + entity.getExperience());
		builder.append(", " + DoctorColumn.MIN_PRICE.getColumnName() + " = " + entity.getMinPrice());
		builder.append(", " + DoctorColumn.MAX_PRICE.getColumnName() + " = " + entity.getMaxPrice());
		builder.append(" where " + DoctorColumn.USER_ID.getColumnName() + " = " + entity.getId());
		try {
			con.setAutoCommit(false);
			stmt = con.prepareStatement(builder.toString());
			stmt.executeUpdate();
			con.commit();
			return queryDoctorByID(entity.getId());
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

	private void updateSpeciality(DoctorUserEntity entity) throws SQLException, ClassNotFoundException, IOException {
		PreparedStatement stmtDelete = null, stmtInsert = null;
		StringBuilder deleteQuery = new StringBuilder();
		deleteQuery.append("delete from " + Doctor_SpecialityColumn.TABLE_NAME);
		deleteQuery.append(" where " + Doctor_SpecialityColumn.DOCTOR_ID.getColumnName() + " = " + entity.getId() + ";");
		StringBuilder insertQuery = new StringBuilder();
		insertQuery.append("insert into ");
		insertQuery.append(Doctor_SpecialityColumn.TABLE_NAME);
		insertQuery.append(" values (?, ?);");
		try {
			stmtDelete = con.prepareStatement(deleteQuery.toString());
			stmtDelete.executeUpdate();
			stmtInsert = con.prepareStatement(insertQuery.toString());
			stmtInsert.setInt(1, Integer.parseInt(entity.getId()));
			for (SpecialityEntity specialityEntity : entity.getSpeciality()) {
				stmtInsert.setInt(2, Integer.parseInt(specialityEntity.getId()));
				stmtInsert.executeUpdate();
			}
		} finally {
			if (stmtInsert != null) {
				stmtInsert.close();
			}
			if (stmtDelete != null) {
				stmtDelete.close();
			}
		}
	}

	private void updateDoctorWorkingHour(DoctorUserEntity doctorUserEntity) throws NumberFormatException, Exception {
		boolean equal = true;
		DoctorUserEntity oldDoctor = queryDoctorByID(doctorUserEntity.getId());
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
				return;
			}
		}
		deleteFacilityWorkingHourForOneDoctorID(Integer.parseInt(oldDoctor.getId()), oldDoctorWH);
		addNewFacilityWorkingHourForOneDoctor(Integer.parseInt(oldDoctor.getId()), newDoctorWH);
	}

	public DoctorUserEntity updateProfessional(DoctorUserEntity entity) throws NumberFormatException, Exception {
		try {
			con.setAutoCommit(false);
			updateDoctorWorkingHour(entity);
			updateSpeciality(entity);
			con.commit();
			return queryDoctorByID(entity.getId());
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
