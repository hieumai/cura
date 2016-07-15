package com.kms.cura.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.UserEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by linhtnvo on 6/17/2016.
 */
public class DataUtils {
    public static long MILISECOND_OF_DAY = 86400000;

    public static List<String> getListName(List<? extends Entity> list) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            names.add(list.get(i).getName());
        }
        return names;
    }

    public static String showUnicode(String src) {
        if (src == null) {
            return null;
        }
        String convert = "";
        try {
            convert = new String(src.getBytes("ISO-8859-1"), "UTF-8");
            return convert;
        } catch (UnsupportedEncodingException e) {
            return src;
        }
    }

    public static List<AppointmentEntity> getAcceptedApptList(List<AppointmentEntity> list) {
        List<AppointmentEntity> entities = new ArrayList<>();
        for (AppointmentEntity entity : list) {
            if (entity.getStatus() == AppointmentEntity.ACCEPTED_STT) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public static List<AppointmentEntity> getUpcomingAppts(List<AppointmentEntity> appts) {
        List<AppointmentEntity> upcomingAppts = new ArrayList<>();
        Date current = gettheCurrent();
        long currentTime = current.getTime();
        for (AppointmentEntity entity : appts) {
            long apptTime = entity.getApptDay().getTime();
            if (entity.getApptDay().after(current)) {
                upcomingAppts.add(entity);
            } else if (Math.abs(currentTime - apptTime) < MILISECOND_OF_DAY) {
                upcomingAppts.add(entity);
            }
        }
        return upcomingAppts;
    }

    public static List<AppointmentEntity> getPastAppts(List<AppointmentEntity> appts) {
        List<AppointmentEntity> pastAppts = new ArrayList<>();
        Date current = gettheCurrent();
        long currentTime = current.getTime();
        for (AppointmentEntity entity : appts) {
            long apptTime = entity.getApptDay().getTime();
            if (entity.getApptDay().before(current) &&
                    (Math.abs(currentTime - apptTime) > MILISECOND_OF_DAY)) {
                pastAppts.add(entity);
            }
        }
        return pastAppts;
    }

    private static Date gettheCurrent() {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        return date;
    }

    public static void loadProfile(UserEntity user, ImageView profile) {
        if (user.getImage() != null && user.getImage().getImage() != null && !user.getImage().getImage().isEmpty()) {
            byte[] decodedString = Base64.decode(user.getImage().getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profile.setImageBitmap(decodedByte);
        } else {
            profile.setImageResource(R.drawable.profile_anon128);
        }
    }

}