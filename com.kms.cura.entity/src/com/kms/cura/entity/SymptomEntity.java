package com.kms.cura.entity;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by toanbnguyen on 6/10/2016.
 */
public class SymptomEntity extends Entity {
	public final static String SYMPTOM_LIST = "symptom_list";

	public SymptomEntity(String id, String name) {
		super(id, name);
	}

	public static Type getSymptonType() {
		Type type = new TypeToken<SymptomEntity>() {
		}.getType();
		return type;
	}
}
