package com.kms.cura_server.response;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.dal.mapping.Doctor_FacilityColumn;
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

	public String successEdit() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}

	public String successResponseAllOpeningHour(HashMap<Integer, List<OpeningHour>> allWorkingHour){
		Set<Integer> facilityID = allWorkingHour.keySet();
		JsonObject jsonObject = new JsonObject();
		JsonArray array = new JsonArray();
		for(int id : facilityID){
			JsonElement jsonElement = EntityToJsonConverter.convertOpeningHourListToJson(allWorkingHour.get(id));
			JsonObject element = new JsonObject();
			element.add(OpeningHour.HOURS_LIST, jsonElement);
			element.addProperty(Doctor_FacilityColumn.FACILITY_ID.getColumnName(),id);
			array.add(element);
		}
		jsonObject.add(OpeningHour.LIST_HOURS_LIST, array);
		jsonObject.addProperty(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}
}
