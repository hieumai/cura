package com.kms.cura.dal.database;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.kms.cura.dal.AppointmentDAL;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;

public class AutoRejectHelper {
	private static long TIME_OF_DAY = 86400000;
	public AutoRejectHelper() {
		
	}
	
	public void createSchedule(final String id, Date apptDate) {
		long currentTime = System.currentTimeMillis();
		long apptTime = apptDate.getTime();
		Date timeToRun = new Date(Math.abs((apptTime - currentTime) + TIME_OF_DAY));
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			private String requestId = id;
			public void run() {
				AppointmentDatabaseHelper dbh = null;
				AppointmentEntity criteria = new AppointmentEntity(requestId, null, null, null, null, null, null, -1, null,
						null);
				try {
					dbh = new AppointmentDatabaseHelper();
					List<AppointmentEntity> list = AppointmentDAL.getInstance()
							.getAppointment(new AppointSearchEntity(criteria), null, null);
					AppointmentEntity appt = list.get(0);
					if (appt.getStatus() == AppointmentEntity.PENDING_STT) {
						appt.setStatus(AppointmentEntity.REJECT_STT);
						dbh.updateAppointment(appt, false);

					}
				} catch (ClassNotFoundException | SQLException | IOException e) {
					// TODO : log for server
				} finally {
					try {
						dbh.closeConnection();
					} catch (SQLException e) {
						//TODO : log for server
					}
					timer.cancel();
					timer.purge();
				}
			}
		}, timeToRun);
	}
}
