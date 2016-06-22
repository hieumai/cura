package com.kms.cura_server;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.gson.JsonElement;
import com.kms.cura.dal.FacilityDAL;
import com.kms.cura.dal.database.FacilityDatabaseHelper;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura_server.resources.Strings;
import com.kms.cura_server.response.APIResponse;
import com.kms.cura_server.response.FacilityAPIResponse;

@Path("/facility")
public class FacilityAPI {
    @GET
    @Path("/getAll")
    public String getAllFacility() {
        try {
            List<Entity> facilities = FacilityDAL.getInstance().getAll(new FacilityDatabaseHelper());
            return new FacilityAPIResponse().successResponse(facilities);
        } catch (ClassNotFoundException | SQLException e) {
            return APIResponse.unsuccessResponse(e.getMessage());
        }
    }
}
