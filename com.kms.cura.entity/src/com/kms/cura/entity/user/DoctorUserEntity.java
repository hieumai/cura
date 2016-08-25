package com.kms.cura.entity.user;

import com.google.gson.reflect.TypeToken;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.WorkingHourEntity;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DoctorUserEntity extends UserEntity {
    public final static String DOCTOR_LIST = "doctor_list";
    public static int DOCTOR_TYPE = 1;
    public final static String GENDER_MALE = "M";
    private String phone;
    private DegreeEntity degree;
    private List<SpecialityEntity> speciality;
    private double rating;
    private int experience;
    private double minPrice;
    private double maxPrice;
    private List<WorkingHourEntity> workingTime;
    private String gender;
    private Date birth;
    private String insurance;
    private List<AppointmentEntity> appointmentList = new ArrayList<>();
    public static String MALE = "M";

    public DoctorUserEntity(String id, String name) {
        super(id, name, null, null);
    }

    public DoctorUserEntity(String id, String name, String email, String password, String phone, DegreeEntity degree,
                            List<SpecialityEntity> speciality, double rating, int experience, double minPrice, double maxPrice,
                            List<WorkingHourEntity> workingTime, String gender, Date birth, String insurance) {
        super(id, name, email, password);
        this.phone = phone;
        this.degree = degree;
        this.speciality = speciality;
        this.rating = rating;
        this.experience = experience;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.workingTime = workingTime;
        this.gender = gender;
        this.birth = birth;
        this.insurance = insurance;
    }

    public DoctorUserEntity(String id, String name, String email, String password, String phone, DegreeEntity degree,
                            List<SpecialityEntity> speciality, List<WorkingHourEntity> workingTime, String gender, Date birth) {
        super(id, name, email, password);
        this.phone = phone;
        this.degree = degree;
        this.speciality = speciality;
        this.workingTime = workingTime;
        this.gender = gender;
        this.birth = birth;
    }

	
	public DoctorUserEntity(String id, String name, String email, String password, String phone, DegreeEntity degree,
			List<SpecialityEntity> speciality, double rating, int experience, double minPrice, double maxPrice,
			List<WorkingHourEntity> workingTime, String gender, Date birth, String insurance,String path) {
		super(id, name, email, password,path);
		this.phone = phone;
		this.degree = degree;
		this.speciality = speciality;
		this.rating = rating;
		this.experience = experience;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.workingTime = workingTime;
		this.gender = gender;
		this.birth = birth;
		this.insurance = insurance;
	}

	public DoctorUserEntity(String id, String name, String email, String password, String phone, DegreeEntity degree,
			List<SpecialityEntity> speciality, List<WorkingHourEntity> workingTime, String gender, Date birth,
			String encodedImage) {
		super(id, name, email, password, encodedImage);
		this.phone = phone;
		this.degree = degree;
		this.speciality = speciality;
		this.workingTime = workingTime;
		this.gender = gender;
		this.birth = birth;
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

	public List<WorkingHourEntity> getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(List<WorkingHourEntity> workingTime) {
		this.workingTime = workingTime;
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

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

    public List<AppointmentEntity> getAppointmentList() {
        return appointmentList;
    }

	public void setAppointmentList(List<AppointmentEntity> appointmentList) {
		this.appointmentList = appointmentList;
	}

	public void addAllAppointmentList(List<AppointmentEntity> appointmentList) {
		this.appointmentList.addAll(appointmentList);
	}

	public List<FacilityEntity> getFacilities() {
		List<FacilityEntity> result = new ArrayList<FacilityEntity>();
		for (WorkingHourEntity workingHourEntity : this.workingTime) {
			result.add(workingHourEntity.getFacilityEntity());
		}
		return result;
	}

	@Override
	public int getType() {
		return DOCTOR_TYPE;
	}

	public static Type getDoctorEntityType() {
		return new TypeToken<DoctorUserEntity>() {
		}.getType();
	}

	public String getPriceRange() {
		StringBuilder builder = new StringBuilder();
		builder.append(getMinPrice());
		builder.append("-");
		builder.append(getMaxPrice());
		return builder.toString();
	}

	public static Type getDoctorEntityListType() {
		return new TypeToken<Collection<DoctorUserEntity>>() {
		}.getType();
	}
    public boolean workOn(DayOfTheWeek dayOfTheWeek) {
        for (WorkingHourEntity hour : this.getWorkingTime()) {
            for (OpeningHour openingHour : hour.getWorkingTime()) {
                if (openingHour.getDayOfTheWeek().getCode() == dayOfTheWeek.getCode()) {
                    return true;
                }
            }
        }
        return false;
    }

	public List<WorkingHourEntity> cloneWorkingHourEntities() {
		List<WorkingHourEntity> workingHourEntities = new ArrayList<>();
		for (WorkingHourEntity entity : workingTime) {
			FacilityEntity facilityEntity = new FacilityEntity(entity.getFacilityEntity());
			List<OpeningHour> openingHours = new ArrayList<>();
			for (OpeningHour openingHour : entity.getWorkingTime()) {
				openingHours.add(new OpeningHour(openingHour));
			}
			workingHourEntities.add(new WorkingHourEntity(openingHours, facilityEntity));
		}
		return workingHourEntities;
	}
}