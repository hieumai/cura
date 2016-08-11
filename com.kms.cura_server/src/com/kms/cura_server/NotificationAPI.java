package com.kms.cura_server;

import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.kms.cura.dal.NotificationDAL;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.NotificationAPIResponse;

@Path("/noti") 
public final class NotificationAPI {
	
	@POST 
	@Path("/getNotiByType")
	public String getNotiByType(String jsonData){
		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			String type = jsonObject.getString(NotificationEntity.NOTI_TYPE);
			return new NotificationAPIResponse().successNotiListResponse(NotificationDAL.getInstance().getbyEntityType(type));
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST 
	@Path("/updateNoti")
	public String updateNotification(String jsonData){
		try {
			NotificationEntity entity = new Gson().fromJson(jsonData, NotificationEntity.getNotificationType());
			NotificationDAL.getInstance().updateNotification(entity);
			return new NotificationAPIResponse().success();
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
}
