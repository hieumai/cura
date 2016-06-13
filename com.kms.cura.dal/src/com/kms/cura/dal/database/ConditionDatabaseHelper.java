package com.kms.cura.dal.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.kms.cura.dal.mapping.ConditionColumn;
import com.kms.cura.dal.mapping.SymptomColumn;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SymptomEntity;

public class ConditionDatabaseHelper extends DatabaseHelper {

    public ConditionDatabaseHelper() throws ClassNotFoundException, SQLException {
        super();
    }

    @Override
    protected Entity getEntityFromResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        return new ConditionEntity(resultSet.getString(ConditionColumn.ID.getColumnName()),
                resultSet.getString(ConditionColumn.NAME.getColumnName()),
                resultSet.getString(ConditionColumn.DESCRIPTION.getColumnName()));
    }
}
