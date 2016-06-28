package com.kms.cura.dal.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.ConditionColumn;
import com.kms.cura.dal.mapping.PatientHealthColumn;
import com.kms.cura.dal.mapping.SymptomColumn;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.SymptomEntity;

public class PatientHealthDatabaseHelper extends DatabaseHelper {
	// prevent ambiguous column name when join
	private static final String CONDITION_NAME = "condition_name";
	private static final String SYMPTOM_NAME = "symptom_name";

	public PatientHealthDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	@Override
	protected HealthEntity getEntityFromResultSet(ResultSet resultSet)
			throws SQLException, ClassNotFoundException {
		if (resultSet.getString(PatientHealthColumn.CONDITION_ID.getColumnName()) == null) {
			SymptomEntity issue = new SymptomEntity(resultSet.getString(PatientHealthColumn.SYMPTOM_ID.getColumnName()), 
					resultSet.getString(SYMPTOM_NAME));
			return new HealthEntity(resultSet.getDate(PatientHealthColumn.START_DATE.getColumnName()), 
					resultSet.getDate(PatientHealthColumn.END_DATE.getColumnName()), null, issue, 
					resultSet.getString(PatientHealthColumn.NOTE.getColumnName()));
		} else {
			ConditionEntity issue = new ConditionEntity(resultSet.getString(PatientHealthColumn.CONDITION_ID.getColumnName()), 
					resultSet.getString(CONDITION_NAME), resultSet.getString(ConditionColumn.DESCRIPTION.getColumnName()));
			return new HealthEntity(resultSet.getDate(PatientHealthColumn.START_DATE.getColumnName()), 
					resultSet.getDate(PatientHealthColumn.END_DATE.getColumnName()), issue, null, 
					resultSet.getString(PatientHealthColumn.NOTE.getColumnName()));
		}
		
	}

	public List<HealthEntity> queryHealthByPatientID(String id) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		List<HealthEntity> pHealthEntities = new ArrayList<>();
		// SELECT patient_id, condition_id, symptom_id, start_date, end_date, c.name as condition_name, s.name as symptom_name, description, note
		// FROM cura.patient_health p left join cura.conditions c on p.condition_id = c.id
		// 							left join cura.symptoms s on p.symptom_id = s.id
		// WHERE p.patient_id = 2
		// ORDER BY start_date
		builder.append("SELECT ");
		builder.append(PatientHealthColumn.PATIENT_ID);
		builder.append(", ");
		builder.append(PatientHealthColumn.CONDITION_ID);
		builder.append(", ");
		builder.append(PatientHealthColumn.SYMPTOM_ID);
		builder.append(", ");
		builder.append(PatientHealthColumn.START_DATE);
		builder.append(", ");
		builder.append(PatientHealthColumn.END_DATE);
		builder.append(", c.");
		builder.append(ConditionColumn.NAME);
		builder.append(" as ");
		builder.append(CONDITION_NAME);
		builder.append(", s.");
		builder.append(SymptomColumn.NAME);
		builder.append(" as ");
		builder.append(SYMPTOM_NAME);
		builder.append(", ");
		builder.append(ConditionColumn.DESCRIPTION);
		builder.append(", ");
		builder.append(PatientHealthColumn.NOTE);
		builder.append(" FROM ");
		builder.append(PatientHealthColumn.TABLE_NAME);
		builder.append(" p left join ");
		builder.append(ConditionColumn.TABLE_NAME);
		builder.append(" c on p.");
		builder.append(PatientHealthColumn.CONDITION_ID);
		builder.append(" = c.");
		builder.append(ConditionColumn.ID);
		builder.append(" left join ");
		builder.append(SymptomColumn.TABLE_NAME);
		builder.append(" s on p.");
		builder.append(PatientHealthColumn.SYMPTOM_ID);
		builder.append(" = s.");
		builder.append(SymptomColumn.ID);
		builder.append(" WHERE p.");
		builder.append(PatientHealthColumn.PATIENT_ID);
		builder.append(" = ");
		builder.append(id);
		builder.append(" ORDER BY ");
		builder.append(PatientHealthColumn.START_DATE);
		try {
			stmt = con.prepareStatement(builder.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				HealthEntity entity = getEntityFromResultSet(rs);
				pHealthEntities.add(entity);
			}
			return pHealthEntities;
		} finally {
			stmt.close();
		}
	}

}
