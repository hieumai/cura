package com.kms.cura.entity;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.user.DoctorUserEntity;

/**
 * Created by toanbnguyen on 6/10/2016.
 */
public class ConditionEntity extends Entity {
	private String description;
	public final static String CONDITION_LIST = "condition_list";

	public ConditionEntity(String id, String name, String description) {
		super(id, name);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static Type getConditionType() {
		Type type = new TypeToken<ConditionEntity>() {
		}.getType();
		return type;
	}

}
