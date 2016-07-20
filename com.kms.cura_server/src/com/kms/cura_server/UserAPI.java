package com.kms.cura_server;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.google.gson.JsonElement;
import com.kms.cura.dal.database.PatientUserDatabaseHelper;
import com.kms.cura.dal.database.UserDatabaseHelper;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.user.DoctorUserDAL;
import com.kms.cura.dal.user.PatientUserDAL;
import com.kms.cura.dal.user.UserDAL;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura_server.resources.Strings;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.UserAPIResponse;

@Path("/user")
public final class UserAPI {
	private static String LIST_OF_CHANGES = "list_of_changes";

	@GET
	@Path("/getAllUser")
	public String getAllUser() {
		try {
			List<Entity> users = UserDAL.getInstance().getAll(new UserDatabaseHelper());
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(users);
			return element.toString();
		} catch (ClassNotFoundException | SQLException e) {
			return Strings.error_internal + e.getMessage();
		}
	}

	@GET
	@Path("/getAllAdmin")
	public String getAllUserAdmin() {
		try {
			List<Entity> users = UserDAL.getInstance().getAll(new UserDatabaseHelper());
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(users);
			return element.toString();
		} catch (ClassNotFoundException | SQLException e) {
			return Strings.error_internal + e.getMessage();
		}
	}

	@GET
	@Path("/getAllPatient")
	public String getAllPatient() {
		try {
			List<Entity> users = PatientUserDAL.getInstance().getAll();
			return new UserAPIResponse().successResponsePatient(users);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}

	@GET
	@Path("/getAllDoctor")
	public String getAllDoctor() {
		try {
			List<Entity> users = DoctorUserDAL.getInstance().getAll();
			return new UserAPIResponse().successResponse(users);
		} catch (ClassNotFoundException | SQLException e) {
			return UserAPIResponse.unsuccessResponse(e.getMessage());
		}
	}

	@POST
	@Path("/createPatient")
	public String createPatient(String jsonData) throws ClassNotFoundException, SQLException {
		try {
			PatientUserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
					PatientUserEntity.getPatientUserType());
			PatientUserEntity user = PatientUserDAL.getInstance().createUser(entity);
			return new UserAPIResponse().successResponsewithType(user);
		} catch (ClassNotFoundException | SQLException | DALException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}

	@POST
	@Path("/createDoctor")
	public String createDoctor(String jsonData) throws ClassNotFoundException, SQLException {
		try {
			DoctorUserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
					DoctorUserEntity.getDoctorEntityType());
			DoctorUserEntity user = DoctorUserDAL.getInstance().createUser(entity);
			return new UserAPIResponse().successResponsewithType(user);
		} catch (ClassNotFoundException | SQLException | DALException e) {
			return APIResponse.unsuccessResponse(e.getMessage());

		}
	}

	@POST
	@Path("/userLogin")
	public String userLogin(String jsonData) {
		UserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				PatientUserEntity.getPatientUserType());
		try {
			PatientUserEntity patientUserEntity = PatientUserDAL.getInstance().searchPatient(entity);
			if (patientUserEntity != null) {
				return new UserAPIResponse().successResponsewithType(patientUserEntity);
			}
			DoctorUserEntity doctorUserEntity = DoctorUserDAL.getInstance().searchDoctor(entity);
			if (doctorUserEntity != null) {
				return new UserAPIResponse().successResponsewithType(doctorUserEntity);
			}
			return APIResponse.unsuccessResponse("Email and password combination does not exist");
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}

	}
	
	@POST
	@Path("/updateDoctor")
	public String updateDoctor(String jsonData) {
		DoctorUserEntity doctorUserEntity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				DoctorUserEntity.getDoctorEntityType());
		try {
			DoctorUserEntity newDoctor = DoctorUserDAL.getInstance().updateDoctor(doctorUserEntity);
			return new UserAPIResponse().successResponse(newDoctor);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/updatePatientHealth")
	public String updatePatientHealth(String jsonData) {
		PatientUserEntity patientUserEntity = JsonToEntityConverter.convertJsonStringToEntity(jsonData, PatientUserEntity.getPatientUserType());
		try {
			PatientUserEntity patient = PatientUserDAL.getInstance().updatePatientHealth(patientUserEntity);
			return new UserAPIResponse().successResponse(patient);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
