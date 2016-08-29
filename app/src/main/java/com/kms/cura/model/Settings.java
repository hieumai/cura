package com.kms.cura.model;

/**
 * Created by linhtnvo on 6/6/2016.
 */
public class Settings {
    public static final String SERVER_URL = "http://192.168.74.141:8080/cura/cura_server";
    public static final String IMAGE_URL = "http://192.168.74.141:8080/cura/image/";
    public static final String CREATE_PATIENT_API = "/user/createPatient";
    public static final String USER_LOGIN = "/user/userLogin";
    public static final String GET_ALL_SPECIALITY = "/speciality/getAll";
    public static final int MY_PERMISSION_LOCATION = 100;
    public static final int MY_PERMISSION_READ_EXTERNAL_STORAGE = 101;
    public static final String GET_ALL_FACILITY = "/facility/getAll";
    public static final String GET_ALL_DEGREE = "/degree/getAll";
    public static final String CREATE_DOCTOR_API = "/user/createDoctor";
    public static final String UPDATE_PROFILE_PHOTO = "/user/updatePhoto";
    public static final String UPDATE_PATIENT = "/user/updatePatient";
    public static final String UPDATE_PASSWORD = "/user/updatePassword";
    public static final String SEARCH_DOCTOR_API = "/search/searchDoctor";
    public static final String GET_ALL_CONDITION = "/condition/getAll";
    public static final String GET_ALL_SYMPTOM = "/symptom/getAll";
    public static final String GET_ASSOCIATED_CONDITION = "/condition/getAssociatedCondition";
    public static final String GET_ASSOCIATED_SYMPTOM = "/symptom/getAssociatedSymptom";
    public static final String GET_SPECIALITY_CONDITION = "/speciality/getByCondition";
    public static final String GET_APPT = "/appt/getBookAppts";
    public static final String UPDATE_PATIENT_HEALTH = "/user/updatePatientHealth";
    public static final String CREATE_APPT = "/appt/createAppt";
    public static final String UPDATE_APPT = "/appt/updateAppt";
    public static final String GET_MESSAGE_PATIENT = "/message/getByPatient";
    public static final String GET_MESSAGE_DOCTOR = "/message/getByDoctor";
    public static final String DELETE_PATIENT_MESSAGE = "/message/deletePatientMessage";
    public static final String DELETE_DOCTOR_MESSAGE = "/message/deleteDoctorMessage";
    public static final String INSERT_MESSAGE = "/message/insertMessage";
    public static final String GET_DOCTOR_BY_FACILITY = "/user/getDoctorByFacility";
    public static final String CHECK_EMAIL = "/user/checkEmailExist";
    public static final int TIME_OUT = 60000;
    public static final String GET_NOTIFICATION = "/noti/getNotiByType";
    public static final String SEND_CODE = "/user/sendCode";
    public static final String CHECK_CODE = "/user/checkCode";
    public static final String RATE_APPT = "/appt/rateAppt";
}
