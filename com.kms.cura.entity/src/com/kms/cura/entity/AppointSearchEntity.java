package com.kms.cura.entity;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class AppointSearchEntity {
	private AppointmentEntity appointmentEntity;

	public AppointSearchEntity(AppointmentEntity appointmentEntity) {
		this.appointmentEntity = appointmentEntity;
	}

	public AppointmentEntity getAppointmentEntity() {
		return appointmentEntity;
	}

	public void setAppointmentEntity(AppointmentEntity appointmentEntity) {
		this.appointmentEntity = appointmentEntity;
	}

	public static Type getAppointmentSearchType() {
		return new TypeToken<AppointSearchEntity>() {
		}.getType();
	}
}
