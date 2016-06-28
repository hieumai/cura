package com.kms.cura_server.response;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
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
		JsonElement jsonElement = EntityToJsonConverter.convertEntityListToJson(entity);
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(DoctorUserEntity.DOCTOR_LIST, jsonElement);
		jsonObject.addProperty(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}

	public String successResponsewithType(UserEntity entity) {
		JsonElement jsonUser = EntityToJsonConverter.convertEntityToJson(entity);
		JsonObject jsonUserConvert = (JsonObject) jsonUser;
		jsonUserConvert.addProperty(Entity.STATUS_KEY, true);
		jsonUserConvert.addProperty(UserEntity.TYPE, entity.getType());
		return jsonUserConvert.toString();
	}
}
