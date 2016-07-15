package com.kms.cura_server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.kms.cura.dal.ConditionDAL;
import com.kms.cura.dal.SymptomDAL;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.SymptonAPIResponse;

@Path("/symptom")
public class SymptomAPI {
	@GET
	@Path("/getAll")
	public String getAllSymptom() {
		try {
			List<Entity> symptom = SymptomDAL.getInstance().getAll();
			return new SymptonAPIResponse().successResponse(symptom);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/getAssociatedSymptom")
	public String getAssociatedSymptom(String jsonData) {
		ConditionEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				ConditionEntity.class);
		try {
			List<SymptomEntity> symptomEntities = SymptomDAL.getInstance().getAssociatedSymptom(entity);
			return new SymptonAPIResponse().successResponse(symptomEntities);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
