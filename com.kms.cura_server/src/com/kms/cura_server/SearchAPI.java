package com.kms.cura_server;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kms.cura.dal.user.DoctorUserDAL;
import com.kms.cura.entity.DoctorSearchEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura_server.resources.Strings;
import com.kms.cura_server.response.UserAPIResponse;

@Path("/search")
public class SearchAPI {
	@POST
	@Path("/searchDoctor")
	public String searchDoctor(String jsonData) {
		try {
			List<DoctorUserEntity> doctors = DoctorUserDAL.getInstance()
					.searchDoctorFunction((DoctorSearchEntity) JsonToEntityConverter.convertJsonStringToEntity(jsonData,
							getDoctorSearchEntityType()));
			return new UserAPIResponse().successResponse(doctors);
		} catch (ClassNotFoundException | SQLException e) {
			return Strings.error_internal + e.toString();
		}
	}

	private static Type getDoctorSearchEntityType() {
		return new TypeToken<DoctorSearchEntity>() {
		}.getType();
	}
}
