package com.kms.cura_server.response;

import java.util.List;

import com.google.gson.JsonObject;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.UserEntity;

public abstract class APIResponse {

	public static String unsuccessResponse(String message) {
		JsonObject jsonError = new JsonObject();
		jsonError.addProperty(Entity.STATUS_KEY, false);
		jsonError.addProperty(Entity.MESSAGE, message);
		return jsonError.toString();
	}

	public abstract String successResponse(Entity entity);

	public abstract String successResponse(List<? extends Entity> entity);
}
