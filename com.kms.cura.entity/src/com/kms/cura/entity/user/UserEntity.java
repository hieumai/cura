package com.kms.cura.entity.user;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.ImageEntity;

public class UserEntity extends Entity {
	public static final String TYPE = "type";
	public static final int USER_TYPE = 0;
	private String email;
	private String password;
	private ImageEntity imageEntity;

	public UserEntity(String id, String name, String email, String password) {
		super(id, name);
		this.email = email;
		this.password = password;
	}

	public UserEntity(String id, String name, String email, String password, String img) {
		super(id, name);
		this.email = email;
		this.password = password;
		this.imageEntity = new ImageEntity(img, null, null, null);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return this.getId() + "\n" + this.getName() + "\n" + this.email;
	}

	public int getType() {
		return USER_TYPE;
	}

	public ImageEntity getImage() {
		return imageEntity;
	}

	public void setImageEntity(ImageEntity image) {
		this.imageEntity = image;
	}

	public static Type getUserEntityType() {
		return new TypeToken<UserEntity>() {
		}.getType();
	}

}
