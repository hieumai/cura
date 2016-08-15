package com.kms.cura_server.response;

import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.NotificationEntity;

public class NotificationAPIResponse extends APIResponse {
	
	@Override
	public String successResponse(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String successResponse(List<? extends Entity> entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public String success() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}
	
	public String successNotiListResponse(List<NotificationEntity> list){
		JsonElement jsonElement = new Gson().toJsonTree(list, NotificationEntity.getNotificationListType());
		JsonObject object = new JsonObject();
		object.add(NotificationEntity.NOTI_LIST, jsonElement);
		object.addProperty(Entity.STATUS_KEY, true);
		return object.toString();
	}
}
