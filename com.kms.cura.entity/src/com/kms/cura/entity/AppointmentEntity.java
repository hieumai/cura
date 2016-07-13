package com.kms.cura.entity;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;

public class AppointmentEntity {
	public static int PENDING_STT = 0;
	public static int ACCEPTED_STT = 1;
	public static int REJECT_STT = 2;
	public static int PATIENT_CANCEL_STT = 3;
	public static int DOCTOR_CANCEL_STT = 4;
	public static int COMPLETED_STT = 5;
	public static int INCOMPLETED_STT = 6;
	public static String PENDING = "PENDING";
	public static String REJECTED = "REJECTED";
	public static String CANCEL = "CANCELLED";
	public static String COMPLETED = "COMPLETED";
	public static String INCOMPLETE = "INCOMPLETE";
	
	private PatientUserEntity patientUserEntity;
	private DoctorUserEntity doctorUserEntity;
	private FacilityEntity facilityEntity;
	private Date apptDay;
	private Time startTime;
	private Time endTime;
	private int status = -1;
	
	public AppointmentEntity(PatientUserEntity patientUserEntity, DoctorUserEntity doctorUserEntity, FacilityEntity facilityEntity, Date apptDay,
			Time startTime, Time endTime, int status) {
		this.patientUserEntity = patientUserEntity;
		this.doctorUserEntity = doctorUserEntity;
		this.facilityEntity = facilityEntity;
		this.apptDay = apptDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
	}

	public PatientUserEntity getPatientUserEntity() {
		return patientUserEntity;
	}

	public void setPatientUserEntity(PatientUserEntity patientUserEntity) {
		this.patientUserEntity = patientUserEntity;
	}

	public DoctorUserEntity getDoctorUserEntity() {
		return doctorUserEntity;
	}

	public void setDoctorUserEntity(DoctorUserEntity doctorUserEntity) {
		this.doctorUserEntity = doctorUserEntity;
	}

	public Date getApptDay() {
		return apptDay;
	}

	public void setApptDay(Date apptDay) {
		this.apptDay = apptDay;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public FacilityEntity getFacilityEntity() {
		return facilityEntity;
	}

	public void setFacilityEntity(FacilityEntity facilityEntity) {
		this.facilityEntity = facilityEntity;
	}

	public static Type getAppointmentType() {
        return new TypeToken<AppointmentEntity>() {
        }.getType();
    }

	public static Type getAppointmentListType() {
		 return new TypeToken<List<AppointmentEntity>>() {
	        }.getType();
	}

	public String getStatusName(){
		switch (status){
			case 0:
				return PENDING;
			case 2:
				return REJECTED;
			case 3:
				return CANCEL;
			case 4:
				return CANCEL;
			case 5:
				return COMPLETED;
			case 6:
				return INCOMPLETE;
		}
		return null;
	}
}
