package com.kms.cura_server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.kms.cura.dal.SpecialityDAL;
import com.kms.cura.dal.SymptomDAL;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.SymptonAPIResponse;
import com.kms.cura_server.response.SpecialityAPIResponse;

@Path("/speciality")
public class SpecialityAPI {
	@GET
	@Path("/getAll")
	public String getAllDegree() {
		try {
			List<Entity> speciality = SpecialityDAL.getInstance().getAll();
			return new SpecialityAPIResponse().successResponse(speciality);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/getByCondition")
	public String getByCondition(String jsonData) {
		ConditionEntity entity = JsonToEntityConverter.convertJsonStringToEntity(jsonData,
				ConditionEntity.class);
		try {
			List<SpecialityEntity> specialityEntities = SpecialityDAL.getInstance().getByCondition(entity);
			return new SpecialityAPIResponse().successResponse(specialityEntities);
		} catch (ClassNotFoundException | SQLException e) {
			return APIResponse.unsuccessResponse(e.getMessage());
		}
	}
}
