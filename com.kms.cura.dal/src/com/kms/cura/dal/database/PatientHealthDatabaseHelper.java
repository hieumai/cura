package com.kms.cura.dal.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kms.cura.dal.mapping.ConditionColumn;
import com.kms.cura.dal.mapping.PatientHealthColumn;
import com.kms.cura.dal.mapping.SymptomColumn;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.entity.user.PatientUserEntity;

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
		builder.append(" DESC");
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

	public void updatePatientHealth(PatientUserEntity entity) throws Exception {
		try {
			con.setAutoCommit(false);
			deleteAllPatientHealth(entity);
			insertAllPatientHealth(entity);
			con.commit();
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
	
	private void deleteAllPatientHealth(PatientUserEntity entity) throws Exception  {
		PreparedStatement stmtDelete = null;
		StringBuilder qDelete = new StringBuilder();
		qDelete.append("delete from ");
		qDelete.append(PatientHealthColumn.TABLE_NAME);
		qDelete.append(" where ");
		qDelete.append(PatientHealthColumn.PATIENT_ID);
		qDelete.append(" = ");
		qDelete.append(entity.getId());
		try {
			stmtDelete = con.prepareStatement(qDelete.toString());
			stmtDelete.executeUpdate();
		} finally {
			if (stmtDelete != null) {
				stmtDelete.close();
			}
		}
	}
	
	private void insertAllPatientHealth(PatientUserEntity entity) throws Exception {
		PreparedStatement stmtInsert = null;
		StringBuilder qInsert = new StringBuilder();
		qInsert.append("insert into ");
		qInsert.append(PatientHealthColumn.TABLE_NAME);
		qInsert.append(" values (?, ?, ?, ?, ?, ?);");
		try {
			stmtInsert = con.prepareStatement(qInsert.toString());
			stmtInsert.setInt(1, Integer.parseInt(entity.getId()));
			for (int i = 0; i < entity.getHealthEntities().size(); ++i) {
				HealthEntity healthEntity = entity.getHealthEntities().get(i);
				if (healthEntity.isCondition()) {
					stmtInsert.setInt(2, Integer.parseInt(healthEntity.getId()));
					stmtInsert.setNull(3, java.sql.Types.INTEGER);
				} else {
					stmtInsert.setNull(2, java.sql.Types.INTEGER);
					stmtInsert.setInt(3, Integer.parseInt(healthEntity.getId()));
				}
				stmtInsert.setDate(4, healthEntity.getStartDate());
				if (healthEntity.isPastHealth()) {
					stmtInsert.setDate(5, healthEntity.getEndDate());
				} else {
					stmtInsert.setNull(5, java.sql.Types.DATE);
				}
				stmtInsert.setString(6, healthEntity.getComment());
				if (stmtInsert.executeUpdate() == 0) {
					throw new Exception("Update patient's health failed");
				}
			}
		}  finally {
			if (stmtInsert != null) {
				stmtInsert.close();
			}
		}
	}

}
