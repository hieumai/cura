package com.kms.cura.entity.user;

import com.kms.cura.entity.Entity;

public class UserEntity extends Entity {
	public static final String TYPE = "type";
	public static final int USER_TYPE = 0;
	private String email;
	private String password;

	public UserEntity(String id, String name, String email, String password) {
		super(id, name);
		this.email = email;
		this.password = password;
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

}
