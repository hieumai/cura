package com.kms.cura.dal.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.DegreeColumn;
import com.kms.cura.dal.mapping.EntityColumn;
import com.kms.cura.dal.mapping.SpecialityColumn;
import com.kms.cura.dal.mapping.Speciality_ConditionColumn;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.SpecialityEntity;


public class SpecialityDatabaseHelper extends DatabaseHelper{

	public SpecialityDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected SpecialityEntity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return new SpecialityEntity(resultSet.getString(DegreeColumn.ID.getColumnName()), resultSet.getString(DegreeColumn.NAME.getColumnName()));
	}
	
	public SpecialityEntity queryByID(int id) throws SQLException, ClassNotFoundException, IOException {
		return (SpecialityEntity) queryByID(SpecialityColumn.TABLE_NAME, EntityColumn.ID.getColumnName(), id);
	}
	
	public List<SpecialityEntity> querySpecialityByCondition(ConditionEntity entity) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		List<SpecialityEntity> specialityEntities = new ArrayList<>();
		builder.append("Select * from ");
		builder.append(Speciality_ConditionColumn.TABLE_NAME);
		builder.append(" c left join ");
		builder.append(SpecialityColumn.TABLE_NAME);
		builder.append(" s on c.");
		builder.append(Speciality_ConditionColumn.SPECIALITY_ID);
		builder.append(" = s.");
		builder.append(SpecialityColumn.ID);
		builder.append(" where ");
		builder.append(Speciality_ConditionColumn.CONDITION_ID);
		builder.append(" = ");
		builder.append(entity.getId());
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				SpecialityEntity specialityEntity = getEntityFromResultSet(rs);
				specialityEntities.add(specialityEntity);
			}
			return specialityEntities;
		} finally {
			stmt.close();
		}
	}
}
