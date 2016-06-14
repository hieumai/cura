package com.kms.cura_server;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.gson.JsonElement;
import com.kms.cura.dal.DegreeDAL;
import com.kms.cura.dal.InsuranceDAL;
import com.kms.cura.dal.database.InsuranceDatabaseHelper;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura_server.resources.Strings;

@Path("/insurance")
public class InsuranceAPI {
	@GET
	@Path("/getAll")
	public String getAllInsurance() {
		try {
			List<Entity> insurance = InsuranceDAL.getInstance().getAll(new InsuranceDatabaseHelper());
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(insurance);
			return element.toString();
		} catch (ClassNotFoundException | SQLException e) {
			return Strings.error_internal;
		}
	}
}
