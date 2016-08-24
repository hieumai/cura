package com.kms.cura.entity;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;

public class AppointmentEntity extends Entity{
	public static final String UPDATE_TYPE = "update_type";
	public static final int PENDING_STT = 0;
	public static final int ACCEPTED_STT = 1;
	public static final int REJECT_STT = 2;
	public static final int PATIENT_CANCEL_STT = 3;
	public static final int DOCTOR_CANCEL_STT = 4;
	public static final int COMPLETED_STT = 5;
	public static final int INCOMPLETED_STT = 6;
	public static String PENDING = "PENDING";
	public static String REJECTED = "REJECTED";
	public static String CANCEL = "CANCELLED";
	public static String COMPLETED = "COMPLETED";
	public static String INCOMPLETE = "INCOMPLETE";
	public static String ACCEPTED = "ACCEPTED";
	public static String APPTS_LIST = "appts_list";
	public static final String APPT_TYPE = "appt";
	private PatientUserEntity patientUserEntity;
	private DoctorUserEntity doctorUserEntity;
	private FacilityEntity facilityEntity;
	private Date apptDay;
	private Time startTime;
	private Time endTime;
	private int status = -1;
	private String patientCmt;
	private String doctorCmt;

	public AppointmentEntity(String id, PatientUserEntity patientUserEntity, DoctorUserEntity doctorUserEntity,
			FacilityEntity facilityEntity, Date apptDay, Time startTime, Time endTime, int status, String patientCmt,
			String doctorCmt) {
		super(id, null);
		this.patientUserEntity = patientUserEntity;
		this.doctorUserEntity = doctorUserEntity;
		this.facilityEntity = facilityEntity;
		this.apptDay = apptDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.patientCmt = patientCmt;
		this.doctorCmt = doctorCmt;
	}

	public AppointmentEntity copy() {
		AppointmentEntity appointmentEntity = new AppointmentEntity(super.getId(), this.patientUserEntity,
				this.doctorUserEntity, this.facilityEntity, this.apptDay, this.startTime, this.endTime, this.status,
				this.patientCmt, this.doctorCmt);
		return appointmentEntity;
	}

	@Override
	public boolean equals(Object obj) {
		AppointmentEntity entity = (AppointmentEntity) obj;
		return (super.getId().equals(entity.getId()));
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	public String getId() {
		return super.getId();
	}

	public void setId(String id) {
		super.setId(id);
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

	public String getPatientCmt() {
		return patientCmt;
	}

	public void setPatientCmt(String patientCmt) {
		this.patientCmt = patientCmt;
	}

	public String getDoctorCmt() {
		return doctorCmt;
	}

	public void setDoctorCmt(String doctorCmt) {
		this.doctorCmt = doctorCmt;
	}

	public String getStatusName() {
		switch (status) {
		case 0:
			return PENDING;
		case 1:
			return ACCEPTED;
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

	private boolean isSameDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		return (c1.get(Calendar.ERA) == c2.get(Calendar.ERA) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
	}
}
