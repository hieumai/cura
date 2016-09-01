package com.kms.cura.view;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.activity.DoctorAppointmentDetailActivity;
import com.kms.cura.view.fragment.CalendarView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by linhtnvo on 8/2/2016.
 */
public class DoctorApptDayVIew implements View.OnClickListener, EventHandler{
    public static long MILISECOND_OF_DAY = 86400000;
    private static int MIN_TIME_SPAN = 30;
    private static long MILISECOND = 60000;
    private LinearLayout layoutDayView;
    private ScrollView scrollView;
    private int green, lightGrey, lightGrey2, grey, yellow, blue;
    private int length;
    private Context mContext;
    private String title;
    private Date date;
    private float downY = 0;
    private float upY = 0;
    private List<AppointmentEntity> apptList;
    private LayoutInflater inflater;
    private String[] timeFrame;
    private List<View> viewList;

    public DoctorApptDayVIew(Context mContext, Date date) {
        this.mContext = mContext;
        this.date = date;
    }

    public View createView(){
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View root = inflater.inflate(R.layout.day_view_layout, null);
        EventBroker.getInstance().register(this, EventConstant.UPDATE_APPT_DOCTOR_LIST);
        layoutDayView = (LinearLayout) root.findViewById(R.id.layoutDayView);
        scrollView = (ScrollView) root.findViewById(R.id.scrollview);
        getData();
        createTimeFrame();
        markTheApptTime();
        scrollView.removeAllViews();
        scrollView.addView(layoutDayView);
        root.setTag(date);
        return root;
    }


    public void getData() {
        title = getSelectedDate();
        List<AppointmentEntity> apptByDate = DataUtils.getApptByDate(((DoctorUserEntity)CurrentUserProfile.getInstance().getEntity()).getAppointmentList(),date );
        apptList = new ArrayList<>();
        for (AppointmentEntity entity : apptByDate){
            int status = entity.getStatus();
            if (status != AppointmentEntity.PENDING_STT && status != AppointmentEntity.PATIENT_CANCEL_STT && status != AppointmentEntity.REJECT_STT){
                apptList.add(entity);
            }
        }
    }

    public String getSelectedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOftheWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());
        StringBuilder builder = new StringBuilder();
        builder.append(dayOftheWeek);
        builder.append(", ");
        builder.append(month_name);
        builder.append(" ");
        builder.append(calendar.get(Calendar.DAY_OF_MONTH));
        builder.append(", ");
        builder.append(calendar.get(Calendar.YEAR));
        return builder.toString();
    }


    private void createTimeFrame() {
        getColor();
        getTimeSpan();
        viewList = new ArrayList<>();
        for (int i = 0; i < length; ++i) {
            layoutDayView.addView(createTimeView(i));
        }
        if (layoutDayView.getParent() != null) {
            ((ViewGroup) layoutDayView.getParent()).removeView(layoutDayView);
        }
    }

    private void markTheApptTime(){
        List<AppointmentEntity> appointments = ((DoctorUserEntity)CurrentUserProfile.getInstance().getEntity()).getAppointmentList();
        for (AppointmentEntity entity : apptList){
            Time start = entity.getStartTime();
            Time end = entity.getEndTime();
            int startPos = getpositionTime(start);
            long timeDif = (end.getTime() - start.getTime())/MILISECOND;
            int gap = (int) (timeDif/MIN_TIME_SPAN);
            int color = colorTheAppt(entity);
            for (int i=0; i<gap; ++i){
                View v = viewList.get(i+startPos);
                v.setBackgroundColor(color);
                v.setTag(appointments.indexOf(entity));
                v.setOnClickListener(this);
            }
            setInfo(gap, startPos, entity);
        }
    }

    private void setInfo(int gap, int startPos, AppointmentEntity entity){
        StringBuilder builder = new StringBuilder();
        String pName = entity.getPatientUserEntity().getName();
        String time = getApptTime(entity.getStartTime(), entity.getEndTime());
        TextView patientName = (TextView) viewList.get(startPos);
        if (gap == 1) {
            builder.append(pName);
            builder.append(" - ");
            builder.append(time);
            patientName.setText(builder.toString());
        }
        else{
            TextView txtTime = (TextView) viewList.get(startPos + 1);
            patientName.setText(pName);
            txtTime.setText(time);
        }

    }

    private String getApptTime(Time start, Time end){
        StringBuilder builder = new StringBuilder();
        builder.append(start.toString().substring(0,5));
        builder.append("-");
        builder.append(end.toString().substring(0,5));
        return builder.toString();
    }



    private Integer colorTheAppt(AppointmentEntity entity){
        switch (entity.getStatus()){
            case AppointmentEntity.ACCEPTED_STT:
                return blue;
            case AppointmentEntity.DOCTOR_CANCEL_STT:
                return grey;
            case AppointmentEntity.INCOMPLETED_STT:
                return yellow;
            case AppointmentEntity.COMPLETED_STT:
                return green;
        }
        return null;
    }

    private int getpositionTime(Time src) {
        int position = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(src);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        position = hour * 2;
        if (min == MIN_TIME_SPAN) {
            position += 1;
        } else if (min > MIN_TIME_SPAN) {
            position += 2;
        }
        return position;
    }

    private View createTimeView(int position) {
        View view = inflater.inflate(R.layout.time_selection_layout, null);
        TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
        TextView txtTimeChosen = (TextView) view.findViewById(R.id.txtTimeChosen);
        txtTime.setText(timeFrame[position]);
        if (position % 2 == 0) {
            txtTime.setBackgroundResource(R.drawable.border_right);
            txtTimeChosen.setBackgroundColor(lightGrey);
        } else {
            txtTime.setBackgroundResource(R.drawable.border_right_2);
            txtTimeChosen.setBackgroundColor(lightGrey2);
        }
        viewList.add(txtTimeChosen);
        return view;
    }

    private void getColor() {
        green = ContextCompat.getColor(mContext, R.color.light_green);
        lightGrey = ContextCompat.getColor(mContext, R.color.light_grey);
        lightGrey2 = ContextCompat.getColor(mContext, R.color.light_grey_2);
        grey = ContextCompat.getColor(mContext, R.color.grey2);
        yellow = ContextCompat.getColor(mContext, R.color.light_yellow);
        blue = ContextCompat.getColor(mContext, R.color.blue);
    }


    private void getTimeSpan() {
        timeFrame = mContext.getResources().getStringArray(R.array.TimeFrame48);
        length = timeFrame.length;
    }


    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Intent toApptDetail = new Intent(mContext, DoctorAppointmentDetailActivity.class);
        toApptDetail.putExtra(CalendarView.APPT_POSITION, position);
        mContext.startActivity(toApptDetail);
    }


    public String getTitle() {
        return title;
    }

    @Override
    public void handleEvent(String event, Object data) {
        switch (event){
            case EventConstant.UPDATE_APPT_DOCTOR_LIST:
                int position = (int) data;
                AppointmentEntity appointment = ((DoctorUserEntity)CurrentUserProfile.getInstance().getEntity()).getAppointmentList().get(position);
                for (View v : viewList){
                    if (v.getTag() != null) {
                        if ((int) v.getTag() == position) {
                            v.setBackgroundColor(colorTheAppt(appointment));
                        }
                    }
                }
                break;
        }
    }
}
