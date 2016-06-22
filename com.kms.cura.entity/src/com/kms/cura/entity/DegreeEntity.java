package com.kms.cura.entity;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.user.DoctorUserEntity;

public class DegreeEntity extends Entity {
	public final static String DEGREE_LIST = "degree_list";

	public DegreeEntity(String id, String name) {
		super(id, name);
	}

	public static Type getDegreeType() {
		Type type = new TypeToken<DegreeEntity>() {
		}.getType();
		return type;
	}
}
