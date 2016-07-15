package com.kms.cura.dal.database;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.kms.cura.dal.exception.DALException;
import com.kms.cura.dal.exception.DuplicatedUserEmailException;
import com.kms.cura.dal.mapping.UserColumn;
import com.kms.cura.entity.ImageEntity;
import com.kms.cura.entity.user.UserEntity;

public class UserDatabaseHelper extends DatabaseHelper {

	private static final String IMAGE = "image";
	private static String PATH = System.getProperty("catalina.base");

	public UserDatabaseHelper() throws ClassNotFoundException, SQLException {
		super();
	}

	public UserEntity insertUser(UserEntity entity)
			throws SQLException, DALException, ClassNotFoundException, IOException {
		PreparedStatement stmt = null, stmt2 = null;
		ResultSet rs = null;
		if (!(entity instanceof UserEntity)) {
			return null;
		}
		try {
			con.setAutoCommit(false);

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM ");
			sb.append(UserColumn.TABLE_NAME);
			sb.append(" WHERE ");
			sb.append(UserColumn.EMAIL.getColumnName());
			sb.append(" = ?");

			stmt = con.prepareStatement(sb.toString());
			stmt.setString(1, entity.getEmail());
			rs = stmt.executeQuery();
			if (rs.next()) {
				throw new DuplicatedUserEmailException(entity.getEmail());
			}

			sb.setLength(0);
			sb.append("INSERT INTO ");
			sb.append(UserColumn.TABLE_NAME);
			sb.append(" (");
			sb.append(UserColumn.EMAIL.getColumnName());
			sb.append(",");
			sb.append(UserColumn.PASSWORD.getColumnName());
			sb.append(")");
			sb.append(" VALUES (?,?)");

			stmt2 = con.prepareStatement(sb.toString());
			stmt2.setString(1, entity.getEmail());
			stmt2.setString(2, entity.getPassword());
			stmt2.executeUpdate();

			con.commit();

			stmt.setString(1, entity.getEmail());
			rs = stmt.executeQuery();
			con.commit();
			UserDatabaseHelper dbh = new UserDatabaseHelper();
			rs.next();
			return dbh.getEntityFromResultSet(rs);
		} catch (SQLException e) {
			if (con != null) {
				System.err.print("Transaction is being rolled back");
				con.rollback();
			}
			throw e;

		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
		}
	}

