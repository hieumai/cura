package com.kms.cura_server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.dal.database.UserDatabaseHelper;
import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.mapping.PasswordCodeColumn;
import com.kms.cura.dal.user.DoctorUserDAL;
import com.kms.cura.dal.user.PatientUserDAL;
import com.kms.cura.dal.user.UserDAL;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.NotificationEntity;
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
			List<Entity> users = UserDAL.getInstance().getAll();
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(users);
			return element.toString();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return Strings.error_internal + e.getMessage();
		}
	}

	@GET
	@Path("/getAllAdmin")
	public String getAllUserAdmin() {
		try {
			List<Entity> users = UserDAL.getInstance().getAll();
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(users);
			return element.toString();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return Strings.error_internal + e.getMessage();
		}
	}

	@GET
	@Path("/getAllPatient")
	public String getAllPatient() {
		try {
			List<Entity> users = PatientUserDAL.getInstance().getAll();
			return new UserAPIResponse().successResponsePatient(users);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}

	@GET
	@Path("/getAllDoctor")
	public String getAllDoctor() {
		try {
			List<Entity> users = DoctorUserDAL.getInstance().getAll();
			return new UserAPIResponse().successResponse(users);
		} catch (ClassNotFoundException | SQLException | IOException e) {
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
		} catch (ClassNotFoundException | SQLException | DALException | IOException e) {
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
		} catch (ClassNotFoundException | SQLException | DALException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());

		}
	}

	@POST
	@Path("/userLogin")
	public String userLogin(String jsonData) {
		UserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				UserEntity.getUserEntityType());
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
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}

	@POST
	@Path("/updatePatientHealth")
	public String updatePatientHealth(String jsonData) {
		PatientUserEntity patientUserEntity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				PatientUserEntity.getPatientUserType());
		try {
			PatientUserEntity patient = PatientUserDAL.getInstance().updatePatientHealth(patientUserEntity);
			return new UserAPIResponse().successResponse(patient);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/getDoctorByFacility")
	public String getDoctorByFacility(String jsonData) {
		FacilityEntity facilityEntity = JsonToEntityConverter.convertJsonStringToEntity(jsonData, FacilityEntity.getFacilityType());
		try {
			List<DoctorUserEntity> doctorUserEntities = DoctorUserDAL.getInstance().getDoctorByFacility(facilityEntity);
			return new UserAPIResponse().successResponse(doctorUserEntities);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}

	@POST
	@Path("/updatePhoto")
	public String updatePhoto(String jsonData) {
		UserEntity user = JsonToEntityConverter.convertJsonStringToEntity(jsonData, UserEntity.getUserEntityType());
		try {
			UserDAL.getInstance().updatePhoto(user);
			return getUserWithType(user);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}

	@POST
	@Path("/updatePatient")
	public String updatePatient(String jsonData) {
		PatientUserEntity patient = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				PatientUserEntity.getPatientUserType());
		try {
			PatientUserEntity result = PatientUserDAL.getInstance().updatePatient(patient);
			return new UserAPIResponse().successResponsewithType(result);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}

	@POST
	@Path("/updatePassword")
	public String updatePassword(String jsonData) {
		UserEntity user = JsonToEntityConverter.convertJsonStringToEntity(jsonData, UserEntity.getUserEntityType());
		try {
			UserDAL.getInstance().updatePassword(user);
			return getUserWithType(user);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	protected String getUserWithType(UserEntity user) throws ClassNotFoundException, SQLException, IOException {
		PatientUserEntity patientUserEntity = PatientUserDAL.getInstance().searchPatient(user);
		if (patientUserEntity != null) {
			return new UserAPIResponse().successResponsewithType(patientUserEntity);
		}
		DoctorUserEntity doctorUserEntity = DoctorUserDAL.getInstance().searchDoctor(user);
		if (doctorUserEntity != null) {
			return new UserAPIResponse().successResponsewithType(doctorUserEntity);
		}
		return APIResponse.unsuccessResponse("Can not retrive user information");

	}
	
	@POST
	@Path("/checkEmailExist")
	public String checkEmailExist(String jsonData) {
		JSONObject jsonObject = new JSONObject(jsonData);
		String email = jsonObject.getString(UserEntity.EMAIL);
		try {
			return new UserAPIResponse().successResponseWithBoolean(UserDAL.getInstance().checkEmailExist(email), UserEntity.EMAIL);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/sendCode")
	public String sendCode(String jsonData) {
		JSONObject jsonObject = new JSONObject(jsonData);
		String email = jsonObject.getString(UserEntity.EMAIL);
		try {
			String userID = UserDAL.getInstance().sendCode(email);
			return new UserAPIResponse().successResponseWithString(userID, UserEntity.STRING_RESPONSE);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/checkCode")
	public String checkCode(String jsonData) {
		JSONObject jsonObject = new JSONObject(jsonData);
		String userID = jsonObject.getString(UserEntity.ID);
		String code = jsonObject.getString(UserEntity.CODE);
		try {
			return new UserAPIResponse().successResponseWithString (UserDAL.getInstance().checkCode(userID, code), UserEntity.STRING_RESPONSE);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/updateDoctorBasic")
	public String updateDoctorBasic(String jsonData) {
		DoctorUserEntity doctorUserEntity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				DoctorUserEntity.getDoctorEntityType());
		try {
			DoctorUserEntity newDoctor = DoctorUserDAL.getInstance().updateDoctorBasic(doctorUserEntity);
			return new UserAPIResponse().successResponse(newDoctor);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/updateDoctorProfessional")
	public String updateDoctorProfessional(String jsonData) {
		DoctorUserEntity doctorUserEntity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				DoctorUserEntity.getDoctorEntityType());
		try {
			DoctorUserEntity newDoctor = DoctorUserDAL.getInstance().updateDoctorProfessional(doctorUserEntity);
			return new UserAPIResponse().successResponse(newDoctor);
		} catch (Exception e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
