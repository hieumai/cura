package com.kms.cura.utils;

import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by linhtnvo on 6/8/2016.
 */
public class CurrentUserProfile {
    private static CurrentUserProfile mInstance;
    private UserEntity entity;
    private List<AppointmentEntity> upcomingAppts;
    private List<AppointmentEntity> pastAppts;

    private CurrentUserProfile() {
        //
    }

    public static CurrentUserProfile getInstance() {
        if (mInstance == null) {
            mInstance = new CurrentUserProfile();
        }
        return mInstance;
    }

    public void setData(UserEntity src) {
        this.entity = src;
    }

    public UserEntity getEntity() {
        return entity;
    }

    public boolean isDoctor() {
        return (entity instanceof DoctorUserEntity);
    }

    public boolean isPatient() {
        return (entity instanceof PatientUserEntity);
    }

    public List<AppointmentEntity> getUpcomingAppts(){
        if(upcomingAppts != null){
            return upcomingAppts;
        }
        upcomingAppts = new ArrayList<>();
        List<AppointmentEntity> appts = new ArrayList<>();
        if(this.isDoctor()){
            appts.addAll(((DoctorUserEntity) entity).getAppointmentList());
        }
        else{
            appts.addAll(((PatientUserEntity) entity).getAppointmentList());
        }
        Date current = gettheCurrent();
        for(AppointmentEntity entity : appts){
            if(entity.getApptDay().after(current)){
                upcomingAppts.add(entity);
            }
        }
        return upcomingAppts;
    }

    public List<AppointmentEntity> getPastAppts(){
        if(pastAppts != null){
            return pastAppts;
        }
        pastAppts = new ArrayList<>();
        List<AppointmentEntity> appts = new ArrayList<>();
        if(this.isDoctor()){
            appts.addAll(((DoctorUserEntity) entity).getAppointmentList());
        }
        else{
            appts.addAll(((PatientUserEntity) entity).getAppointmentList());
        }
        Date current = gettheCurrent();
        for(AppointmentEntity entity : appts){
            if(entity.getApptDay().before(current)){
                pastAppts.add(entity);
            }
        }
        return pastAppts;
    }

    private Date gettheCurrent(){
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        return new Date(date.getTime());
    }
}
