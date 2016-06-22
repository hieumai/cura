package com.kms.cura_server.response;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.UserEntity;

public class FacilityAPIResponse extends APIResponse{
	
	
	@Override
	public String successResponse(Entity entity) {
		return null;
	}

	@Override
	public String successResponse(List<? extends Entity> entity) {
		JsonElement jsonElement = EntityToJsonConverter.convertEntityListToJson(entity);
		JsonObject object = new JsonObject();
		object.add(FacilityEntity.FACILITY_LIST, jsonElement);
		object.addProperty(Entity.STATUS_KEY, true);
		return object.toString();
	}

}
