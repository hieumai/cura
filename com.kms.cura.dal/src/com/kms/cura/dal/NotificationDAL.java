package com.kms.cura.dal;

import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.NotificationDatabaseHelper;
import com.kms.cura.entity.NotificationEntity;

public class NotificationDAL {
	private static NotificationDAL _instance;

	private NotificationDAL() {
		// TODO Auto-generated constructor stub
	}

	public static NotificationDAL getInstance() {
		if (_instance == null) {
			_instance = new NotificationDAL();
		}
		return _instance;
	}
	
	public List<NotificationEntity> getbyEntityType(String type) throws SQLException, ClassNotFoundException{
		NotificationDatabaseHelper databaseHelper = new NotificationDatabaseHelper();
		try{
			return databaseHelper.getByEntityType(type);
		}
		finally{
			databaseHelper.closeConnection();
		}
	}
	
	public void updateNotification(NotificationEntity entity) throws SQLException, ClassNotFoundException{
		NotificationDatabaseHelper databaseHelper = new NotificationDatabaseHelper();
		try{
			databaseHelper.updateNotification(entity.getId(), entity.getType());
		}
		finally{
			databaseHelper.closeConnection();
		}
	}
}
