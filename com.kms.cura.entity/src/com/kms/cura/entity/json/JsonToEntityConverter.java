package com.kms.cura.entity.json;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.kms.cura.entity.Entity;

public class JsonToEntityConverter {
	private JsonToEntityConverter() {
		// hide constructor
	}
	
	public static <T extends Entity> T convertJsonStringToEntity(String jsonString, Type type) {
		return new Gson().fromJson(jsonString, type);
	}
	
	public static <T extends Entity> T convertJsonToEntity(JsonElement jsonElement, Type type) {
		return new Gson().fromJson(jsonElement, type);
	}
	
	public static <T extends Entity> List<T> convertJsonArrayToEntityList(JsonElement jsonElement, Type type) {
		return new Gson().fromJson(jsonElement, type);
	}


}
