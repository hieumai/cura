package com.kms.cura_server;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.gson.JsonElement;
import com.kms.cura.dal.DegreeDAL;
import com.kms.cura.dal.database.DegreeDatabaseHelper;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura_server.resources.Strings;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.DegreeAPIResponse;

@Path("/degree")
public class DegreeAPI {
	@GET
	@Path("/getAll")
	public String getAllDegree() {
		try {
			List<Entity> degree = DegreeDAL.getInstance().getAll(new DegreeDatabaseHelper());
			return new DegreeAPIResponse().successResponse(degree);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
