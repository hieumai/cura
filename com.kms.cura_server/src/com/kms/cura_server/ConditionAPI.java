package com.kms.cura_server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.kms.cura.dal.ConditionDAL;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.ConditionAPIResponse;
import com.kms.cura_server.response.SymptonAPIResponse;

@Path("/condition")
public class ConditionAPI {
	@GET
	@Path("/getAll")
	public String getAllCondition() {
		try {
			List<Entity> condition = ConditionDAL.getInstance().getAll();
			return new ConditionAPIResponse().successResponse(condition);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/getAssociatedCondition")
	public String getAssociatedCondition(String jsonData) {
		SymptomEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				SymptomEntity.class);
		try {
			List<ConditionEntity> conditionEntities = ConditionDAL.getInstance().getAssociatedCondition(entity);
			return new ConditionAPIResponse().successResponse(conditionEntities);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
