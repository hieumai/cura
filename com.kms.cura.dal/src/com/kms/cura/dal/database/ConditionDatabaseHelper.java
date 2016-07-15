package com.kms.cura.dal.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.ConditionColumn;
import com.kms.cura.dal.mapping.Condition_SymptomColumn;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.SymptomEntity;

public class ConditionDatabaseHelper extends DatabaseHelper {

	public ConditionDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
    }
    
    public List<Entity> queryAll() throws SQLException, ClassNotFoundException, IOException {
    	return super.queryAll(ConditionColumn.TABLE_NAME);
	}

	@Override
	protected ConditionEntity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
		return new ConditionEntity(resultSet.getString(ConditionColumn.ID.getColumnName()),
				resultSet.getString(ConditionColumn.NAME.getColumnName()),
				resultSet.getString(ConditionColumn.DESCRIPTION.getColumnName()));
	}

	public List<ConditionEntity> queryAssociatedCondition(SymptomEntity symptomEntity)
			throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		List<ConditionEntity> conditionEntities = new ArrayList<>();
		builder.append("Select * from ");
		builder.append(Condition_SymptomColumn.TABLE_NAME);
		builder.append(" s left join ");
		builder.append(ConditionColumn.TABLE_NAME);
		builder.append(" c on s.");
		builder.append(Condition_SymptomColumn.CONDITION_ID);
		builder.append(" = c.");
		builder.append(ConditionColumn.ID);
		builder.append(" where ");
		builder.append(Condition_SymptomColumn.SYMPTOM_ID);
		builder.append(" = ");
		builder.append(symptomEntity.getId());
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				ConditionEntity entity = getEntityFromResultSet(rs);
				conditionEntities.add(entity);
			}
			return conditionEntities;
		} finally {
			stmt.close();
		}

	}
}
