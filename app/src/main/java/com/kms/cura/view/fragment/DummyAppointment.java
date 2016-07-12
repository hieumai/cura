package com.kms.cura.view.fragment;

import java.sql.Date;

/**
 * Created by linhtnvo on 7/12/2016.
 */
public class DummyAppointment {
    private String doctorName;
    private String facilityName;
    private String time;
    private int status;
    private Date date;

    public DummyAppointment(String doctorName, String facilityName, String time, int status, Date date) {
        this.doctorName = doctorName;
        this.facilityName = facilityName;
        this.time = time;
        this.status = status;
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName(){
        switch (status){
            case 0:
                return "PENDING";
            case 2:
                return "REJECTED";
            case 3:
                return "CANCELED";
            default:
                return "";
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
