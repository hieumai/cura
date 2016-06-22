package com.kms.cura.entity;

import java.lang.reflect.Type;
import java.sql.Time;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.user.PatientUserEntity;

public class OpeningHour {
	private DayOfTheWeek dayOfTheWeek;
    private Time openTime;
    private Time closeTime;
    public static String HOURS_LIST = "hours_list";
	public OpeningHour(DayOfTheWeek dayOfTheWeek, Time openTime, Time closeTime) {
		super();
		this.dayOfTheWeek = dayOfTheWeek;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}
	public DayOfTheWeek getDayOfTheWeek() {
		return dayOfTheWeek;
	}
	public void setDayOfTheWeek(DayOfTheWeek dayOfTheWeek) {
		this.dayOfTheWeek = dayOfTheWeek;
	}
	public Time getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Time openTime) {
		this.openTime = openTime;
	}
	public Time getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Time closeTime) {
		this.closeTime = closeTime;
	}
	public String toString(){
		return this.dayOfTheWeek +" " +this.openTime.toString() +"-"+this.closeTime.toString();
	}
	
	public static Type getPatientUserType() {
		Type type = new TypeToken<OpeningHour>() {
		}.getType();
		return type;
	}
}
