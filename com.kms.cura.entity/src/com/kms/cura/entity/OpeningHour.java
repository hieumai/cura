package com.kms.cura.entity;

import java.sql.Time;

public class OpeningHour {
	private DayOfTheWeek dayOfTheWeek;
    private Time openTime;
    private Time closeTime;
    
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
	
	 public String getTime() {
         StringBuilder builder = new StringBuilder();
         builder.append(getOpenTime().toString().substring(0, 5));
         builder.append("-");
         builder.append(getCloseTime().toString().substring(0, 5));
         return builder.toString();
     }
}
