package com.kms.cura_server;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kms.cura.dal.SpecialityDAL;
import com.kms.cura.dal.database.SpecialityDatabaseHelper;
import com.kms.cura.dal.mapping.DoctorColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura_server.resources.Strings;

@Path("/speciality")
public class SpecialityAPI {
	@GET
	@Path("/getAll")
	public String getAllDegree() {
		try {
			List<Entity> speciality = SpecialityDAL.getInstance().getAll(new SpecialityDatabaseHelper());
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(speciality);
			return element.toString();
		} catch (ClassNotFoundException | SQLException e) {
			return Strings.error_internal;
		}
	}
	@POST
	@Path("/getbyDoctorID");
	public String getbyDoctorID(String jsonData) {
		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			int id = jsonObject.getInt(DoctorColumn.USER_ID.getColumnName());
			List<Entity> speciality = SpecialityDAL.getInstance().getbyDoctorID(id);
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(speciality);
			return element.toString();
		} catch (ClassNotFoundException | SQLException e) {
			return Strings.error_internal;
		}
		
	}
}
