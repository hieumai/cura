package com.kms.cura.view.fragment;

import com.kms.cura.R;

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

    public Integer getStatusName(){
        switch (status){
            case 0:
                return R.string.Pending;
            case 2:
                return R.string.Rejected;
            case 3:
                return R.string.Cancel;
            case 4:
                return R.string.Cancel;
            case 5:
                return R.string.Complete;
            case 6:
                return R.string.Incomplete;
        }
        return null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
