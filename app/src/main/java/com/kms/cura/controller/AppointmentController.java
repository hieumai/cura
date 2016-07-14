package com.kms.cura.controller;

import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.model.AppointmentModel;

import java.util.List;

/**
 * Created by linhtnvo on 7/15/2016.
 */
public class AppointmentController {
    public static List<AppointmentEntity> bookAppointment(AppointmentEntity appointmentEntity, PatientUserEntity patient) throws Exception {
        return AppointmentModel.getInstance().bookAppointment(appointmentEntity, patient);
    }

    public static List<AppointmentEntity> getAppointment(AppointSearchEntity entity) throws Exception {
        return AppointmentModel.getInstance().getAppointment(entity);
    }
}
