package com.kms.cura_server.response;

import java.util.List;

import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.UserEntity;

public class UserAPIResponse extends APIResponse {

	@Override
	public String successResponse(Entity entity) {
		JsonElement jsonUser = EntityToJsonConverter.convertEntityToJson(entity);
		JsonObject jsonUserConvert = (JsonObject) jsonUser;
		jsonUserConvert.addProperty(Entity.STATUS_KEY, true);
		return jsonUserConvert.toString();
	}

	@Override
	public String successResponse(List<? extends Entity> entity) {
		return null;
	}

	public String successResponsewithType(UserEntity entity) {
		JsonElement jsonUser = EntityToJsonConverter.convertEntityToJson(entity);
		JsonObject jsonUserConvert = (JsonObject) jsonUser;
		jsonUserConvert.addProperty(Entity.STATUS_KEY, true);
		jsonUserConvert.addProperty(UserEntity.TYPE, entity.getType());
		return jsonUserConvert.toString();
	}
	
	public String successResponseOpeningHour(List<OpeningHour> list) {
		JsonObject jsonObject = new JsonObject();
		JsonElement jsonElement = EntityToJsonConverter.convertOpeningHourListToJson(list);
		jsonObject.add(OpeningHour.HOURS_LIST, jsonElement);
		jsonObject.addProperty(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}
}
