package com.kms.cura_server;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.gson.JsonElement;
import com.kms.cura.dal.ConditionDAL;
import com.kms.cura.dal.database.ConditionDatabaseHelper;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura_server.resources.Strings;

@Path("/condition")
public class ConditionAPI {
	@GET
	@Path("/getAll")
	public String getAllCondition() {
		try {
			List<Entity> condition = ConditionDAL.getInstance().getAll(new ConditionDatabaseHelper());
			JsonElement element = EntityToJsonConverter.convertEntityListToJson(condition);
			return element.toString();
		} catch (ClassNotFoundException | SQLException e) {
			return Strings.error_internal;
		}
	}
}
