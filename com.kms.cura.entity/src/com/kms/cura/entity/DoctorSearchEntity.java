package com.kms.cura.entity;

import com.kms.cura.entity.user.DoctorUserEntity;

// This class will contains all the searching data for passing back and forth between the server and client
public class DoctorSearchEntity extends Entity {
	private DoctorUserEntity doctor;
	
	public DoctorSearchEntity(DoctorUserEntity doctor){
		this.doctor = doctor;
	}

	public DoctorUserEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorUserEntity doctor) {
		this.doctor = doctor;
	}
}
