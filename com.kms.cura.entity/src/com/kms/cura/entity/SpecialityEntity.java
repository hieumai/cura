package com.kms.cura.entity;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.user.PatientUserEntity;

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
	
	public static Type getSpecialityListType(){
		Type type = new TypeToken<List<SpecialityEntity>>() {
		}.getType();
		return type;
	}
}
