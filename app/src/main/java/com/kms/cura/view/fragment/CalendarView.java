package com.kms.cura.view.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.CalendarListener;
import com.kms.cura.view.CustomCalendarView;
import com.kms.cura.view.DayDecorator;
import com.kms.cura.view.DayView;
import com.kms.cura.view.adapter.DoctorAppointmentAdapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by linhtnvo on 7/26/2016.
 */
public class CalendarView implements CalendarListener, AdapterView.OnItemClickListener{
    private Context mContext;
    private CustomCalendarView calendarView;
    private int currentMonthIndex;
    private int currentIndex;
    private List<AppointmentEntity> appts;
    private List<Date> listApptDay;
    private ListView lvApptList;
    private Date previousDateSelected;
    private String calendarTitle;
    private TextView txtDate;
    private Date selectedDay;
    public CalendarView(Context mContext, int currentMonthIndex) {
        this.mContext = mContext;
        this.currentMonthIndex = currentMonthIndex;
    }

    public View createView () {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.fragment_calendar_view_pager,null);
        txtDate = (TextView) root.findViewById(R.id.txtDate);
        lvApptList = (ListView) root.findViewById(R.id.lvApptList);
        lvApptList.setOnItemClickListener(this);
        initCalendar(root);
        setData();
        root.setTag(currentIndex);
        root.setTag(R.string.calendarTitle, calendarTitle);
        return root;
    }

    private void initCalendar(View root) {
        calendarView = (CustomCalendarView) root.findViewById(R.id.calendar_view);
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setShowOverflowDate(false);
        currentIndex = currentMonthIndex - currentCalendar.get(Calendar.MONTH);
        calendarView.setCurrentMonthIndex(currentIndex);
        calendarView.changeMonthCalendar();
        calendarTitle = calendarView.getTextTitle();
    }

    private void setData() {
        appts = ((DoctorUserEntity) CurrentUserProfile.getInstance().getEntity()).getAppointmentList();
        listApptDay = new ArrayList<>();
        for (AppointmentEntity entity : appts) {
            if (entity.getStatus() != AppointmentEntity.PENDING_STT && !listApptDay.contains(entity.getApptDay())) {
                listApptDay.add(entity.getApptDay());
            }
        }
        calendarView.decorateApptDay(new ColorDecorator(), listApptDay);
        calendarView.setCalendarListener(this);
    }

    @Override
    public void onDateSelected(java.util.Date date) {
        if (date == null) {
            selectedDay = new Date(calendarView.getSelectedDayNotHaveAppt().getTime());
            txtDate.setVisibility(View.INVISIBLE);
            previousDateSelected = null;
            DoctorAppointmentAdapter adapter = new DoctorAppointmentAdapter(mContext, new ArrayList<AppointmentEntity>());
            lvApptList.setAdapter(adapter);
            return;
        }
        txtDate.setVisibility(View.VISIBLE);
        txtDate.setText(getSelectedDate(date));
        selectedDay = new Date(date.getTime());
        previousDateSelected = new Date(date.getTime());
        List<AppointmentEntity> apptByDate = DataUtils.getApptByDate(appts, selectedDay);
        DoctorAppointmentAdapter adapter = new DoctorAppointmentAdapter(mContext, apptByDate);
        lvApptList.setAdapter(adapter);
    }

    public String getSelectedDate(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOftheWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        SimpleDateFormat date_format = new SimpleDateFormat("dd/mm/yyyy");
        String dateString = date_format.format(date);
        StringBuilder builder = new StringBuilder();
        builder.append(dayOftheWeek);
        builder.append(", ");
        builder.append(calendar.get(Calendar.DAY_OF_MONTH));
        builder.append("/");
        builder.append(calendar.get(Calendar.MONTH)+1);
        builder.append("/");
        builder.append(calendar.get(Calendar.YEAR));
        return builder.toString();
    }


    @Override
    public void onMonthChanged(java.util.Date time) {
        return;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class ColorDecorator implements DayDecorator {

        @Override
        public void decorate(DayView cell) {
            int color = ContextCompat.getColor(mContext, R.color.grey);
            RelativeLayout parent = (RelativeLayout) ((ViewGroup) cell.getParent());
            ImageView circle = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            circle.setLayoutParams(params);
            int dimen = (int) (mContext.getResources().getDimension(R.dimen.docText_14)/mContext.getResources().getDisplayMetrics().scaledDensity);
            circle.getLayoutParams().width = dimen;
            circle.getLayoutParams().height = dimen;
            circle.setBackgroundResource(R.drawable.circle_mark_appt_day);
            RelativeLayout.LayoutParams rLParams =
                    new RelativeLayout.LayoutParams(
                            dimen, dimen);
            rLParams.addRule(RelativeLayout.CENTER_HORIZONTAL,1);
            rLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
            rLParams.setMargins(0,0,0,dimen);
            parent.addView(circle, rLParams);
        }
    }

    public void clearAlltheContent(){
        txtDate.setVisibility(View.INVISIBLE);
        calendarView.decorateApptDay(new ColorDecorator(), listApptDay);
        DoctorAppointmentAdapter adapter = new DoctorAppointmentAdapter(mContext, new ArrayList<AppointmentEntity>());
        lvApptList.setAdapter(adapter);
        calendarView.unColorThePriviousSelected();
        calendarView.setPreviousSelected(0);
        previousDateSelected = null;
    }

    public Date getSelectedDay() {
        return selectedDay;
    }

    public void colorSelectedDayFromDayView(Date selectedDay){
        calendarView.colorSelectedDay(selectedDay);
    }
}
