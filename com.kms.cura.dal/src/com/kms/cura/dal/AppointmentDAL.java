package com.kms.cura.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.kms.cura.dal.database.AppointmentDatabaseHelper;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;

public class AppointmentDAL {
    private static AppointmentDAL _instance;

    private AppointmentDAL() {
        // TODO Auto-generated constructor stub
    }

    public static AppointmentDAL getInstance() {
        if (_instance == null) {
            _instance = new AppointmentDAL();
        }
        return _instance;
    }

    public List<AppointmentEntity> getAppointment(AppointSearchEntity criteria, PatientUserEntity patientUserEntity,
			DoctorUserEntity doctorUserEntity) throws ClassNotFoundException, SQLException, IOException {
        AppointmentDatabaseHelper appointmentDatabaseHelper = new AppointmentDatabaseHelper();
        try {
            return appointmentDatabaseHelper.getAppointment(criteria, patientUserEntity, doctorUserEntity);
        } finally {
            appointmentDatabaseHelper.closeConnection();
        }
    }

    public List<AppointmentEntity> bookAppointment(AppointmentEntity entity)
			throws ClassNotFoundException, SQLException, IOException {
        AppointmentDatabaseHelper dbh = new AppointmentDatabaseHelper();
        try {
            return dbh.bookAppointment(entity);
        } finally {
            dbh.closeConnection();
        }
    }

    public List<AppointmentEntity> updateAppointment(AppointmentEntity entity) throws ClassNotFoundException, SQLException, IOException {
        AppointmentDatabaseHelper dbh = new AppointmentDatabaseHelper();
        try {
            return dbh.updateAppointment(entity);
        } finally {
            dbh.closeConnection();
        }
    }
}
