package com.kms.cura.entity;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class FacilityEntity extends Entity {
	public final static String FACILITY_LIST = "facility_list";
	private String address;
	private String phone;
	private String city;
	private List<OpeningHour> openingHours;

	public FacilityEntity(String id, String name, String address, String phone, String city,
			List<OpeningHour> openingHours) {
		super(id, name);
		this.address = address;
		this.phone = phone;
		this.city = city;
		this.openingHours = openingHours;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<OpeningHour> getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(List<OpeningHour> openingHours) {
		this.openingHours = openingHours;
	}

	public static Type getFacilityType() {
		Type type = new TypeToken<FacilityEntity>() {
		}.getType();
		return type;
	}
}
