package com.kms.cura.dal.database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.EntityColumn;
import com.kms.cura.dal.mapping.FacilityColumn;
import com.kms.cura.dal.mapping.OpeningHourColumn;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.OpeningHour;

public class FacilityDatabaseHelper extends DatabaseHelper {

	public FacilityDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected FacilityEntity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		List<OpeningHour> openingHours = new ArrayList<OpeningHour>();
		ResultSet openingHoursRS = null;
		try {
			openingHoursRS = this.queryByReferenceID(OpeningHourColumn.TABLE_NAME,
					OpeningHourColumn.FACILITY_ID.getColumnName(), resultSet.getInt(EntityColumn.ID.getColumnName()));
			while (openingHoursRS.next()) {
				openingHours.add(new OpeningHour(
						DayOfTheWeek.getDayOfTheWeek(openingHoursRS.getInt(OpeningHourColumn.WEEK_DAY.getColumnName())),
						openingHoursRS.getTime(OpeningHourColumn.TIME_OPEN.getColumnName()),
						openingHoursRS.getTime(OpeningHourColumn.TIME_CLOSE.getColumnName())));
			}
			FacilityEntity facility = new FacilityEntity(resultSet.getString(FacilityColumn.ID.getColumnName()),
					resultSet.getString(FacilityColumn.NAME.getColumnName()),
					resultSet.getString(FacilityColumn.ADDRESS.getColumnName()),
					resultSet.getString(FacilityColumn.PHONE.getColumnName()),
					resultSet.getString(FacilityColumn.CITY.getColumnName()), openingHours,
					resultSet.getDouble(FacilityColumn.LATITUDE.getColumnName()), 
					resultSet.getDouble(FacilityColumn.LONGITUDE.getColumnName()));
			return facility;
		} finally {
			if (resultSet.isAfterLast()) {
				resultSet.close();
			}
			if (openingHoursRS.isAfterLast()) {
				openingHoursRS.close();
			}
		}
	}
	
	public FacilityEntity queryByID(int id) throws SQLException, ClassNotFoundException, IOException {
		return (FacilityEntity) super.queryByID(FacilityColumn.TABLE_NAME, id);
	}
}
