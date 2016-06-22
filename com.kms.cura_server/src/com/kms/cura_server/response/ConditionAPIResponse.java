package com.kms.cura_server.response;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.UserEntity;

public class ConditionAPIResponse extends APIResponse{

	@Override
	public String successResponse(Entity entity) {
		return null;
	}

	@Override
	public String successResponse(List<? extends Entity> entity) {
		JsonElement jsonElement = EntityToJsonConverter.convertEntityListToJson(entity);
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(ConditionEntity.CONDITION_LIST, jsonElement);
		jsonObject.addProperty(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}

}
