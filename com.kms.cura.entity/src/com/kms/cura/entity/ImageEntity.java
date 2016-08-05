package com.kms.cura.entity;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

public class ImageEntity extends Entity {
	public static String IMAGE_FORMAT ="png";
	private String encodedImage, path, userId;

	public ImageEntity(String encodedImage, String path, String name, String id) {
		super(name, id);
		this.encodedImage = encodedImage;
		this.path = path;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEncodedImage() {
		return encodedImage;
	}

	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
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
