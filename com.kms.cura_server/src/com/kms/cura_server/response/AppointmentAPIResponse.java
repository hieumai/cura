package com.kms.cura_server.response;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.Entity;

public class AppointmentAPIResponse extends APIResponse{
	private static String APPTS_LIST = "appts_list";
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
	
	public String successListApptsResponse (List<AppointmentEntity> list){
		JsonElement jsonElement = new Gson().toJsonTree(list, AppointmentEntity.getAppointmentListType());
		JsonObject object = new JsonObject();
		object.add(APPTS_LIST, jsonElement);
		object.addProperty(Entity.STATUS_KEY, true);
		return object.toString();
	}
	
	public String successRatingResponse(AppointmentEntity entity) {
		JsonElement element = new Gson().toJsonTree(entity, AppointmentEntity.getAppointmentType());
		JsonObject object = (JsonObject) element;
		object.addProperty(Entity.STATUS_KEY, true);
		return object.toString();
	}

}
