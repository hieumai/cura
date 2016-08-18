package com.kms.cura.dal.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.kms.cura.dal.EntityDAL;
import com.kms.cura.dal.database.DatabaseHelper;
import com.kms.cura.dal.database.PasswordCodeDatabaseHelper;
import com.kms.cura.dal.database.UserDatabaseHelper;
import com.kms.cura.dal.mapping.UserColumn;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.UserEntity;

public class UserDAL extends EntityDAL {
	private static UserDAL _instance;
	private static SecureRandom random = new SecureRandom();
	private static String ADMIN_EMAIL = "curaappmail@gmail.com";
	private static String ADMIN_PASSWORD = "curacura";

	protected UserDAL() {
		// hide constructor
	}

	public static UserDAL getInstance() {
		if (_instance == null) {
			_instance = new UserDAL();
		}
		return _instance;
	}

	public List<Entity> getAll() throws ClassNotFoundException, SQLException, IOException {
		UserDatabaseHelper dbh = null;
		try{
			dbh = new UserDatabaseHelper();
			return super.getAll(UserColumn.TABLE_NAME, dbh);
		}
		finally{
			dbh.closeConnection();
		}
	}

	public void updatePhoto(UserEntity user) throws ClassNotFoundException, SQLException, IOException {
		UserDatabaseHelper dbh = new UserDatabaseHelper();
		try {
			dbh.updateProfile(user);
		} finally {
			dbh.closeConnection();
		}
	}

	public void updatePassword(UserEntity user) throws ClassNotFoundException, SQLException, IOException {
		UserDatabaseHelper dbh = new UserDatabaseHelper();
		try {
			dbh.updatePassword(user);
		} finally {
			dbh.closeConnection();
		}
	}

	private static String randomizeCode() {
		return new BigInteger(130, random).toString(32).substring(0, 5).toUpperCase();
	}

	public boolean checkEmailExist(String email) throws ClassNotFoundException, SQLException {
		UserDatabaseHelper dbh = new UserDatabaseHelper();
		try {
			UserEntity userEntity = dbh.queryByEmail(email);
			return (userEntity != null);
		} finally {
			dbh.closeConnection();
		}
	}

	public String sendCode(String email)
			throws MessagingException, ClassNotFoundException, SQLException, UnsupportedEncodingException {
		PasswordCodeDatabaseHelper dbh = new PasswordCodeDatabaseHelper();
		UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
		String code = randomizeCode();
		try {
			while (!dbh.checkDuplicateCode(code)) {
				code = randomizeCode();
			}
			// prapare mails
			String to = email;
			String from = ADMIN_EMAIL;
			String password = ADMIN_PASSWORD;
			Properties props = new Properties();
			String host = "smtp.gmail.com";
			props.put("mail.imap.ssl.enable", "true"); // required for Gmail
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.user", from);
	        props.put("mail.smtp.password", password);
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "yes");
			Session session = Session.getInstance(props);
			// prepare message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("[Cura] Password reset code");
			message.setText("Your password resetting code is : " + code);
			// send message
			Transport.send(message, from, password);
			// replace code in database
			UserEntity userEntity = userDatabaseHelper.queryByEmail(email);
			dbh.updateNewCode(userEntity.getId(), code, new Timestamp(System.currentTimeMillis()));
			return userEntity.getId();
		} finally {
			dbh.closeConnection();
			userDatabaseHelper.closeConnection();
		}
	}
	
	public String checkCode(String userID, String code) throws Exception {
		PasswordCodeDatabaseHelper dbh = new PasswordCodeDatabaseHelper();
		return dbh.checkCode(userID, code);
	}
}