package com.kms.cura_server;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.kms.cura.dal.MessageDAL;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.MessageAPIResponse;

@Path("/message")
public class MessageAPI {
	@POST
	@Path("/getByPatient")
	public String getByPatient(String jsonData) {
		PatientUserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData, PatientUserEntity.getPatientUserType());
		try {
			List<MessageEntity> messageEntities = MessageDAL.getInstance().getMessageForPatient(entity);
			return new MessageAPIResponse().successMessageResponse(messageEntities);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/getByDoctor")
	public String getByDoctor(String jsonData) {
		DoctorUserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData, DoctorUserEntity.getDoctorEntityType());
		try {
			List<MessageEntity> messageEntities = MessageDAL.getInstance().getMessageForDoctor(entity);
			return new MessageAPIResponse().successMessageResponse(messageEntities);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/deletePatientMessage")
	public String deleteMessage(String jsonData) {
		MessageEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData, MessageEntity.class);
		try {
			MessageDAL.getInstance().deletePatientMessage(entity);
			return new MessageAPIResponse().successUpdateMessageResponse();
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/deleteDoctorMessage")
	public String deleteDoctorMessage(String jsonData) {
		MessageEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData, MessageEntity.class);
		try {
			MessageDAL.getInstance().deleteDoctorMessage(entity);
			return new MessageAPIResponse().successUpdateMessageResponse();
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
