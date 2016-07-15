package com.kms.cura.entity;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

public class ImageEntity extends Entity {
	private String image, path, userId;

	public ImageEntity(String image, String path, String name, String id) {
		super(name, id);
		this.image = image;
		this.path = path;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static Type getImageEntityType() {
		return new TypeToken<ImageEntity>() {
		}.getType();
	}

}
