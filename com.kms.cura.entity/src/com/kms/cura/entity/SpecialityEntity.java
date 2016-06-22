package com.kms.cura.entity;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.user.DoctorUserEntity;

public class SpecialityEntity extends Entity {
	public final static String SPECIALITY_LIST = "speciality_list";

	public SpecialityEntity(String id, String name) {
		super(id, name);
	}

	public static Type getSpecialityType() {
		Type type = new TypeToken<SpecialityEntity>() {
		}.getType();
		return type;
	}

}
