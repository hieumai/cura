package com.kms.cura.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.NotificationDatabaseHelper;
import com.kms.cura.dal.mapping.NotificationType;
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
	
	public List<NotificationEntity> getbyEntityType(String userID, String type) throws SQLException, ClassNotFoundException, IOException{
		NotificationDatabaseHelper databaseHelper = new NotificationDatabaseHelper();
		try{
			return databaseHelper.getByEntityType(userID, type);
		}
		finally{
			databaseHelper.closeConnection();
		}
	}
	
	public void updateNotification(NotificationEntity entity) throws SQLException, ClassNotFoundException{
		NotificationDatabaseHelper databaseHelper = new NotificationDatabaseHelper();
		try{
			String type = null;
			if (entity.isMsgType()){
				type = NotificationType.MSG_TYPE.getNotiType();
			}
			else{
				type = NotificationType.APPT_TYPE.getNotiType();
			}
			databaseHelper.updateNotification(entity.getId(), type);
		}
		finally{
			databaseHelper.closeConnection();
		}
	}
}
