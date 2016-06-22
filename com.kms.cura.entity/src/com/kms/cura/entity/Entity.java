package com.kms.cura.entity;

public abstract class Entity {
	private String id;
	private String name;
	public static final String STATUS_KEY = "statuskey";
	public static final String MESSAGE= "message";
	public Entity() {
	}
	
	public Entity(String id, String name) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
