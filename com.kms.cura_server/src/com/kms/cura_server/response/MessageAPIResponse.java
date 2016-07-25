package com.kms.cura_server.response;

import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;

public class MessageAPIResponse extends APIResponse {
	
	@Override
	public String successResponse(Entity entity) {
		return null;
	}

	@Override
	public String successResponse(List<? extends Entity> entity) {
		return null;
	}

	public String successMessageResponse(List<MessageEntity> entity) {
		JsonObject jsonObject = new JsonObject();
		JsonArray array = new JsonArray();
		for (MessageEntity msg : entity) {
			JsonElement element = EntityToJsonConverter.convertEntityToJson(msg);
			JsonObject object = (JsonObject) element;
			if (msg.isSenderDoctor()){
				object.addProperty(MessageEntity.SENT_BY_DOCTOR, 1);
			}
			else{
				object.addProperty(MessageEntity.SENT_BY_DOCTOR, 0);
			}
			array.add(object);
		}
		jsonObject.add(MessageEntity.MESSAGE_LIST, array);
		jsonObject.addProperty(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}
	
	public String successUpdateMessageResponse() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}
}