	protected PreparedStatement createPreparedStatement(Map<String, Object> valueMap, String databaseName,
			String databaseMethod) throws SQLException {
		StringBuilder insertString = new StringBuilder(databaseMethod + " " + databaseName + " (");
		StringBuilder valueString = new StringBuilder(") VALUES (");
		boolean isFirst = true;
		for (Entry<String, Object> entry : valueMap.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				insertString.append(",");
				valueString.append(",");
			}
			insertString.append(entry.getKey());
			valueString.append("?");
		}
		valueString.append(")");
		String statementString = insertString.toString() + valueString.toString();
		PreparedStatement stmt = con.prepareStatement(statementString);
		setPreparedStatementValue(valueMap, stmt);
		return stmt;
	}

	protected void setPreparedStatementValue(Map<String, Object> valueMap, PreparedStatement stmt) throws SQLException {
		int count = 1;
		for (Entry<String, Object> valueEntry : valueMap.entrySet()) {
			if (valueEntry.getValue() instanceof Integer) {
				stmt.setInt(count, (Integer) valueEntry.getValue());
			} else if (valueEntry.getValue() instanceof String) {
				stmt.setString(count, (String) valueEntry.getValue());
			} else if (valueEntry.getValue() instanceof Date) {
				stmt.setDate(count, (Date) valueEntry.getValue());
			} else if (valueEntry.getValue() instanceof Double) {
				stmt.setDouble(count, (Double) valueEntry.getValue());
			} else {
				// handle for null
				stmt.setString(count, null);
			}
			count++;
		}
	}

	@Override
	protected UserEntity getEntityFromResultSet(ResultSet resultSet)
			throws SQLException, ClassNotFoundException, IOException {
		try {
			String id, email, password;
			String path = resultSet.getString(UserColumn.IMAGE_PATH.getColumnName());

			id = resultSet.getString(UserColumn.ID.getColumnName());
			email = resultSet.getString(UserColumn.EMAIL.getColumnName());
			password = resultSet.getString(UserColumn.PASSWORD.getColumnName());
			return new UserEntity(id, null, email, password, path);
		} finally {
		}

	}

	public UserEntity queryUserEntitybyEmailPassword(String childTable, String email, String password,
			String childRefIdColumnName) throws SQLException, ClassNotFoundException, IOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM ");
		builder.append(childTable);
		builder.append(" LEFT JOIN ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(" ON ");
		builder.append(childTable);
		builder.append(".");
		builder.append(childRefIdColumnName);
		builder.append(" = ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(".");
		builder.append(UserColumn.ID);
		builder.append(" WHERE ");
		builder.append(UserColumn.EMAIL.getColumnName());
		builder.append(" = ? AND ");
		builder.append(UserColumn.PASSWORD.getColumnName());
		builder.append(" = ?");
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.setString(1, email);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				return getEntityFromResultSet(rs);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return null;
	}

	public UserEntity queryByEmail(String email) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con
					.prepareStatement("SELECT * FROM " + UserColumn.TABLE_NAME + " WHERE " + UserColumn.EMAIL + " = ?");
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				return new UserEntity(rs.getString(UserColumn.ID.getColumnName()), null,
						rs.getString(UserColumn.EMAIL.getColumnName()),
						rs.getString(UserColumn.PASSWORD.getColumnName()));
			}
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public UserEntity queryUserEntitybyId(String childTable, String id, String childRefIdColumnName)
			throws SQLException, ClassNotFoundException, IOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM ");
		builder.append(childTable);
		builder.append(" LEFT JOIN ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(" ON ");
		builder.append(childTable);
		builder.append(".");
		builder.append(childRefIdColumnName);
		builder.append(" = ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(".");
		builder.append(UserColumn.ID.getColumnName());
		builder.append(" WHERE ");
		builder.append(UserColumn.ID.getColumnName());
		builder.append(" = ?");
		try {
			stmt = con.prepareStatement(builder.toString());
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				return getEntityFromResultSet(rs);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return null;
	}

	public void updateProfile(UserEntity user) throws SQLException, ClassNotFoundException, IOException {
		String encodedImage = user.getImage().getEncodedImage();
		ImageEntity image = new ImageEntity(encodedImage, saveImage(encodedImage), null, null);
		user.setImageEntity(image);
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(" SET ");
		builder.append(UserColumn.IMAGE_PATH);
		builder.append(" = ");
		builder.append(" ? ");
		builder.append(" WHERE ");
		builder.append(UserColumn.ID);
		builder.append(" = ? ");
		try {
			con.setAutoCommit(false);
			stmt2 = con
					.prepareStatement("SELECT * FROM " + UserColumn.TABLE_NAME + " WHERE " + UserColumn.EMAIL + " = ?");
			stmt2.setString(1, user.getEmail());
			rs = stmt2.executeQuery();
			if (rs != null && rs.next()) {
				String path = PATH + File.separator + IMAGE + rs.getString(UserColumn.IMAGE_PATH.getColumnName());
				if (path != null) {
					File oldImage = new File(path);
					oldImage.delete();
				}
			}
			stmt = con.prepareStatement(builder.toString());
			stmt.setString(1, user.getImage().getPath());
			stmt.setInt(2, Integer.parseInt(user.getId()));
			stmt.executeUpdate();
			con.commit();
		} catch (Exception e) {
			con.rollback();
		} finally {
			con.setAutoCommit(true);
			if (stmt != null) {
				stmt.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
		}
	}

	private String saveImage(String base64String) throws IOException {
		// create a buffered image
		BufferedImage image = null;
		byte[] imageByte;

		imageByte = Base64.decodeBase64(base64String);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
		try {
			image = ImageIO.read(bis);
		} finally {
			bis.close();
		}
		// write the image to a file

		File imageDir = new File(PATH + File.separator + IMAGE);
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String fileName = fmt.format(new java.util.Date());
		StringBuilder sb = new StringBuilder();
		sb.append(imageDir.getAbsolutePath());
		sb.append("\\");
		sb.append(fileName + "." + ImageEntity.IMAGE_FORMAT);
		File outputfile = new File(sb.toString());
		ImageIO.write(image, ImageEntity.IMAGE_FORMAT, outputfile);

		return fileName + "." + ImageEntity.IMAGE_FORMAT;
	}

	public String encodeToString(BufferedImage image, String type) throws IOException {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			imageString = new String(Base64.encodeBase64(imageBytes));

		} finally {
			bos.close();

		}
		return imageString;
	}

	public void updatePassword(UserEntity user) throws SQLException, ClassNotFoundException, IOException {

		PreparedStatement stmt = null;
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE ");
		builder.append(UserColumn.TABLE_NAME);
		builder.append(" SET ");
		builder.append(UserColumn.PASSWORD);
		builder.append(" = ");
		builder.append(" ? ");
		builder.append(" WHERE ");
		builder.append(UserColumn.ID);
		builder.append(" = ? ");
		try {
			con.setAutoCommit(false);

			stmt = con.prepareStatement(builder.toString());
			stmt.setString(1, user.getPassword());
			stmt.setInt(2, Integer.parseInt(user.getId()));
			stmt.executeUpdate();
			con.commit();
		} catch (Exception e) {
			con.rollback();
		} finally {
			con.setAutoCommit(true);
			if (stmt != null) {
				stmt.close();
			}
		}
	}
}
