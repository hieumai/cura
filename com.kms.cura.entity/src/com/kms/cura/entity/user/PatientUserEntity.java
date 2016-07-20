package com.kms.cura.entity.user;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.HealthEntity;

public class PatientUserEntity extends UserEntity {
	public static int PATIENT_TYPE = 2;
	public final static String PATIENT_LIST = "patient_list";
	public final static String GENDER_MALE = "M";
	private String gender;
	private Date birth;
	private String location;
	private String insurance;
	private String healthConcern;
	private List<HealthEntity> healthEntities;
	private List<AppointmentEntity> appointmentList = new ArrayList<>();

	public PatientUserEntity(UserEntity entity) {
		super(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword());
	}

	public PatientUserEntity(String id, String name, String email, String password, String gender, Date birth,
			String location, String insurance, String healthConcern) {
		super(id, name, email, password);
		this.gender = gender;
		this.birth = birth;
		this.location = location;
		this.insurance = insurance;
		this.healthConcern = healthConcern;
	}

	public PatientUserEntity(String id, String name, String email, String password, String gender, Date birth,
							 String location, String insurance, String healthConcern, List<HealthEntity> healthEntities, List<AppointmentEntity> appointmentList) {
		super(id, name, email, password);
		this.gender = gender;
		this.birth = birth;
		this.location = location;
		this.insurance = insurance;
		this.healthConcern = healthConcern;
		this.healthEntities = healthEntities;
		this.appointmentList = appointmentList;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getHealthConcern() {
		return healthConcern;
	}

	public void setHealthConcern(String healthConcern) {
		this.healthConcern = healthConcern;
	}

	public List<HealthEntity> getHealthEntities() {
		return healthEntities;
	}

	public void setHealthEntities(List<HealthEntity> healthEntities) {
		this.healthEntities = healthEntities;
	}

	public void addHealthEntity(HealthEntity entity) {
		healthEntities.add(entity);
	}
	
	public List<AppointmentEntity> getAppointmentList() {
		return appointmentList;
	}

	public void setAppointmentList(List<AppointmentEntity> appointmentList) {
		this.appointmentList = appointmentList;
	}
	
	public void addAllAppointmentList(List<AppointmentEntity> appointmentList){
		this.appointmentList.addAll(appointmentList);
	}

	@Override
	public int getType() {
		return PATIENT_TYPE;
	}

	public static Type getPatientUserType() {
		Type type = new TypeToken<PatientUserEntity>() {
		}.getType();
		return type;
	}

}
