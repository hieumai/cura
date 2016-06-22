package com.kms.cura_server;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.gson.JsonElement;
import com.kms.cura.dal.SymptomDAL;
import com.kms.cura.dal.database.SymptomDatabaseHelper;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura_server.resources.Strings;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.SymptonAPIResponse;

@Path("/symptom")
public class SymptomAPI {
	@GET
	@Path("/getAll")
	public String getAllSymptom() {
		try {
			List<Entity> symptom = SymptomDAL.getInstance().getAll(new SymptomDatabaseHelper());
			return new SymptonAPIResponse().successResponse(symptom);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
