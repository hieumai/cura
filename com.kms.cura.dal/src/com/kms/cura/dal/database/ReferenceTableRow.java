package com.kms.cura.dal.database;

public class ReferenceTableRow {
	private String firstRefKey;
	private String firstValue;
	private String secondRefKey;
	private String secondValue;
	public ReferenceTableRow(String firstRefKey, String firstValue, String secondRefKey, String secondValue) {
		this.firstRefKey = firstRefKey;
		this.firstValue = firstValue;
		this.secondRefKey = secondRefKey;
		this.secondValue = secondValue;
	}
	public ReferenceTableRow(){
		
	}
	public String getFirstRefKey() {
		return firstRefKey;
	}
	public void setFirstRefKey(String firstRefKey) {
		this.firstRefKey = firstRefKey;
	}
	public String getFirstValue() {
		return firstValue;
	}
	public void setFirstValue(String firstValue) {
		this.firstValue = firstValue;
	}
	public String getSecondRefKey() {
		return secondRefKey;
	}
	public void setSecondRefKey(String secondRefKey) {
		this.secondRefKey = secondRefKey;
	}
	public String getSecondValue() {
		return secondValue;
	}
	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}
	
}