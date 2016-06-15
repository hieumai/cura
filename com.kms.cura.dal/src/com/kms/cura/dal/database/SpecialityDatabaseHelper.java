package com.kms.cura.dal.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.DegreeColumn;
import com.kms.cura.dal.mapping.Doctor_SpecialityColumn;
import com.kms.cura.dal.mapping.SpecialityColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SpecialityEntity;


public class SpecialityDatabaseHelper extends DatabaseHelper{

	public SpecialityDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected Entity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return new SpecialityEntity(resultSet.getString(DegreeColumn.ID.getColumnName()), resultSet.getString(DegreeColumn.NAME.getColumnName()));
	}

	public List<Entity> getSpecialitiesFromDoctorID(int id) throws SQLException, ClassNotFoundException{
		//select cura.specialties.name from (select cura.doctor_specialties.speciality_id from cura.doctor_specialties 
		//	where cura.doctor_specialties.doctor_id = 3) as T1 join cura.specialties on T1.speciality_id = cura.specialties.id;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM ");
		builder.append("( SELECT ");
		builder.append(Doctor_SpecialityColumn.TABLE_NAME+"."+Doctor_SpecialityColumn.SPECIALITY_ID);
		builder.append(" FROM "+Doctor_SpecialityColumn.TABLE_NAME);
		builder.append(" WHERE "+Doctor_SpecialityColumn.DOCTOR_ID);
		builder.append(" = ?) AS T1 JOIN ");
		builder.append(SpecialityColumn.TABLE_NAME + " AS T2 ON");
		builder.append(" T1."+SpecialityColumn.ID);
		builder.append(" = T2."+SpecialityColumn.ID);
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs != null) {
				List<Entity> specilities = new ArrayList<>();
				while(rs.next()){
					specilities.add(getEntityFromResultSet(rs));
					return specilities;
				}
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return null;

	}
}
