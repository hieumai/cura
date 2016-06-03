package com.kms.cura.entity.user;

import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.SpecialityEntity;
import java.sql.Date;
import java.util.List;

public class DoctorUserEntity extends UserEntity {
	private String phone;
	private DegreeEntity degree;
	private List<SpecialityEntity> speciality;
	private double rating;
	private int experience;
	private double minPrice;
	private double maxPrice;
	private List<FacilityEntity> facility;
	private String gender;
	private Date birth;
	private String location;
	private String insurance;

	public DoctorUserEntity(String id, String name, String email, String password, String phone, DegreeEntity degree,
			List<SpecialityEntity> speciality, double rating, int experience, double minPrice, double maxPrice,
			List<FacilityEntity> facility, String gender, Date birth, String location, String insurance) {
		super(id, name, email, password);
		this.phone = phone;
		this.degree = degree;
		this.speciality = speciality;
		this.rating = rating;
		this.experience = experience;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.facility = facility;
		this.gender = gender;
		this.birth = birth;
		this.location = location;
		this.insurance = insurance;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public DegreeEntity getDegree() {
		return degree;
	}

	public void setDegree(DegreeEntity degree) {
		this.degree = degree;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public List<SpecialityEntity> getSpeciality() {
		return speciality;
	}

	public void setSpeciality(List<SpecialityEntity> speciality) {
		this.speciality = speciality;
	}

	public List<FacilityEntity> getFacility() {
		return facility;
	}

	public void setFacility(List<FacilityEntity> facility) {
		this.facility = facility;
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

}
