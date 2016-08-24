package com.kms.cura_server;

import java.io.IOException;
import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.kms.cura.dal.AppointmentDAL;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.AppointmentAPIResponse;

@Path("/appt")
public final class AppointmentAPI {
	
	@POST
	@Path("/getBookAppts")
	public String getBookAppts(String jsonData){
		AppointSearchEntity appointSearchEntity = new Gson().fromJson(jsonData, AppointSearchEntity.getAppointmentSearchType());
		try {
			return new AppointmentAPIResponse().successListApptsResponse(AppointmentDAL.getInstance().getAppointment(appointSearchEntity,null,null));
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/createAppt")
	public String createAppts(String jsonData){
		AppointmentEntity appointmentEntity = new Gson().fromJson(jsonData, AppointmentEntity.getAppointmentType());
		try {
			return new AppointmentAPIResponse().successListApptsResponse(AppointmentDAL.getInstance().bookAppointment(appointmentEntity));
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/updateAppt")
	public String updateAppts(String jsonData){
		JSONObject jsonObject = new JSONObject(jsonData);
		boolean patient = jsonObject.getBoolean(AppointmentEntity.UPDATE_TYPE);
		AppointmentEntity appointmentEntity = new Gson().fromJson(jsonData, AppointmentEntity.getAppointmentType());
		try {
			return new AppointmentAPIResponse().successListApptsResponse(AppointmentDAL.getInstance().updateAppointment(appointmentEntity, patient));
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/rateAppt")
	public String rateAppts(String jsonData) {
		AppointmentEntity appointmentEntity = new Gson().fromJson(jsonData, AppointmentEntity.getAppointmentType());
		try {
			appointmentEntity = AppointmentDAL.getInstance().rateAppointment(appointmentEntity);
			return new AppointmentAPIResponse().successRatingResponse(appointmentEntity);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
