package com.kms.cura_server;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kms.cura.dal.database.DoctorUserDatabaseHelper;
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

@Path("/user")
public final class UserAPI {
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
			List<Entity> users = PatientUserDAL.getInstance().getAll(new PatientUserDatabaseHelper());
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(users);
			return element.toString();
		} catch (ClassNotFoundException | SQLException e) {
			return Strings.error_internal + e.toString();
		}
	}

	@GET
	@Path("/getAllDoctor")
	public String getAllDoctor() {
		try {
			List<Entity> users = DoctorUserDAL.getInstance().getAll(new DoctorUserDatabaseHelper());
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(users);
			return element.toString();
		} catch (ClassNotFoundException | SQLException e) {
			return Strings.error_internal + e.getMessage();
		}
	}

	@POST
	@Path("/createPatient")
	public String createPatient(String jsonData) throws ClassNotFoundException, SQLException {
		try {
			PatientUserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData, getPatientUserType());
			PatientUserEntity user = PatientUserDAL.getInstance().createUser(entity);
			return SuccessResponse(user);
		} catch (ClassNotFoundException | SQLException | DALException e) {
			return UnsuccessResponse(e);
		}
	}

	@POST
	@Path("/createDoctor")
	public String createDoctor(String jsonData) throws ClassNotFoundException, SQLException {
		try {
			DoctorUserEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData, getDoctorEntityType());
			DoctorUserEntity user = DoctorUserDAL.getInstance().createUser(entity);
			return SuccessResponse(user);
		} catch (ClassNotFoundException | SQLException | DALException e) {
			return UnsuccessResponse(e);

		}
	}

	private static Type getDoctorEntityType() {
		return new TypeToken<DoctorUserEntity>() {
		}.getType();
	}

	private Type getPatientUserType() {
		Type type = new TypeToken<PatientUserEntity>() {
		}.getType();
		return type;
	}
	private String SuccessResponse(UserEntity user){
		JsonElement JsonUser = EntityToJsonConverter.convertEntityToJson(user);
		JsonObject JsonUserConvert =(JsonObject) JsonUser;
		JsonUserConvert.addProperty(UserEntity.STATUS_KEY, true);
		return JsonUserConvert.toString();
	}
	private String UnsuccessResponse(Exception e){
		JsonObject jsonError=new JsonObject();
		jsonError.addProperty(UserEntity.STATUS_KEY, false);
		jsonError.addProperty(UserEntity.MESSAGE, e.toString());
		return jsonError.toString();
	}
}
