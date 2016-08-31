package com.kms.cura_server;

import java.io.IOException;
import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.kms.cura.dal.NotificationDAL;
import com.kms.cura.dal.mapping.NotificationColumn;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.NotificationAPIResponse;


import com.kms.cura.dal.database.NotificationHelper;

@Path("/noti")
public final class NotificationAPI {

	@POST
	@Path("/registerNoti")
	public String registerNoti(String jsonData){
		JSONObject jsonObject = new JSONObject(jsonData);
		String regID = jsonObject.getString(NotificationEntity.REG_ID);
		String regName = jsonObject.getString(NotificationEntity.REG_NAME);
		NotificationHelper helper = new NotificationHelper();
		try {
			helper.registerID(regName, regID);
			return new NotificationAPIResponse().success(regID);
		} catch (IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/unregisterNoti")
	public String unRegisterNoti(String jsonData){
		JSONObject jsonObject = new JSONObject(jsonData);
		String regName = jsonObject.getString(NotificationEntity.REG_NAME);
		NotificationHelper helper = new NotificationHelper();
		try {
			helper.unRegisterID(regName);
			return new NotificationAPIResponse().success();
		} catch (IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST 
	@Path("/getNotiByType")
	public String getNotiByType(String jsonData) throws IOException{
		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			String type = jsonObject.getString(NotificationEntity.NOTI_TYPE);
			String userID = jsonObject.getString(NotificationEntity.USER_ID);
			return new NotificationAPIResponse().successNotiListResponse(NotificationDAL.getInstance().getbyEntityType(userID, type), type);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST 
	@Path("/updateNoti")
	public String updateNotification(String jsonData){
		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			String type = jsonObject.getString(NotificationEntity.NOTI_TYPE);
			NotificationEntity entity = null;
			if (type.equals(NotificationEntity.MSG_TYPE)){
				entity = new NotificationEntity(jsonObject.getString(NotificationColumn.NOTI_ID.getColumnName()), new Gson().fromJson(jsonObject.getJSONObject(NotificationEntity.REF_ENTITY).toString(), MessageEntity.class), 
						jsonObject.getBoolean(NotificationColumn.STATUS.getColumnName()));
			}
			else{
				entity = new NotificationEntity(jsonObject.getString(NotificationColumn.NOTI_ID.getColumnName()), new Gson().fromJson(jsonObject.getJSONObject(NotificationEntity.REF_ENTITY).toString(), AppointmentEntity.class), 
						jsonObject.getBoolean(NotificationColumn.STATUS.getColumnName()), null);
			}
			NotificationDAL.getInstance().updateNotification(entity);
			return new NotificationAPIResponse().success();
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
}
