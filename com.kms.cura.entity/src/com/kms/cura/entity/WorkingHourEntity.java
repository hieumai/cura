package com.kms.cura.entity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class WorkingHourEntity {
	private List<OpeningHour> workingTime;
	private FacilityEntity facilityEntity;
	
	
	public WorkingHourEntity(FacilityEntity facilityEntity){
		this.facilityEntity = facilityEntity;
		this.workingTime = new ArrayList<>();
	}
	
	
	
	public List<OpeningHour> getWorkingTime() {
		return workingTime;
	}

	public WorkingHourEntity(List<OpeningHour> workingTime, FacilityEntity facilityEntity) {
		this.workingTime = workingTime;
		this.facilityEntity = facilityEntity;
	}



	public void setWorkingTime(List<OpeningHour> workingTime) {
		this.workingTime = workingTime;
	}

	public FacilityEntity getFacilityEntity() {
		return facilityEntity;
	}

	public void setFacilityEntity(FacilityEntity facilityEntity) {
		this.facilityEntity = facilityEntity;
	}

	@Override
	public boolean equals(Object obj) {
		WorkingHourEntity entity = (WorkingHourEntity) obj;
		if (this.facilityEntity.equals(entity.facilityEntity)) {
			for (int i = 0; i < workingTime.size(); ++i) {
				if (!workingTime.get(i).equals(entity.getWorkingTime().get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
