package com.kms.cura.model;

/**
 * Created by linhtnvo on 6/6/2016.
 */
public class Settings {
    public static final String SERVER_URL = "http://192.168.74.141:8080/cura/cura_server";
    public static final String CREATE_PATIENT_API = "/user/createPatient";
    public static final String USER_LOGIN = "/user/userLogin";
    public static final String GET_ALL_SPECIALITY = "/speciality/getAll";
    public static final int MY_PERMISSION_LOCATION = 100;
    public static final String GET_ALL_FACILITY = "/facility/getAll";
    public static final String GET_ALL_DEGREE = "/degree/getAll";
    public static final String CREATE_DOCTOR_API = "/user/createDoctor";
    public static final String SEARCH_DOCTOR_API = "/search/searchDoctor";
    public static final String GET_ALL_CONDITION = "/condition/getAll";
    public static final String GET_ALL_SYMPTOM = "/symptom/getAll";
    public static final String GET_ASSOCIATED_CONDITION = "/condition/getAssociatedCondition";
    public static final String GET_ASSOCIATED_SYMPTOM = "/symptom/getAssociatedSymptom";
    public static final String GET_SPECIALITY_CONDITION = "/speciality/getByCondition";
    public static final String GET_APPT = "/appt/getBookAppts";
    public static final String UPDATE_PATIENT_HEALTH = "/user/updatePatientHealth";
    public static final String CREATE_APPT = "/appt/createAppts";
}
