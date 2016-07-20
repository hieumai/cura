package com.kms.cura.entity;

import java.sql.Date;

/**
 * Created by toanbnguyen on 6/22/2016.
 */
public class HealthEntity extends Entity {
    private Date start_date;
    private Date end_date;
    private ConditionEntity condition;
    private SymptomEntity symptom;
    private String comment;

    public final static String HEALTH_LIST = "healthEntities";

    public HealthEntity(Date startDate, ConditionEntity condition, SymptomEntity symptom, String comment) {
        this.start_date = startDate;
        this.comment = comment;
        this.condition = condition;
        this.symptom = symptom;
    }

    public HealthEntity(Date startDate, Date endDate, ConditionEntity condition, SymptomEntity symptom, String comment) {
        this.start_date = startDate;
        this.end_date = endDate;
        this.comment = comment;
        this.condition = condition;
        this.symptom = symptom;
    }

    public Date getStartDate() {
        return start_date;
    }

    public void setStartDate(Date startDate) {
        this.start_date = startDate;
    }

    public Date getEndDate() {
        return end_date;
    }

    public void setEndDate(Date endDate) {
        this.end_date = endDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCondition() {
        return (condition != null);
    }

    public boolean isPastHealth() {
        return (end_date != null);
    }

    public String getName() {
        if (isCondition())
            return condition.getName();
        return symptom.getName();
    }
    
    public String getId() {
    	if (isCondition())
    		return condition.getId();
    	return symptom.getId();
    }
}
