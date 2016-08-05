package com.kms.cura_server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.kms.cura.dal.DegreeDAL;
import com.kms.cura.entity.Entity;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.DegreeAPIResponse;

@Path("/degree")
public class DegreeAPI {
	@GET
	@Path("/getAll")
	public String getAllDegree() {
		try {
			List<Entity> degree = DegreeDAL.getInstance().getAll();
			return new DegreeAPIResponse().successResponse(degree);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
