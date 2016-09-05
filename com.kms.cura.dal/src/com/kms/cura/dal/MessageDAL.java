package com.kms.cura.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.kms.cura.dal.database.MessageDatabaseHelper;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;

public class MessageDAL extends EntityDAL {
	private static MessageDAL _instance;

	private MessageDAL() {
	}

	public static MessageDAL getInstance() {
		if (_instance == null) {
			_instance = new MessageDAL();
		}
		return _instance;
	}

	public List<MessageEntity> getMessageForPatient(PatientUserEntity patient) throws ClassNotFoundException, SQLException, IOException {
		MessageDatabaseHelper dbh = new MessageDatabaseHelper();
		try {
			return dbh.queryMessageByPatient(patient);
		} finally {
			dbh.closeConnection();
		}
	}

	public List<MessageEntity> getMessageForDoctor(DoctorUserEntity doctor) throws ClassNotFoundException, SQLException, IOException {
		MessageDatabaseHelper dbh = new MessageDatabaseHelper();
		try {
			return dbh.queryMessageByDoctor(doctor);
		} finally {
			dbh.closeConnection();
		}
	}

	public void deletePatientMessage(MessageEntity entity) throws SQLException, ClassNotFoundException {
		MessageDatabaseHelper dbh = new MessageDatabaseHelper();
		try {
			dbh.deletePatientMessage(entity);
		} finally {
			dbh.closeConnection();
		}
	}

	public void deleteDoctorMessage(MessageEntity entity) throws SQLException, ClassNotFoundException {
		MessageDatabaseHelper dbh = new MessageDatabaseHelper();
		try {
			dbh.deleteDoctorMessage(entity);
		} finally {
			dbh.closeConnection();
		}
	}

	public void insertMessage(MessageEntity entity, boolean sentByDoctor) throws ClassNotFoundException, SQLException, IOException {
		MessageDatabaseHelper dbh = new MessageDatabaseHelper();
		try {
			dbh.insertMessage(entity, sentByDoctor);
		} finally {
			dbh.closeConnection();
		}
	}
}
