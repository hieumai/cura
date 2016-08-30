package com.kms.cura_server.response;

import java.util.List;

import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONObject;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.NotificationEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kms.cura.dal.mapping.NotificationColumn;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.UserEntity;


public class NotificationAPIResponse extends APIResponse {

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
	
	public String success(String regID){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Entity.STATUS_KEY, true);
		jsonObject.put(NotificationEntity.REG_ID, regID);
		return jsonObject.toString();
	}

	public String success() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Entity.STATUS_KEY, true);
		return jsonObject.toString();
	}
	
	public String successNotiListResponse(List<NotificationEntity> list, String type) throws JsonProcessingException{
		JSONArray array = new JSONArray();
		for (int i=0; i<list.size(); ++i){
			array.put(manuallyParse(list.get(i)));
		}
		JSONObject object = new JSONObject();
		object.put(UserEntity.STATUS_KEY, true);
		object.put(NotificationEntity.NOTI_LIST, array);
		return object.toString();
	}
	
	private JSONObject manuallyParse(NotificationEntity entity){
		JSONObject object = new JSONObject();
		object.put(NotificationColumn.NOTI_ID.getColumnName(), entity.getId());
		JsonElement element = null;
		if (entity.isMsgType()){
			object.put(NotificationEntity.NOTI_TYPE, NotificationEntity.MSG_TYPE);
			element = EntityToJsonConverter.convertEntityToJson(entity.getRefEntity());
		}
		else{
			object.put(NotificationEntity.NOTI_TYPE, NotificationEntity.APPT_TYPE);
			element = new Gson().toJsonTree(entity.getRefEntity(), AppointmentEntity.getAppointmentType());
		}
		JSONObject ref = new JSONObject(element.toString());
		object.put(NotificationEntity.REF_ENTITY, ref);
		object.put(NotificationColumn.STATUS.getColumnName(), entity.isStatus());
		if (entity.getNotiType() != null){
			object.put(NotificationColumn.APPT_NOTI_TYPE.getColumnName(), entity.getNotiType());
		}
		return object;
	}
}
