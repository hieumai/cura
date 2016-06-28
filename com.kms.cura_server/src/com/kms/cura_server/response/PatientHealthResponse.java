package com.kms.cura_server.response;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;

public class PatientHealthResponse  extends APIResponse {
	@Override
	public String successResponse(Entity entity) {
		return null;
	}

	@Override
	public String successResponse(List<? extends Entity> entity) {
		JsonElement jsonElement = EntityToJsonConverter.convertEntityListToJson(entity);
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(HealthEntity.HEALTH_LIST, jsonElement);
		jsonObject.addProperty(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}
}
