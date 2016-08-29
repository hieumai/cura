package com.kms.cura_server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.kms.cura.dal.AppointmentDAL;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;

public class MainServer extends HttpServlet {
	private static long TIME_OF_DAY = 86400000;
	public void init() throws ServletException {
		// disableSSL();
		scheduleExistingRequest();
	}

	public void scheduleJobWhenRequestAppt(String id, Date apptDate) {
		createSchedule(id, apptDate);
	}

	private void scheduleExistingRequest() {
		System.out.println("Schedule Existing Request");
		AppointmentEntity criteria = new AppointmentEntity(null, null, null, null, null, null, null,
				AppointmentEntity.PENDING_STT, null, null);
		try {
			List<AppointmentEntity> list = AppointmentDAL.getInstance()
					.getAppointment(new AppointSearchEntity(criteria), null, null);
			for (AppointmentEntity entity : list) {
				createSchedule(entity.getId(), entity.getApptDay());
			}
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO : log for server
		}

	}

	private void createSchedule(final String id, Date apptDate) {
		long currentTime = System.currentTimeMillis();
		long apptTime = apptDate.getTime();
		Date timeToRun = new Date(Math.abs((apptTime - currentTime) + TIME_OF_DAY));
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			private String requestId = id;
			public void run() {
				AppointmentEntity criteria = new AppointmentEntity(requestId, null, null, null, null, null, null, -1, null,
						null);
				try {
					List<AppointmentEntity> list = AppointmentDAL.getInstance()
							.getAppointment(new AppointSearchEntity(criteria), null, null);
					AppointmentEntity appt = list.get(0);
					if (appt.getStatus() == AppointmentEntity.PENDING_STT) {
						appt.setStatus(AppointmentEntity.REJECT_STT);
						List<AppointmentEntity> newlist = AppointmentDAL.getInstance().updateAppointment(appt, false);
						// sendPushNotification
						System.out.println("Auto reject appointment request");
					}
				} catch (ClassNotFoundException | SQLException | IOException e) {
					// TODO : log for server
				} finally {
					timer.cancel();
					timer.purge();
				}
			}
		}, timeToRun);
	}

	private void disableSSL() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub

			}
		} };

		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
			URLConnection con = url.openConnection();
			Reader reader = new InputStreamReader(con.getInputStream());
			while (true) {
				int ch = reader.read();
				if (ch == -1) {
					break;
				}
			}
		} catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
		}
	}

}
