package com.kms.cura.dal.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.Doctor_FacilityColumn;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.OpeningHour;

public class WorkingHourDatabaseHelper extends DatabaseHelper {

	public WorkingHourDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return null;
	}

	public OpeningHour getWorkingHourFromResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {
		if (rs.getInt(Doctor_FacilityColumn.WORKING_DAY.getColumnName()) == -1) {
			return null;
		}
		return new OpeningHour(
				DayOfTheWeek.getDayOfTheWeek(rs.getInt(Doctor_FacilityColumn.WORKING_DAY.getColumnName())),
				rs.getTime(Doctor_FacilityColumn.START_WORKING_TIME.getColumnName()),
				rs.getTime(Doctor_FacilityColumn.END_WORKING_TIME.getColumnName()));
	}

	public List<OpeningHour> querybyID(int doctorID, int facilityID) throws SQLException, ClassNotFoundException {
		List<OpeningHour> workingTime = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM ");
		builder.append(Doctor_FacilityColumn.TABLE_NAME);
		builder.append(" WHERE ");
		builder.append(Doctor_FacilityColumn.DOCTOR_ID.getColumnName());
		builder.append(" = ? AND ");
		builder.append(Doctor_FacilityColumn.FACILITY_ID.getColumnName());
		builder.append(" = ?");
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.setInt(1, doctorID);
			stmt.setInt(2, facilityID);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					OpeningHour hour = getWorkingHourFromResultSet(rs);
					if (hour == null) {
						return new ArrayList<OpeningHour>();
					}
					workingTime.add(getWorkingHourFromResultSet(rs));
				}
			}
			return workingTime;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

}
