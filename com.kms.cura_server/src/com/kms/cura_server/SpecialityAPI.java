package com.kms.cura_server;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.kms.cura.dal.SpecialityDAL;
import com.kms.cura.dal.database.SpecialityDatabaseHelper;
import com.kms.cura.entity.Entity;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.SpecialityAPIResponse;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.SpecialityAPIResponse;

@Path("/speciality")
public class SpecialityAPI {
	@GET
	@Path("/getAll")
	public String getAllDegree() {
		try {
			List<Entity> speciality = SpecialityDAL.getInstance().getAll(new SpecialityDatabaseHelper());
			return new SpecialityAPIResponse().successResponse(speciality);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	

	
}
