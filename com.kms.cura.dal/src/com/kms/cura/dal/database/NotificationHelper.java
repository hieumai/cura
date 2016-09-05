package com.kms.cura.dal.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.gson.Gson;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.PatientUserEntity;

public class NotificationHelper {

	private static String PATH = System.getProperty("catalina.base");
	private static String GCM = "gcm";
	public static final String GOOGLE_SERVER_KEY = "AIzaSyCIlZaJ4KHGbrrevEeCnBk0lPhytav592k";
	static final String REG_ID_STORE = "GCMRegId.txt";

	public NotificationHelper() {
	}

	public void registerID(String userName, String regID) throws IOException {
		Map<String, String> regIdMap = readFromFile();
		if (regIdMap.containsKey(userName)) {
			if (regIdMap.get(userName).equals(regID)) {
				return;
			}

		}
		writeToFile(userName, regID);
	}

	public void unRegisterID(String userName) throws IOException {
		Map<String, String> regIdMap = readFromFile();
		regIdMap.remove(userName);
		StringBuilder builder = new StringBuilder();
		builder.append(PATH + File.separator);
		builder.append(GCM + File.separator);
		File file = new File(builder.toString(), REG_ID_STORE);
		if (regIdMap.isEmpty()) {
			file.delete();
			return;
		}
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(), false)));
		for (Map.Entry<String, String> entry : regIdMap.entrySet()) {
			out.println(entry.getKey() + "," + entry.getValue());
		}
		out.close();
	}

	public void sendAppointmentRequest(String toDoctor, AppointmentEntity appt) throws IOException {
		PatientUserDatabaseHelper dbh = null;
		try {
			dbh = new PatientUserDatabaseHelper();
			PatientUserEntity patient = dbh.queryPatientByID(appt.getPatientUserEntity().getId());
			PatientUserEntity newPatient = new PatientUserEntity(patient.getId(), patient.getName(), null, null, null,
					null, null, null, null, patient.getImage().getPath());
			appt.setPatientUserEntity(newPatient);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO log file for server later
		} finally {
			try {
				dbh.closeConnection();
			} catch (SQLException e) {
				// TODO : log for server
				return;
			}
		}
		Sender sender = new Sender(GOOGLE_SERVER_KEY);
		Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true)
				.addData(NotificationEntity.NOTI_TYPE, NotificationEntity.PATIENT_REQUEST_TYPE)
				.addData(NotificationEntity.PATIENT_REQUEST_TYPE,
						new Gson().toJson(appt, AppointmentEntity.getAppointmentType()).toString())
				.build();
		Map<String, String> regIdMap = readFromFile();
		String toId = regIdMap.get(toDoctor);
		if (toId == null) {
			return;
		}
		Result result = sender.send(message, toId, 1);
	}
	
	public void sendMessageRequest(MessageEntity msg) throws IOException {
		Sender sender = new Sender(GOOGLE_SERVER_KEY);
		Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true)
				.addData(NotificationEntity.NOTI_TYPE, NotificationEntity.MSG_TYPE)
				.addData(NotificationEntity.MSG_TYPE, msg.getSender().getName() + " : " + msg.getMessage())
				.build();
		Map<String, String> regIdMap = readFromFile();
		String toId = regIdMap.get(msg.getReceiverID());
		if (toId == null) {
			return;
		}
		Result result = sender.send(message, toId, 1);
	}

	public void sendAppointmentUpdateNoti(String toPatient) throws IOException {
		Sender sender = new Sender(GOOGLE_SERVER_KEY);
		Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true)
				.addData(NotificationEntity.NOTI_TYPE, NotificationEntity.UPDATE_APPT_TYPE).build();
		Map<String, String> regIdMap = readFromFile();
		String toId = regIdMap.get(toPatient);
		if (toId == null) {
			return;
		}
		Result result = sender.send(message, toId, 1);
	}

	public void sendIncompleteAppointmentNoti(String toDoctor) throws IOException {
		Sender sender = new Sender(GOOGLE_SERVER_KEY);
		Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true)
				.addData(NotificationEntity.NOTI_TYPE, NotificationEntity.INCOMPLETE_APPT_TYPE)
				.build();
		Map<String, String> regIdMap = readFromFile();
		String toId = regIdMap.get(toDoctor);
		if (toId == null) {
			return;
		}
		Result result = sender.send(message, toId, 1);
	}
	
	private void writeToFile(String name, String regId) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(PATH + File.separator);
		builder.append(GCM + File.separator);
		Map<String, String> regIdMap = readFromFile();
		regIdMap.put(name, regId);
		File file = new File(builder.toString(), REG_ID_STORE);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(), false)));
		for (Map.Entry<String, String> entry : regIdMap.entrySet()) {
			out.println(entry.getKey() + "," + entry.getValue());
		}
		out.close();

	}

	private Map<String, String> readFromFile() throws IOException {
		String path = PATH + File.separator + GCM;
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String pathFile = path + File.separator;
		File file = new File(pathFile, REG_ID_STORE);
		file.createNewFile();
		BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
		String regIdLine = "";
		Map<String, String> regIdMap = new HashMap<String, String>();
		while ((regIdLine = br.readLine()) != null) {
			String[] regArr = regIdLine.split(",");
			regIdMap.put(regArr[0], regArr[1]);
		}
		br.close();
		return regIdMap;
	}

}
