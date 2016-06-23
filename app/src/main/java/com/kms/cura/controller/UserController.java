package com.kms.cura.controller;

import android.content.Context;
import android.util.Base64;

import com.kms.cura.entity.DegreeEntity;
import com.kms.cura.R;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.UserModel;

import java.sql.Date;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class UserController {
    private static final String ENCRYPT_METHOD = "PBEWithMD5AndDES";
    private static final String SIGNED_IN_INFO = "userInfo";
    private static final byte[] SALT = {
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };

    public static void registerPatient(String name, String email, String password) {
        UserEntity entity = new UserEntity(null, name, email, password);
        UserModel.getInstace().registerPatient(entity);
    }

    public static void registerDoctor(String id, String name, String email, String password, String phone, DegreeEntity degree,
                                      List<SpecialityEntity> speciality, List<FacilityEntity> facility, String gender, Date birth) {
        DoctorUserEntity entity = new DoctorUserEntity(id, name, email, password, phone, degree, speciality, facility, gender, birth);
        UserModel.getInstace().registerDoctor(entity);
    }

    public static void userLogin(String email, String password) {
        UserEntity entity = new UserEntity(null, null, email, password);
        UserModel.getInstace().userLogin(entity);
    }

    public static boolean saveNewUser(String email, String password) {
        return UserModel.getInstace().save(new UserEntity(null, null, email, password));
    }

    public static String encrypt(String pwd, char[] ENCRYPT_KEY) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPT_METHOD);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(ENCRYPT_KEY));
            Cipher pbeCipher = Cipher.getInstance(ENCRYPT_METHOD);
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return base64Encode(pbeCipher.doFinal(pwd.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException
                | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return pwd;
    }

    public static String decrypt(String pwd, char[] ENCRYPT_KEY) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPT_METHOD);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(ENCRYPT_KEY));
            Cipher pbeCipher = Cipher.getInstance(ENCRYPT_METHOD);
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return new String(pbeCipher.doFinal(base64Decode(pwd)), "UTF-8");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | NoSuchPaddingException | IOException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return pwd;
    }

    public static String base64Encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static byte[] base64Decode(String property) throws IOException {
        return Base64.decode(property, Base64.DEFAULT);
    }

    public static boolean checkSignIn(Context context) {
        File file = new File(context.getFilesDir(), SIGNED_IN_INFO);
        return file.exists();
    }

    public static void autoSignIn(Context context) {
        File file = new File(context.getFilesDir(), SIGNED_IN_INFO);
        try {
            Scanner scanner = new Scanner(new FileInputStream(file));
            String email = scanner.nextLine();
            String pwd = scanner.nextLine();
            scanner.close();
            userLogin(email, decrypt(pwd, context.getResources().getString(R.string.encrypt_key).toCharArray()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveLoginInfo(Context context, String username, String pwd) {
        File file = new File(context.getFilesDir(), SIGNED_IN_INFO);
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(file));
            writer.write(username);
            writer.write('\n');
            writer.write(encrypt(pwd, context.getResources().getString(R.string.encrypt_key).toCharArray()));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
