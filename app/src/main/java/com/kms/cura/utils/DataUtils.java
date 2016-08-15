package com.kms.cura.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.Entity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.Settings;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
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

    public static List<AppointmentEntity> getApptByDate(List<AppointmentEntity> appts, Date date) {
        List<AppointmentEntity> newList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (AppointmentEntity entity : appts) {
            Calendar compareCalendar = (Calendar) calendar.clone();
            compareCalendar.setTime(entity.getApptDay());
            if (isSameDay(calendar, compareCalendar)) {
                newList.add(entity);
            }
        }
        return newList;
    }

    private static boolean isSameDay (Calendar cal1, Calendar cal2){
        if (cal1 == null || cal2 == null) {
            return false;
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static void loadProfile(UserEntity user, ImageView profile, Context context) {
        if (user.getImage() != null && user.getImage().getPath() != null) {
            StringBuilder link = new StringBuilder();
            link.append(Settings.IMAGE_URL);
            link.append(user.getImage().getPath());
            Glide.with(context).load(link.toString()).into(profile);
        } else {
            profile.setImageResource(R.drawable.profile_anon128);
        }
    }

    public static List<AppointmentEntity> getAllDoctorAvailableAppt(List<AppointmentEntity> appts){
        List<AppointmentEntity> entities = new ArrayList<>();
        for (AppointmentEntity entity : appts){
            int status = entity.getStatus();
            if (status !=  AppointmentEntity.PENDING_STT && status != AppointmentEntity.PATIENT_CANCEL_STT
                    && status != AppointmentEntity.REJECT_STT ){
                entities.add(entity);
            }
        }
        return entities;
    }

}