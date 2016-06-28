package com.kms.cura.entity;
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

}
